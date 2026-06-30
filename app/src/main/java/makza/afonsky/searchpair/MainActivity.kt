package makza.afonsky.searchpair

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.compose.rememberNavController
import makza.afonsky.searchpair.audio.SoundManager
import makza.afonsky.searchpair.navigation.FindAPairNavHost
import makza.afonsky.searchpair.ui.theme.FindAPairTheme

class MainActivity : ComponentActivity() {

    private lateinit var soundManager: SoundManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        hideSystemBars()

        soundManager = SoundManager(this)

        setContent {
            FindAPairTheme {
                val navController = rememberNavController()
                FindAPairNavHost(
                    navController = navController,
                    soundManager = soundManager,
                    onExitApp = { finish() },
                )
            }
        }
    }

    private fun hideSystemBars() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).apply {
            hide(WindowInsetsCompat.Type.systemBars())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    override fun onDestroy() {
        if (::soundManager.isInitialized) {
            soundManager.release()
        }
        super.onDestroy()
    }
}
