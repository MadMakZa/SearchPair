package makza.afonsky.searchpair.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import makza.afonsky.searchpair.R

val CaesarDressing = FontFamily(Font(R.font.caesar_dressing))

val AppTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = CaesarDressing,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp,
        color = ColorRedDark,
    ),
    headlineMedium = TextStyle(
        fontFamily = CaesarDressing,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        color = ColorRedDark,
    ),
    titleLarge = TextStyle(
        fontFamily = CaesarDressing,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        color = ColorRedDark,
    ),
    titleMedium = TextStyle(
        fontFamily = CaesarDressing,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        color = ColorRedDark,
    ),
    bodyLarge = TextStyle(
        fontFamily = CaesarDressing,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        color = ColorRedDark,
    ),
    labelLarge = TextStyle(
        fontFamily = CaesarDressing,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = ColorRedDark,
    ),
)
