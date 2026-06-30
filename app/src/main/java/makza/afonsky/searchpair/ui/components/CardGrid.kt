package makza.afonsky.searchpair.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import makza.afonsky.searchpair.data.LevelConfig
import makza.afonsky.searchpair.game.GameCard

@Composable
fun CardGrid(
    config: LevelConfig,
    cards: List<GameCard>,
    flippingCardId: Int?,
    flipDurationMs: Int,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onCardClick: (Int) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        for (row in 0 until config.rows) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                for (col in 0 until config.columns) {
                    val card = cards.find { it.gridRow == row && it.gridCol == col && !it.isMatched }
                    if (card != null) {
                        MemoryCard(
                            card = card,
                            cardBack = config.cardBack,
                            isFlipping = flippingCardId == card.id,
                            flipDurationMs = flipDurationMs,
                            enabled = enabled,
                            onClick = { onCardClick(card.id) },
                            modifier = Modifier.weight(1f),
                        )
                    } else {
                        Spacer(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f),
                        )
                    }
                }
            }
        }
    }
}
