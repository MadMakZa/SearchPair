package makza.afonsky.searchpair.multiplayer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import makza.afonsky.searchpair.R
import makza.afonsky.searchpair.game.GameCard
import makza.afonsky.searchpair.multiplayer.network.dto.InfoTypes

data class TurnBannerState(
    val text: String = "",
    val visible: Boolean = false,
)

data class MultiplayerGameUiState(
    val cards: List<GameCard> = emptyList(),
    val myUserId: Long = 0L,
    val myName: String = "",
    val opponentName: String = "",
    val myScore: Int = 0,
    val opponentScore: Int = 0,
    val totalGroups: Int = 12,
    val gridColumns: Int = MultiplayerFieldMapper.DEFAULT_COLUMNS,
    val gridRows: Int = MultiplayerFieldMapper.DEFAULT_ROWS,
    val currentPlayerId: Long = 0L,
    val isMyTurn: Boolean = false,
    val isInputEnabled: Boolean = false,
    val isGameFinished: Boolean = false,
    val winnerId: Long? = null,
    val statusMessage: String = "",
    val turnBanner: TurnBannerState = TurnBannerState(),
    val showSurrenderDialog: Boolean = false,
    val turnSecondsLeft: Int? = null,
    val myRematchReady: Boolean = false,
    val opponentRematchReady: Boolean = false,
    val countdownSeconds: Int? = null,
    val matchSize: Int = MultiplayerFieldMapper.DEFAULT_MATCH_SIZE,
    val isSearchGame: Boolean = false,
)

sealed class MultiplayerGameEvent {
    data object NavigateToLobby : MultiplayerGameEvent()
}

class MultiplayerGameViewModel(
    application: Application,
    private val repository: MultiplayerRepository,
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(MultiplayerGameUiState())
    val uiState: StateFlow<MultiplayerGameUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<MultiplayerGameEvent>()
    val events: SharedFlow<MultiplayerGameEvent> = _events.asSharedFlow()

    private var opponentId: Long = 0L
    private var latestServerCards: List<GameCard> = emptyList()
    private val animatingMatchedIds = mutableSetOf<Int>()
    private var matchAnimationJob: Job? = null
    private var turnTimerJob: Job? = null
    private var surrenderSent = false

    init {
        val pendingCellCount = repository.pendingCellCount
        val pendingMatchSize = repository.pendingMatchSize
        val pendingLayout = MultiplayerFieldMapper.layoutFor(pendingCellCount)
        _uiState.update {
            it.copy(
                myUserId = repository.getUserId(),
                myName = repository.getNickname(),
                opponentName = repository.pendingOpponentName,
                matchSize = pendingMatchSize,
                totalGroups = MultiplayerFieldMapper.totalGroups(pendingCellCount, pendingMatchSize),
                gridColumns = pendingLayout.columns,
                gridRows = pendingLayout.rows,
                isSearchGame = repository.isSearchRoom,
            )
        }
        opponentId = repository.pendingOpponentId
        repository.peekGameState()?.let { data ->
            if (data["field"].orEmpty().isNotBlank()) {
                applyGameState(data)
            }
        }
        if (repository.consumeAutoReadyOnGameEnter()) {
            viewModelScope.launch {
                runCatching { repository.sendReady() }
            }
        }
        viewModelScope.launch {
            repository.events.collect { message ->
                when {
                    message.type == "INFO" && message.infoType == InfoTypes.GAME_STATE -> {
                        if (_uiState.value.isGameFinished) return@collect
                        applyGameState(message.data)
                    }
                    message.type == "INFO" && message.infoType == InfoTypes.GAME_STARTED -> {
                        surrenderSent = false
                        val current = message.data["currentPlayerId"]?.toLongOrNull() ?: 0L
                        val myId = repository.getUserId()
                        val pendingLayout = MultiplayerFieldMapper.layoutFor(repository.pendingCellCount)
                        _uiState.update {
                            it.copy(
                                isGameFinished = false,
                                winnerId = null,
                                statusMessage = "",
                                myRematchReady = false,
                                opponentRematchReady = false,
                                countdownSeconds = null,
                                myScore = 0,
                                opponentScore = 0,
                                totalGroups = MultiplayerFieldMapper.totalGroups(
                                    repository.pendingCellCount,
                                    repository.pendingMatchSize,
                                ),
                                gridColumns = pendingLayout.columns,
                                gridRows = pendingLayout.rows,
                                currentPlayerId = current,
                                isMyTurn = current == myId,
                                isInputEnabled = current == myId,
                                turnBanner = TurnBannerState(),
                                turnSecondsLeft = null,
                            )
                        }
                        repository.peekGameState()?.let { applyGameState(it) }
                        updateTurnTimer(current)
                    }
                    message.type == "INFO" && message.infoType == InfoTypes.TURN_CHANGED -> {
                        val current = message.data["currentPlayerId"]?.toLongOrNull() ?: 0L
                        applyTurnChange(current)
                    }
                    message.type == "INFO" && message.infoType == InfoTypes.GAME_FINISHED -> {
                        if (_uiState.value.isGameFinished) return@collect
                        stopTurnTimerCountdown()
                        val winner = message.data["winnerId"]?.toLongOrNull()
                        val scores = MultiplayerFieldMapper.parseScores(message.data["scores"].orEmpty())
                        val myId = repository.getUserId()
                        val opponent = scores.keys.firstOrNull { it != myId } ?: opponentId
                        _uiState.update {
                            it.copy(
                                isGameFinished = true,
                                winnerId = winner,
                                isInputEnabled = false,
                                myScore = scores[myId] ?: it.myScore,
                                opponentScore = scores[opponent] ?: it.opponentScore,
                                myRematchReady = false,
                                opponentRematchReady = false,
                                countdownSeconds = null,
                                statusMessage = when (winner) {
                                    myId -> "You win!"
                                    null -> "Draw"
                                    else -> "You lose"
                                },
                                turnBanner = TurnBannerState(),
                            )
                        }
                    }
                    message.type == "INFO" && message.infoType == InfoTypes.READY_STATUS -> {
                        if (!_uiState.value.isGameFinished) applyReadyStatus(message.data)
                    }
                    message.type == "INFO" && (
                        message.infoType == InfoTypes.COUNTDOWN_STARTED ||
                            message.infoType == InfoTypes.COUNTDOWN_TICK
                        ) -> {
                        if (_uiState.value.isGameFinished) return@collect
                        val seconds = message.data["seconds"]?.toIntOrNull()
                        _uiState.update { it.copy(countdownSeconds = seconds) }
                    }
                    message.type == "INFO" && message.infoType == InfoTypes.COUNTDOWN_CANCELLED -> {
                        if (_uiState.value.isGameFinished) return@collect
                        _uiState.update {
                            it.copy(
                                countdownSeconds = null,
                                myRematchReady = false,
                                opponentRematchReady = false,
                            )
                        }
                    }
                    message.type == "INFO" && message.infoType == InfoTypes.PARTNER_LEFT_LOBBY -> {
                        handlePartnerLeftLobby()
                    }
                    message.type == "INFO" && message.infoType == InfoTypes.MATCH_FOUND -> {
                        opponentId = message.data["opponentId"]?.toLongOrNull() ?: 0L
                        val opponentName = message.data["opponentUsername"].orEmpty()
                        _uiState.update { it.copy(opponentName = opponentName) }
                    }
                }
            }
        }
    }

    private fun handlePartnerLeftLobby() {
        repository.setSearchRoom(false)
        repository.clearMatchInfo()
        repository.suppressLobbyAutoNavigate = true
        viewModelScope.launch {
            _events.emit(MultiplayerGameEvent.NavigateToLobby)
        }
    }

    private fun applyReadyStatus(data: Map<String, String>) {
        val myId = repository.getUserId()
        val p1Id = data["player1Id"]?.toLongOrNull() ?: return
        val p2Id = data["player2Id"]?.toLongOrNull()
        val p1Ready = data["player1Ready"]?.toBooleanStrictOrNull() ?: false
        val p2Ready = data["player2Ready"]?.toBooleanStrictOrNull() ?: false
        val myReady = when (myId) {
            p1Id -> p1Ready
            p2Id -> p2Ready
            else -> _uiState.value.myRematchReady
        }
        val oppReady = when (myId) {
            p1Id -> p2Ready
            p2Id -> p1Ready
            else -> _uiState.value.opponentRematchReady
        }
        _uiState.update {
            it.copy(
                myRematchReady = myReady,
                opponentRematchReady = oppReady,
            )
        }
    }

    private fun applyGameState(data: Map<String, String>) {
        val field = data["field"].orEmpty()
        val scores = MultiplayerFieldMapper.parseScores(data["scores"].orEmpty())
        val currentPlayerId = data["currentPlayerId"]?.toLongOrNull() ?: 0L
        val matchSize = data["matchSize"]?.toIntOrNull() ?: MultiplayerFieldMapper.DEFAULT_MATCH_SIZE
        val cellCount = data["cellCount"]?.toIntOrNull() ?: MultiplayerFieldMapper.DEFAULT_CELL_COUNT
        val totalGroups = data["totalGroups"]?.toIntOrNull()
            ?: MultiplayerFieldMapper.totalGroups(cellCount, matchSize)
        val parsed = MultiplayerFieldMapper.parseField(field, cellCount)
        val newCards = parsed.cards
        latestServerCards = newCards
        val myId = repository.getUserId()
        val opponent = scores.keys.firstOrNull { it != myId } ?: opponentId
        val myScore = scores[myId] ?: 0
        val oppScore = scores[opponent] ?: 0

        val previousCards = _uiState.value.cards
        val newlyMatched = newCards.filter { card ->
            card.isMatched && previousCards.find { it.id == card.id }?.isMatched != true
        }.map { it.id }.toSet()

        val baseUpdate = _uiState.value.copy(
            currentPlayerId = currentPlayerId,
            isMyTurn = currentPlayerId == myId,
            isInputEnabled = currentPlayerId == myId && !_uiState.value.isGameFinished,
            myScore = myScore,
            opponentScore = oppScore,
            totalGroups = totalGroups,
            matchSize = matchSize,
            gridColumns = parsed.layout.columns,
            gridRows = parsed.layout.rows,
        )
        updateTurnTimer(currentPlayerId)

        if (newlyMatched.isEmpty()) {
            _uiState.update {
                baseUpdate.copy(cards = cardsForDisplay(newCards))
            }
            return
        }

        animatingMatchedIds.addAll(newlyMatched)
        _uiState.update {
            baseUpdate.copy(cards = cardsForDisplay(newCards))
        }

        matchAnimationJob?.cancel()
        matchAnimationJob = viewModelScope.launch {
            delay(MATCH_ANIMATION_MS)
            animatingMatchedIds.removeAll(newlyMatched)
            _uiState.update {
                it.copy(cards = cardsForDisplay(latestServerCards))
            }
        }
    }

    private fun cardsForDisplay(serverCards: List<GameCard>): List<GameCard> {
        return serverCards.map { card ->
            if (card.id in animatingMatchedIds) {
                card.copy(isMatched = false, isFaceUp = true)
            } else {
                card
            }
        }
    }

    private fun applyTurnChange(currentPlayerId: Long) {
        val myId = repository.getUserId()
        val isMyTurn = currentPlayerId == myId
        val opponentName = _uiState.value.opponentName.ifBlank { "Opponent" }
        val bannerText = if (isMyTurn) {
            getApplication<Application>().getString(R.string.multiplayer_your_turn)
        } else {
            getApplication<Application>().getString(
                R.string.multiplayer_enemy_turn,
                opponentName,
            )
        }
        _uiState.update {
            it.copy(
                currentPlayerId = currentPlayerId,
                isMyTurn = isMyTurn,
                isInputEnabled = isMyTurn && !it.isGameFinished,
                turnBanner = TurnBannerState(text = bannerText, visible = true),
            )
        }
        updateTurnTimer(currentPlayerId)
        viewModelScope.launch {
            delay(TURN_BANNER_MS)
            _uiState.update { state ->
                if (state.turnBanner.text == bannerText) {
                    state.copy(turnBanner = TurnBannerState())
                } else {
                    state
                }
            }
        }
    }

    private fun updateTurnTimer(currentPlayerId: Long) {
        val state = _uiState.value
        if (state.isGameFinished || currentPlayerId == 0L) {
            stopTurnTimerCountdown()
            return
        }
        turnTimerJob?.cancel()
        val trackedPlayerId = currentPlayerId
        turnTimerJob = viewModelScope.launch {
            var seconds = TURN_SECONDS
            while (seconds > 0) {
                val current = _uiState.value
                if (current.isGameFinished || current.currentPlayerId != trackedPlayerId) break
                _uiState.update { it.copy(turnSecondsLeft = seconds) }
                delay(1_000)
                seconds--
            }
            if (_uiState.value.currentPlayerId == trackedPlayerId) {
                _uiState.update { it.copy(turnSecondsLeft = null) }
            }
        }
    }

    private fun stopTurnTimerCountdown() {
        turnTimerJob?.cancel()
        turnTimerJob = null
        _uiState.update { it.copy(turnSecondsLeft = null) }
    }

    fun onCardClick(cardId: Int) {
        if (!_uiState.value.isInputEnabled) return
        viewModelScope.launch { repository.sendTurn(cardId) }
    }

    fun onBackPressed() {
        if (_uiState.value.isGameFinished) return
        _uiState.update { it.copy(showSurrenderDialog = true) }
    }

    fun onDismissSurrenderDialog() {
        _uiState.update { it.copy(showSurrenderDialog = false) }
    }

    fun onConfirmSurrender() {
        if (surrenderSent || _uiState.value.isGameFinished) return
        surrenderSent = true
        _uiState.update { it.copy(showSurrenderDialog = false) }
        viewModelScope.launch {
            runCatching { repository.sendSurrender() }
        }
    }

    fun onAppBackgrounded() {
        if (!_uiState.value.isGameFinished && !surrenderSent) {
            onConfirmSurrender()
        }
    }

    fun onPlayAgain() {
        if (!_uiState.value.isGameFinished || _uiState.value.myRematchReady) return
        _uiState.update { it.copy(myRematchReady = true) }
        viewModelScope.launch {
            runCatching { repository.sendReady() }
        }
    }

    fun onLeaveSearchGame() {
        repository.suppressLobbyAutoNavigate = true
        viewModelScope.launch {
            runCatching { repository.sendUnready() }
            runCatching { repository.leaveLobby(viewModelScope) }
            repository.setSearchRoom(false)
            repository.clearMatchInfo()
            repository.clearGameStateCache()
            _events.emit(MultiplayerGameEvent.NavigateToLobby)
        }
    }

    fun onBackToLobby() {
        repository.suppressLobbyAutoNavigate = true
        viewModelScope.launch {
            runCatching { repository.sendUnready() }
            _uiState.update {
                it.copy(
                    myRematchReady = false,
                    countdownSeconds = null,
                )
            }
            _events.emit(MultiplayerGameEvent.NavigateToLobby)
        }
    }

    class Factory(
        private val application: Application,
        private val repository: MultiplayerRepository,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MultiplayerGameViewModel(application, repository) as T
        }
    }

    companion object {
        private const val MATCH_ANIMATION_MS = 450L
        private const val TURN_BANNER_MS = 1600L
        private const val TURN_SECONDS = 15
    }
}
