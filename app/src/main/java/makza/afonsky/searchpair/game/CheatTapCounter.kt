package makza.afonsky.searchpair.game

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CheatTapCounter(
    private val scope: CoroutineScope,
    private val requiredTaps: Int,
    private val resetTimeoutMs: Long = CHEAT_RESET_TIMEOUT_MS,
    private val onTriggered: () -> Unit,
) {
    private var taps = 0
    private var resetJob: Job? = null

    fun onSecretTap() {
        resetJob?.cancel()
        taps++
        if (taps >= requiredTaps) {
            taps = 0
            onTriggered()
        } else {
            resetJob = scope.launch {
                delay(resetTimeoutMs)
                taps = 0
            }
        }
    }

    fun reset() {
        resetJob?.cancel()
        taps = 0
    }

    companion object {
        const val CHEAT_RESET_TIMEOUT_MS = 3_000L
    }
}
