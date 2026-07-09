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
import makza.afonsky.searchpair.data.CardBackStyle
import makza.afonsky.searchpair.game.GameCard

@Composable
fun MultiplayerCardGrid(
    cards: List<GameCard>,
    gridColumns: Int,
    gridRows: Int,
    enabled: Boolean,
    onCardClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val cardBack = CardBackStyle.GREEN

    BoxWithConstraints(modifier = modifier, contentAlignment = Alignment.Center) {
        val spacing = 3.dp
        val horizontalGaps = spacing * (gridColumns - 1).coerceAtLeast(0)
        val verticalGaps = spacing * (gridRows - 1).coerceAtLeast(0)
        val cellFromWidth = (maxWidth - horizontalGaps) / gridColumns
        val cellFromHeight = (maxHeight - verticalGaps) / gridRows
        val cellSize = minOf(cellFromWidth, cellFromHeight)
        val gridWidth = cellSize * gridColumns + horizontalGaps
        val gridHeight = cellSize * gridRows + verticalGaps

        Column(
            modifier = Modifier.size(width = gridWidth, height = gridHeight),
            verticalArrangement = Arrangement.spacedBy(spacing),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            for (row in 0 until gridRows) {
                Row(horizontalArrangement = Arrangement.spacedBy(spacing)) {
                    for (col in 0 until gridColumns) {
                        val card = cards.find { it.gridRow == row && it.gridCol == col && !it.isMatched }
                        if (card != null) {
                            MemoryCard(
                                card = card,
                                cardBack = cardBack,
                                isFlipping = false,
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
