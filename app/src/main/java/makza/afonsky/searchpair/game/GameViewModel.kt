package makza.afonsky.searchpair.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import makza.afonsky.searchpair.audio.GameSound
import makza.afonsky.searchpair.audio.SoundManager
import makza.afonsky.searchpair.data.GameRepository
import makza.afonsky.searchpair.data.LevelConfig
import makza.afonsky.searchpair.data.LevelGenerator

enum class GamePhase {
    INTRO,
    PLAYING,
    DEFEAT,
    WON,
}

data class GameUiState(
    val config: LevelConfig,
    val cards: List<GameCard> = emptyList(),
    val health: Int = 0,
    val openedCardIds: List<Int> = emptyList(),
    val matchedGroups: Int = 0,
    val isInputBlocked: Boolean = true,
    val isWon: Boolean = false,
    val availableKits: Int = 0,
    val flippingCardId: Int? = null,
    val phase: GamePhase = GamePhase.INTRO,
    val flipDurationMs: Int = 300,
)

sealed class GameEvent {
    data object NavigateToBonus : GameEvent()
    data class NavigateToLevel(val level: Int) : GameEvent()
}

class GameViewModel(
    private val level: Int,
    private val repository: GameRepository,
    private val soundManager: SoundManager,
) : ViewModel() {

    private val config = LevelGenerator.generate(level)

    private val _uiState = MutableStateFlow(GameUiState(config = config))
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<GameEvent>()
    val events: SharedFlow<GameEvent> = _events.asSharedFlow()

    private var healthCheatTaps = 0
    private var introJobRunning = false
    private var matchStreak = 0

    init {
        startLevel()
    }

    fun startLevel() {
        matchStreak = 0
        repository.exchangeKitsForLevel(level)
        val kits = repository.getKitCount(config.healthKitTier)
        _uiState.value = GameUiState(
            config = config,
            cards = MatchEngine.createDeck(config),
            health = 0,
            availableKits = kits,
            phase = GamePhase.INTRO,
            isInputBlocked = true,
        )
        runIntroSnake()
    }

    private fun runIntroSnake() {
        if (introJobRunning) return
        viewModelScope.launch {
            introJobRunning = true
            playIntroSnake()
            introJobRunning = false
            if (_uiState.value.phase == GamePhase.INTRO) {
                _uiState.update {
                    it.copy(phase = GamePhase.PLAYING, isInputBlocked = false)
                }
            }
        }
    }

    private suspend fun playIntroSnake() {
        playSnakeAnimation(config.snakeOrder)
    }

    private suspend fun playHintSnake() {
        val state = _uiState.value
        val order = config.snakeOrder.filter { cardId ->
            val card = state.cards.getOrNull(cardId)
            card != null && !card.isMatched && !card.isFaceUp
        }
        if (order.isNotEmpty()) {
            playSnakeAnimation(order)
        }
    }

    private suspend fun playSnakeAnimation(cardOrder: List<Int>) {
        coroutineScope {
            cardOrder.mapIndexed { index, cardId ->
                async {
                    delay(index * INTRO_STAGGER_MS)
                    val card = _uiState.value.cards.getOrNull(cardId) ?: return@async
                    if (card.isMatched) return@async
                    setCardFaceUp(cardId, faceUp = true, durationMs = INTRO_FLIP_MS)
                    delay(INTRO_FLIP_MS.toLong())
                    val afterFlip = _uiState.value.cards.getOrNull(cardId) ?: return@async
                    if (!afterFlip.isMatched && afterFlip.isFaceUp) {
                        setCardFaceUp(cardId, faceUp = false, durationMs = INTRO_FLIP_MS)
                        delay(INTRO_FLIP_MS.toLong())
                    }
                }
            }.awaitAll()
        }
    }

    private suspend fun setCardFaceUp(cardId: Int, faceUp: Boolean, durationMs: Int) {
        if (faceUp) soundManager.play(GameSound.CARD_OPEN) else soundManager.play(GameSound.CARD_CLOSE)
        _uiState.update { state ->
            state.copy(
                flippingCardId = cardId,
                flipDurationMs = durationMs,
                cards = state.cards.map { card ->
                    if (card.id == cardId) card.copy(isFaceUp = faceUp) else card
                },
            )
        }
        delay(durationMs.toLong())
        _uiState.update { it.copy(flippingCardId = null, flipDurationMs = FLIP_DURATION_MS.toInt()) }
    }

    fun onCardClick(cardId: Int) {
        val state = _uiState.value
        if (state.isInputBlocked || state.isWon || state.phase != GamePhase.PLAYING) return

        val card = state.cards.getOrNull(cardId) ?: return
        if (card.isMatched || card.isFaceUp || state.openedCardIds.contains(cardId)) return

        viewModelScope.launch {
            flipCard(cardId)
        }
    }

    private suspend fun flipCard(cardId: Int) {
        blockInput(true)
        setCardFaceUp(cardId, faceUp = true, durationMs = FLIP_DURATION_MS.toInt())

        _uiState.update { state ->
            state.copy(openedCardIds = state.openedCardIds + cardId)
        }

        val state = _uiState.value
        when (MatchEngine.evaluate(state.openedCardIds, state.cards, config.matchSize)) {
            MatchEvaluation.CONTINUE -> blockInput(false)
            MatchEvaluation.MATCH -> handleMatch()
            MatchEvaluation.MISMATCH -> handleMismatch()
        }
    }

    private suspend fun handleMatch() {
        soundManager.play(GameSound.CRASH)
        val opened = _uiState.value.openedCardIds
        val newHealth = MatchEngine.applyHeal(_uiState.value.health, config.healOnMatch)
        val newMatched = _uiState.value.matchedGroups + 1

        matchStreak++
        var kits = repository.getKitCount(config.healthKitTier)
        if (matchStreak >= STREAK_KITS_REWARD) {
            repository.addKit(config.healthKitTier)
            kits = repository.getKitCount(config.healthKitTier)
            matchStreak = 0
        }

        _uiState.update { state ->
            state.copy(
                health = newHealth,
                matchedGroups = newMatched,
                openedCardIds = emptyList(),
                availableKits = kits,
                cards = state.cards.map { card ->
                    if (opened.contains(card.id)) card.copy(isMatched = true, isFaceUp = false)
                    else card
                },
            )
        }

        delay(MATCH_ANIMATION_MS)

        if (newMatched >= config.groupsToWin) {
            onLevelComplete()
        } else {
            blockInput(false)
        }
    }

    private suspend fun handleMismatch() {
        val opened = _uiState.value.openedCardIds
        val newHealth = MatchEngine.applyDamage(
            _uiState.value.health,
            config.damageOnMiss,
            config.healthMax,
        )

        if (MatchEngine.isDefeated(newHealth, config.healthMax)) {
            playDefeatAndRestart(newHealth)
            return
        }

        matchStreak = 0
        soundManager.play(GameSound.CARD_CLOSE)
        _uiState.update { state ->
            state.copy(
                health = newHealth,
                cards = state.cards.map { card ->
                    if (opened.contains(card.id)) card.copy(isFaceUp = false) else card
                },
                openedCardIds = emptyList(),
            )
        }

        delay(FLIP_DURATION_MS)
        blockInput(false)
    }

    private suspend fun playDefeatAndRestart(finalHealth: Int) {
        soundManager.play(GameSound.BUM)
        _uiState.update {
            it.copy(
                health = finalHealth,
                phase = GamePhase.DEFEAT,
                isInputBlocked = true,
                openedCardIds = emptyList(),
                cards = it.cards.map { card -> card.copy(isFaceUp = false) },
            )
        }
        delay(DEFEAT_ANIMATION_MS)
        startLevel()
    }

    private fun onLevelComplete() {
        if (config.isFinalLevel) {
            repository.setMaxKits()
            _uiState.update {
                it.copy(
                    isWon = true,
                    phase = GamePhase.WON,
                    isInputBlocked = true,
                    availableKits = GameRepository.MAX_KITS,
                )
            }
        } else {
            repository.saveLevelProgress(config.level)
            repository.addKit(config.healthKitTier)
            _uiState.update {
                it.copy(
                    isWon = true,
                    phase = GamePhase.WON,
                    isInputBlocked = true,
                    availableKits = repository.getKitCount(config.healthKitTier),
                )
            }
        }
    }

    fun onNextLevelClick() {
        viewModelScope.launch {
            soundManager.play(GameSound.DROP)
            if (config.isFinalLevel) {
                _events.emit(GameEvent.NavigateToBonus)
            } else {
                _events.emit(GameEvent.NavigateToLevel(config.level + 1))
            }
        }
    }

    fun onHealthKitClick() {
        val state = _uiState.value
        if (state.isInputBlocked || state.availableKits <= 0 || state.phase != GamePhase.PLAYING) return
        if (!repository.consumeKit(config.healthKitTier)) return

        blockInput(true)
        viewModelScope.launch {
            soundManager.play(GameSound.CLOSE)
            val regen = config.healthMax / 6
            val newHealth = (state.health - regen).coerceAtLeast(0)

            _uiState.update {
                it.copy(
                    health = newHealth,
                    availableKits = repository.getKitCount(config.healthKitTier),
                )
            }

            playHintSnake()
            if (_uiState.value.phase == GamePhase.PLAYING) {
                blockInput(false)
            }
        }
    }

    fun onHealthBarCheatTap() {
        healthCheatTaps++
        if (healthCheatTaps >= 20) {
            soundManager.play(GameSound.CLOSE)
            _uiState.update { it.copy(health = 0) }
            healthCheatTaps = 0
        }
    }

    /** Temporary debug: instant win and show next-level overlay. */
    fun debugForceWin() {
        if (_uiState.value.isWon) return
        onLevelComplete()
    }

    private fun blockInput(blocked: Boolean) {
        _uiState.update { it.copy(isInputBlocked = blocked) }
    }

    class Factory(
        private val level: Int,
        private val repository: GameRepository,
        private val soundManager: SoundManager,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return GameViewModel(level, repository, soundManager) as T
        }
    }

    companion object {
        private const val STREAK_KITS_REWARD = 3
        private const val FLIP_DURATION_MS = 300L
        private const val INTRO_FLIP_MS = 700
        private const val INTRO_STAGGER_MS = 125L
        private const val MATCH_ANIMATION_MS = 400L
        private const val DEFEAT_ANIMATION_MS = 1400L
    }
}
