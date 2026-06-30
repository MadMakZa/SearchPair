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

    private var logoCheatTaps = 0
    private var levelLabelCheatTaps = 0

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
        repository.saveSelectedDifficultyPage(pageIndex)
        _uiState.update { it.copy(selectedDifficultyPage = pageIndex) }
    }

    fun onNewGameClick() {
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
        soundManager.play(GameSound.DROP)
        _uiState.update { it.copy(showResetDialog = false) }
    }

    fun onOpenChest() {
        soundManager.play(GameSound.CHEST)
        refresh()
        _uiState.update { it.copy(showChestDialog = true) }
    }

    fun onDismissChest() {
        soundManager.play(GameSound.DROP)
        _uiState.update { it.copy(showChestDialog = false) }
    }

    fun onBackRequest() {
        soundManager.play(GameSound.DROP)
        _uiState.update { it.copy(showExitDialog = true) }
    }

    fun onDismissExit() {
        soundManager.play(GameSound.DROP)
        _uiState.update { it.copy(showExitDialog = false) }
    }

    fun onLevelClick(level: Int) {
        soundManager.play(GameSound.DROP)
        val page = DifficultyPage.fromGlobalLevel(level).index
        repository.saveSelectedDifficultyPage(page)
        _uiState.update { it.copy(selectedDifficultyPage = page) }
    }

    fun onLogoCheatTap() {
        logoCheatTaps++
        if (logoCheatTaps >= 35) {
            soundManager.play(GameSound.CLOSE)
            repository.setMaxKits()
            refresh()
            logoCheatTaps = 0
        }
    }

    fun onLevelLabelCheatTap() {
        levelLabelCheatTaps++
        if (levelLabelCheatTaps >= 30) {
            soundManager.play(GameSound.CLOSE)
            repository.unlockAllLevels()
            refresh()
            levelLabelCheatTaps = 0
        }
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
