package makza.afonsky.searchpair.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import makza.afonsky.searchpair.ui.theme.ButtonBorder
import makza.afonsky.searchpair.ui.theme.ButtonGradientEnd
import makza.afonsky.searchpair.ui.theme.ButtonGradientStart
import makza.afonsky.searchpair.ui.theme.ColorRedDark

private val ButtonShape = RoundedCornerShape(12.dp)
private val ButtonGradient = Brush.verticalGradient(
    colors = listOf(ButtonGradientStart, ButtonGradientEnd),
)

@Composable
fun GameButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Box(
        modifier = modifier
            .heightIn(min = 48.dp)
            .clip(ButtonShape)
            .background(ButtonGradient, ButtonShape)
            .border(2.dp, ButtonBorder, ButtonShape)
            .clickable(
                enabled = enabled,
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            )
            .padding(horizontal = 16.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = androidx.compose.material3.MaterialTheme.typography.labelLarge.copy(
                fontSize = 28.sp,
            ),
            color = ColorRedDark,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
fun LevelButton(
    text: String,
    unlocked: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(
        modifier = modifier
            .clip(ButtonShape)
            .background(ButtonGradient, ButtonShape)
            .border(2.dp, ButtonBorder, ButtonShape)
            .clickable(
                enabled = unlocked,
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        val fontSize = (minOf(maxWidth, maxHeight).value * 0.42f)
            .coerceIn(14f, 28f)
            .sp
        Text(
            text = if (unlocked) text else "X",
            style = androidx.compose.material3.MaterialTheme.typography.labelLarge.copy(
                fontSize = fontSize,
            ),
            color = ColorRedDark,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Clip,
        )
    }
}
