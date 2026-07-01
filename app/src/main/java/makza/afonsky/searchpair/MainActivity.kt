package makza.afonsky.searchpair

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.LocalIndication
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.view.WindowCompat
import makza.afonsky.searchpair.ui.theme.NoPressIndication
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
            @OptIn(ExperimentalMaterial3Api::class)
            CompositionLocalProvider(
                LocalIndication provides NoPressIndication,
                LocalRippleConfiguration provides null,
            ) {
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
