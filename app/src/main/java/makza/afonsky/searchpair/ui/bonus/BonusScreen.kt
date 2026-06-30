package makza.afonsky.searchpair.ui.bonus

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import makza.afonsky.searchpair.R
import makza.afonsky.searchpair.data.DifficultyBackground
import makza.afonsky.searchpair.data.DifficultyPage
import makza.afonsky.searchpair.audio.GameSound
import makza.afonsky.searchpair.audio.SoundManager
import makza.afonsky.searchpair.data.CardAssets
import kotlin.math.roundToInt
import kotlin.random.Random

private const val PARTICLE_COUNT = 28

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
        val iconSizePx = with(LocalDensity.current) { 44.dp.toPx() }

        Image(
            painter = painterResource(DifficultyBackground.forPage(DifficultyPage.QUARTETS)),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )

        repeat(PARTICLE_COUNT) { index ->
            FallingParticle(
                index = index,
                maxWidthPx = maxWidthPx,
                maxHeightPx = maxHeightPx,
                iconSizePx = iconSizePx,
            )
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(R.drawable.congratulations_dialog),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        scaleX = 2.5f
                        scaleY = 2.5f
                    },
                contentScale = ContentScale.Fit,
            )
        }
    }
}

@Composable
private fun FallingParticle(
    index: Int,
    maxWidthPx: Int,
    maxHeightPx: Int,
    iconSizePx: Float,
) {
    val random = remember(index) { Random(index * 31 + 7) }
    val symbol = remember(index) { random.nextInt(1, CardAssets.maxSymbol + 1) }
    val driftRight = index % 2 == 0
    val startX = remember(index, maxWidthPx) {
        if (driftRight) {
            random.nextInt(0, (maxWidthPx * 0.45f).roundToInt().coerceAtLeast(1))
        } else {
            random.nextInt((maxWidthPx * 0.55f).roundToInt(), maxWidthPx.coerceAtLeast(1))
        }
    }
    val driftPx = remember(index, maxWidthPx) {
        random.nextInt((maxWidthPx * 0.12f).roundToInt().coerceAtLeast(40), (maxWidthPx * 0.28f).roundToInt().coerceAtLeast(80))
    }
    val durationMs = remember(index) { random.nextInt(900, 1500) }

    val offsetY = remember { Animatable(-iconSizePx) }
    val offsetX = remember { Animatable(startX.toFloat()) }

    LaunchedEffect(index, maxWidthPx, maxHeightPx) {
        delay(index * 70L)
        while (isActive) {
            val targetX = if (driftRight) {
                (startX + driftPx).coerceAtMost((maxWidthPx - iconSizePx).toInt())
            } else {
                (startX - driftPx).coerceAtLeast(0)
            }.toFloat()

            coroutineScope {
                val fall = async {
                    offsetY.snapTo(-iconSizePx)
                    offsetY.animateTo(
                        targetValue = maxHeightPx + iconSizePx,
                        animationSpec = tween(durationMs, easing = LinearEasing),
                    )
                }
                val drift = async {
                    offsetX.snapTo(startX.toFloat())
                    offsetX.animateTo(
                        targetValue = targetX,
                        animationSpec = tween(durationMs, easing = LinearEasing),
                    )
                }
                fall.await()
                drift.await()
            }
            delay(random.nextLong(80, 220))
        }
    }

    Image(
        painter = painterResource(CardAssets.faceDrawable(symbol)),
        contentDescription = null,
        modifier = Modifier
            .size(44.dp)
            .offset {
                IntOffset(
                    x = offsetX.value.roundToInt(),
                    y = offsetY.value.roundToInt(),
                )
            },
        contentScale = ContentScale.Fit,
    )
}
