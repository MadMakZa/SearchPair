package makza.afonsky.searchpair.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = ColorPrimary,
    onPrimary = ColorRedDark,
    primaryContainer = ColorOrange,
    secondary = ColorGold,
    onSecondary = ColorRedDark,
    background = ColorYellow,
    onBackground = ColorRedDark,
    surface = ColorOrange2,
    onSurface = ColorRedDark,
)

@Composable
fun FindAPairTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) LightColorScheme else LightColorScheme,
        typography = AppTypography,
        content = content,
    )
}
