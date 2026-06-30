package makza.afonsky.searchpair.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import makza.afonsky.searchpair.data.GridPatternGenerator
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
    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        val spacing = 3.dp
        val gridCols = config.columns
        val gridRows = config.rows

        val horizontalGaps = spacing * (gridCols - 1).coerceAtLeast(0)
        val verticalGaps = spacing * (gridRows - 1).coerceAtLeast(0)

        val cellFromWidth = (maxWidth - horizontalGaps) / gridCols
        val cellFromHeight = (maxHeight - verticalGaps) / gridRows
        val cellSize = minOf(cellFromWidth, cellFromHeight)

        val gridWidth = cellSize * gridCols + horizontalGaps
        val gridHeight = cellSize * gridRows + verticalGaps

        Column(
            modifier = Modifier.size(width = gridWidth, height = gridHeight),
            verticalArrangement = Arrangement.spacedBy(spacing),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            for (row in 0 until gridRows) {
                Row(horizontalArrangement = Arrangement.spacedBy(spacing)) {
                    for (col in 0 until gridCols) {
                        val card = cards.find {
                            it.gridRow == row && it.gridCol == col && !it.isMatched
                        }
                        if (card != null) {
                            MemoryCard(
                                card = card,
                                cardBack = config.cardBack,
                                isFlipping = flippingCardId == card.id,
                                flipDurationMs = flipDurationMs,
                                enabled = enabled,
                                onClick = { onCardClick(card.id) },
                                modifier = Modifier.size(cellSize),
                            )
                        } else {
                            Spacer(modifier = Modifier.size(cellSize))
                        }
                    }
                }
            }
        }
    }
}
