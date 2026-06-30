package makza.afonsky.searchpair.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import makza.afonsky.searchpair.ui.theme.HealthBarBackgroundEnd
import makza.afonsky.searchpair.ui.theme.HealthBarBackgroundStart
import makza.afonsky.searchpair.ui.theme.HealthBarFillEnd
import makza.afonsky.searchpair.ui.theme.HealthBarFillStart

@Composable
fun HealthBar(
    health: Int,
    healthMax: Int,
    onCheatTap: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val progress by animateFloatAsState(
        targetValue = if (healthMax > 0) health.toFloat() / healthMax else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "health",
    )

    val shape = RoundedCornerShape(8.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(24.dp)
            .clip(shape)
            .background(
                Brush.horizontalGradient(
                    listOf(HealthBarBackgroundStart, HealthBarBackgroundEnd),
                ),
                shape,
            )
            .clickable(onClick = onCheatTap),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progress.coerceIn(0f, 1f))
                .height(24.dp)
                .clip(shape)
                .background(
                    Brush.horizontalGradient(
                        listOf(HealthBarFillStart, HealthBarFillEnd),
                    ),
                    shape,
                ),
        )
    }
}
