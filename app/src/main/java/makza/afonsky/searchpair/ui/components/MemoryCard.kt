package makza.afonsky.searchpair.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import makza.afonsky.searchpair.data.CardAssets
import makza.afonsky.searchpair.data.CardBackStyle
import makza.afonsky.searchpair.game.GameCard

@Composable
fun MemoryCard(
    card: GameCard,
    cardBack: CardBackStyle,
    isFlipping: Boolean,
    flipDurationMs: Int = 300,
    enabled: Boolean = true,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val rotation by animateFloatAsState(
        targetValue = if (card.isFaceUp) 180f else 0f,
        animationSpec = tween(flipDurationMs),
        label = "cardFlip",
    )

    val showFace = rotation > 90f
    val clickable = enabled && !card.isFaceUp && !isFlipping

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .padding(1.dp)
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 12f * density
            }
            .clip(RoundedCornerShape(8.dp))
            .clickable(enabled = clickable, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        val drawable = if (showFace) {
            CardAssets.faceDrawable(card.symbol)
        } else {
            cardBack.drawableRes
        }

        Image(
            painter = painterResource(drawable),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit,
        )
    }
}

@Composable
fun HealthKitIcon(
    drawableRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: androidx.compose.ui.unit.Dp = 40.dp,
) {
    Image(
        painter = painterResource(drawableRes),
        contentDescription = null,
        modifier = modifier
            .size(size)
            .clickable(onClick = onClick),
        contentScale = ContentScale.Fit,
    )
}
