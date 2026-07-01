package makza.afonsky.searchpair.ui.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import makza.afonsky.searchpair.audio.GameSound
import makza.afonsky.searchpair.audio.SoundManager
import makza.afonsky.searchpair.data.DifficultyPage
import makza.afonsky.searchpair.data.GameRepository
import makza.afonsky.searchpair.game.CheatTapCounter

data class MenuUiState(
    val unlockedLevel: Int = 1,
    val selectedDifficultyPage: Int = 0,
    val unlockedPages: Set<Int> = setOf(0),
    val kitCounts: GameRepository.KitCounts = GameRepository.KitCounts(0, 0, 0),
    val showResetDialog: Boolean = false,
    val showExitDialog: Boolean = false,
    val showChestDialog: Boolean = false,
)

class MenuViewModel(
    private val repository: GameRepository,
    private val soundManager: SoundManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MenuUiState())
    val uiState: StateFlow<MenuUiState> = _uiState.asStateFlow()

    private val logoCheat = CheatTapCounter(viewModelScope, requiredTaps = 35) {
        soundManager.play(GameSound.CLOSE)
        repository.setMaxKits()
        refresh()
    }

    private val levelLabelCheat = CheatTapCounter(viewModelScope, requiredTaps = 30) {
        soundManager.play(GameSound.CLOSE)
        repository.unlockAllLevels()
        refresh()
    }

    init {
        refresh()
    }

    fun refresh() {
        val unlocked = repository.getUnlockedLevel()
        val selectedPage = repository.getSelectedDifficultyPage()
        val pages = DifficultyPage.entries
            .filter { DifficultyPage.isPageUnlocked(it, unlocked) }
            .map { it.index }
            .toSet()
        val safePage = if (pages.contains(selectedPage)) {
            selectedPage
        } else {
            pages.maxOrNull() ?: 0
        }
        _uiState.update {
            it.copy(
                unlockedLevel = unlocked,
                selectedDifficultyPage = safePage,
                unlockedPages = pages,
                kitCounts = repository.getAllKitCounts(),
            )
        }
    }

    fun onDifficultyPageChanged(pageIndex: Int) {
        resetAllCheats()
        repository.saveSelectedDifficultyPage(pageIndex)
        _uiState.update { it.copy(selectedDifficultyPage = pageIndex) }
    }

    fun onNewGameClick() {
        resetAllCheats()
        soundManager.play(GameSound.DROP)
        if (repository.getUnlockedLevel() >= 2) {
            _uiState.update { it.copy(showResetDialog = true) }
        }
    }

    fun onConfirmReset() {
        repository.resetProgress()
        refresh()
        _uiState.update { it.copy(showResetDialog = false) }
    }

    fun onDismissReset() {
        resetAllCheats()
        soundManager.play(GameSound.DROP)
        _uiState.update { it.copy(showResetDialog = false) }
    }

    fun onOpenChest() {
        resetAllCheats()
        soundManager.play(GameSound.CHEST)
        refresh()
        _uiState.update { it.copy(showChestDialog = true) }
    }

    fun onDismissChest() {
        resetAllCheats()
        soundManager.play(GameSound.DROP)
        _uiState.update { it.copy(showChestDialog = false) }
    }

    fun onBackRequest() {
        resetAllCheats()
        soundManager.play(GameSound.DROP)
        _uiState.update { it.copy(showExitDialog = true) }
    }

    fun onDismissExit() {
        resetAllCheats()
        soundManager.play(GameSound.DROP)
        _uiState.update { it.copy(showExitDialog = false) }
    }

    fun onLevelClick(level: Int) {
        resetAllCheats()
        soundManager.play(GameSound.DROP)
        val page = DifficultyPage.fromGlobalLevel(level).index
        repository.saveSelectedDifficultyPage(page)
        _uiState.update { it.copy(selectedDifficultyPage = page) }
    }

    fun onPagerDotClick() {
        resetAllCheats()
    }

    fun onOtherMenuTap() {
        resetAllCheats()
    }

    fun onLogoCheatTap() {
        levelLabelCheat.reset()
        logoCheat.onSecretTap()
    }

    fun onLevelLabelCheatTap() {
        logoCheat.reset()
        levelLabelCheat.onSecretTap()
    }

    private fun resetAllCheats() {
        logoCheat.reset()
        levelLabelCheat.reset()
    }

    class Factory(
        private val repository: GameRepository,
        private val soundManager: SoundManager,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MenuViewModel(repository, soundManager) as T
        }
    }
}
