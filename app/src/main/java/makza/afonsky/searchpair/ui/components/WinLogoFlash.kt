package makza.afonsky.searchpair.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import makza.afonsky.searchpair.R

private const val FLASH_DURATION_MS = 1800
private const val START_SCALE = 0.15f
private const val END_SCALE = 7f

@Composable
fun WinLogoFlash(
    modifier: Modifier = Modifier,
    onFinished: () -> Unit = {},
) {
    val scale = remember { Animatable(START_SCALE) }
    val alpha = remember { Animatable(1f) }

    LaunchedEffect(Unit) {
        coroutineScope {
            launch {
                scale.animateTo(
                    targetValue = END_SCALE,
                    animationSpec = tween(FLASH_DURATION_MS, easing = FastOutSlowInEasing),
                )
            }
            launch {
                alpha.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(FLASH_DURATION_MS, easing = FastOutSlowInEasing),
                )
            }
        }
        onFinished()
    }

    Image(
        painter = painterResource(R.drawable.splashscreenlogo),
        contentDescription = null,
        modifier = modifier.graphicsLayer {
            scaleX = scale.value
            scaleY = scale.value
            this.alpha = alpha.value
        },
        contentScale = ContentScale.Fit,
    )
}
