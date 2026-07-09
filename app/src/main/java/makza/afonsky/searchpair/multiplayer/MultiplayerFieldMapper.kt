package makza.afonsky.searchpair.multiplayer

import makza.afonsky.searchpair.data.GridCell
import makza.afonsky.searchpair.data.GridLayout
import makza.afonsky.searchpair.data.GridPatternGenerator
import makza.afonsky.searchpair.game.GameCard

object MultiplayerFieldMapper {

    const val DEFAULT_COLUMNS = 6
    const val DEFAULT_ROWS = 4
    const val DEFAULT_CELL_COUNT = 24
    const val SEARCH_DEFAULT_CELL_COUNT = 30
    const val DEFAULT_MATCH_SIZE = 2

    data class ParsedField(
        val cards: List<GameCard>,
        val layout: GridLayout,
    )

    fun layoutFor(cellCount: Int): GridLayout =
        GridPatternGenerator.generate(cellCount, GridPatternGenerator.MULTIPLAYER_MAX_COLUMNS)

    fun totalGroups(cellCount: Int, matchSize: Int): Int =
        (cellCount / matchSize.coerceAtLeast(2)).coerceAtLeast(1)

    fun parseField(field: String, cellCount: Int = DEFAULT_CELL_COUNT): ParsedField {
        val layout = layoutFor(cellCount)
        if (field.isBlank()) {
            return ParsedField(emptyList(), layout)
        }
        val cards = field.split(",").mapNotNull { token ->
            val parts = token.split(":")
            if (parts.size < 3) return@mapNotNull null
            val id = parts[0].toIntOrNull() ?: return@mapNotNull null
            val image = parts[1].toIntOrNull() ?: return@mapNotNull null
            val state = parts[2].toIntOrNull() ?: 0
            val cell = layout.positions.getOrNull(id) ?: GridCell(id / layout.columns, id % layout.columns)
            GameCard(
                id = id,
                symbol = image,
                gridRow = cell.row,
                gridCol = cell.col,
                isFaceUp = state >= 1,
                isMatched = state >= 2,
            )
        }
        return ParsedField(cards, layout)
    }

    fun parseScores(scores: String): Map<Long, Int> {
        if (scores.isBlank()) return emptyMap()
        return scores.split(",").mapNotNull { part ->
            val kv = part.split(":")
            if (kv.size != 2) return@mapNotNull null
            val id = kv[0].toLongOrNull() ?: return@mapNotNull null
            val score = kv[1].toIntOrNull() ?: 0
            id to score
        }.toMap()
    }
}
