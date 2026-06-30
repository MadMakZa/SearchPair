package makza.afonsky.searchpair.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
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
import androidx.compose.ui.unit.dp
import makza.afonsky.searchpair.ui.theme.ButtonBorder
import makza.afonsky.searchpair.ui.theme.ButtonGradientEnd
import makza.afonsky.searchpair.ui.theme.ButtonGradientStart
import makza.afonsky.searchpair.ui.theme.ColorRedDark

@Composable
fun GameButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val shape = RoundedCornerShape(12.dp)
    val gradient = Brush.verticalGradient(
        colors = listOf(ButtonGradientStart, ButtonGradientEnd),
    )

    Box(
        modifier = modifier
            .defaultMinSize(minWidth = 80.dp, minHeight = 44.dp)
            .clip(shape)
            .background(gradient, shape)
            .border(2.dp, ButtonBorder, shape)
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
            style = androidx.compose.material3.MaterialTheme.typography.labelLarge,
            color = ColorRedDark,
            textAlign = TextAlign.Center,
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
    GameButton(
        text = if (unlocked) text else "X",
        onClick = onClick,
        enabled = unlocked,
        modifier = modifier,
    )
}
