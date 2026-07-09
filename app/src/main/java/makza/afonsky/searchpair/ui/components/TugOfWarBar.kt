package makza.afonsky.searchpair.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import makza.afonsky.searchpair.ui.theme.ButtonBorder
import makza.afonsky.searchpair.ui.theme.ColorGold
import makza.afonsky.searchpair.ui.theme.ColorOrange2
import makza.afonsky.searchpair.ui.theme.ColorRedDark
import makza.afonsky.searchpair.ui.theme.HealthBarBackgroundEnd
import makza.afonsky.searchpair.ui.theme.HealthBarBackgroundStart

private val HudCardShape = RoundedCornerShape(10.dp)

@Composable
fun TugOfWarBar(
    leftName: String,
    rightName: String,
    leftScore: Int,
    rightScore: Int,
    totalGroups: Int = 12,
    turnSecondsLeft: Int? = null,
    timerOnLeft: Boolean = true,
    modifier: Modifier = Modifier,
) {
    val neededToWin = (totalGroups / 2) + 1
    val leadDelta = leftScore - rightScore
    val coinFraction by animateFloatAsState(
        targetValue = (0.5f - leadDelta.toFloat() / (2f * neededToWin)).coerceIn(0f, 1f),
        animationSpec = tween(600),
        label = "coinMove",
    )
    val leftLeading = leftScore > rightScore
    val rightLeading = rightScore > leftScore
    val timerText = turnSecondsLeft?.let { "%02d".format(it) }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.Start,
            ) {
                NicknameChip(name = leftName, alignEnd = false)
            }
            Text(
                text = "VS",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = ColorGold,
                modifier = Modifier.padding(horizontal = 6.dp),
            )
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.End,
            ) {
                NicknameChip(name = rightName, alignEnd = true)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = leftScore.toString(),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = ColorGold,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Start,
            )
            Text(
                text = rightScore.toString(),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = ColorGold,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.End,
            )
        }

        val trackShape = RoundedCornerShape(10.dp)
        val coinSize = 34.dp

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp)
                .height(36.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .align(Alignment.Center)
                    .clip(trackShape)
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                if (leftLeading) ColorGold.copy(alpha = 0.45f) else HealthBarBackgroundStart,
                                ColorOrange2.copy(alpha = 0.35f),
                                if (rightLeading) ColorGold.copy(alpha = 0.45f) else HealthBarBackgroundEnd,
                            ),
                        ),
                        trackShape,
                    ),
            )

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .align(Alignment.Center),
            ) {
                val centerX = size.width / 2f
                drawLine(
                    color = ColorRedDark.copy(alpha = 0.35f),
                    start = Offset(centerX, 0f),
                    end = Offset(centerX, size.height),
                    strokeWidth = 2f,
                )
            }

            val maxOffset = (maxWidth - coinSize).coerceAtLeast(0.dp)
            Box(
                modifier = Modifier
                    .offset(x = maxOffset * coinFraction)
                    .size(coinSize)
                    .align(Alignment.CenterStart),
                contentAlignment = Alignment.Center,
            ) {
                Canvas(modifier = Modifier.size(coinSize)) {
                    val radius = size.minDimension / 2f
                    val center = Offset(size.width / 2f, size.height / 2f)
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xFFFFF8B0),
                                ColorGold,
                                Color(0xFFC98A00),
                            ),
                            center = center,
                            radius = radius,
                        ),
                        radius = radius,
                        center = center,
                    )
                    drawCircle(
                        color = ButtonBorder,
                        radius = radius,
                        center = center,
                        style = Stroke(width = 2.5f),
                    )
                    drawCircle(
                        color = Color(0xFFFFF8B0).copy(alpha = 0.55f),
                        radius = radius * 0.28f,
                        center = Offset(center.x - radius * 0.22f, center.y - radius * 0.22f),
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
                .height(24.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.CenterStart,
            ) {
                if (timerOnLeft && timerText != null) {
                    Text(
                        text = timerText,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorGold,
                    )
                }
            }
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.CenterEnd,
            ) {
                if (!timerOnLeft && timerText != null) {
                    Text(
                        text = timerText,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorGold,
                        textAlign = TextAlign.End,
                    )
                }
            }
        }
    }
}

@Composable
private fun NicknameChip(
    name: String,
    alignEnd: Boolean,
) {
    Text(
        text = name,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        color = ColorGold,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        textAlign = if (alignEnd) TextAlign.End else TextAlign.Start,
        modifier = Modifier
            .widthIn(max = 160.dp)
            .background(ColorOrange2.copy(alpha = 0.35f), HudCardShape)
            .border(1.dp, ColorRedDark.copy(alpha = 0.5f), HudCardShape)
            .padding(horizontal = 10.dp, vertical = 6.dp),
    )
}
