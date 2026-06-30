package makza.afonsky.searchpair.ui.bonus

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import makza.afonsky.searchpair.R
import makza.afonsky.searchpair.audio.GameSound
import makza.afonsky.searchpair.audio.SoundManager
import makza.afonsky.searchpair.data.CardAssets
import kotlin.random.Random

@Composable
fun BonusScreen(
    soundManager: SoundManager,
    onNavigateToMenu: () -> Unit,
) {
    BackHandler {
        soundManager.play(GameSound.DROP)
        onNavigateToMenu()
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val maxWidthPx = constraints.maxWidth
        val maxHeightPx = constraints.maxHeight
        val density = LocalDensity.current

        Image(
            painter = painterResource(R.drawable.congratulations_dialog),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit,
        )

        repeat(12) { streamIndex ->
            val offsetY = remember { Animatable(0f) }

            LaunchedEffect(streamIndex) {
                delay(streamIndex * 80L)
                while (isActive) {
                    offsetY.snapTo(0f)
                    offsetY.animateTo(
                        targetValue = maxHeightPx.toFloat(),
                        animationSpec = tween(1200),
                    )
                    delay(150)
                }
            }

            Image(
                painter = painterResource(CardAssets.faceDrawable(Random.nextInt(1, CardAssets.maxSymbol + 1))),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .offset {
                        IntOffset(
                            x = (streamIndex * maxWidthPx / 12).coerceAtMost(maxWidthPx - 100),
                            y = offsetY.value.toInt(),
                        )
                    },
                contentScale = ContentScale.Fit,
            )
        }
    }
}
