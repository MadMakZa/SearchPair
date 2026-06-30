package makza.afonsky.searchpair.data

import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.roundToInt

data class GridCell(val row: Int, val col: Int)

data class GridLayout(
    val columns: Int,
    val rows: Int,
    val positions: List<GridCell>,
    val snakeOrder: List<Int>,
)

object GridPatternGenerator {

    const val MIN_COLUMNS = 5
    const val MAX_COLUMNS = 7
    const val MAX_ROWS = 14

    val maxCardSlots: Int get() = MAX_COLUMNS * MAX_ROWS

    fun generate(totalCards: Int): GridLayout {
        require(totalCards in 1..maxCardSlots) {
            "Card count $totalCards exceeds grid capacity $maxCardSlots"
        }

        val columns = columnsForCardCount(totalCards)
        var rows = ceil(totalCards / columns.toDouble()).toInt().coerceAtMost(MAX_ROWS)
        require(columns * rows >= totalCards) {
            "Grid capacity ${columns * rows} too small for $totalCards cards"
        }

        val centerCol = (columns - 1) / 2.0
        val centerRow = (rows - 1) / 2.0

        val allCells = buildList {
            for (row in 0 until rows) {
                for (col in 0 until columns) {
                    add(GridCell(row, col))
                }
            }
        }

        val positions = allCells
            .sortedWith(
                compareBy<GridCell>(
                    { abs(it.col - centerCol) + abs(it.row - centerRow) },
                    { it.row },
                    { it.col },
                ),
            )
            .take(totalCards)

        check(positions.size == totalCards) {
            "Grid layout failed for $totalCards cards"
        }

        return GridLayout(
            columns = columns,
            rows = rows,
            positions = positions,
            snakeOrder = buildSnakeOrder(positions),
        )
    }

    /** More cards → wider grid (5 … 7 columns). */
    private fun columnsForCardCount(totalCards: Int): Int {
        val minCards = 4
        val maxCards = 80
        val progress = ((totalCards - minCards).toFloat() / (maxCards - minCards)).coerceIn(0f, 1f)
        return (MIN_COLUMNS + progress * (MAX_COLUMNS - MIN_COLUMNS))
            .roundToInt()
            .coerceIn(MIN_COLUMNS, MAX_COLUMNS)
    }

    private fun buildSnakeOrder(positions: List<GridCell>): List<Int> {
        val indexByCell = positions.withIndex().associate { (index, cell) -> cell to index }
        val grouped = positions.groupBy { it.row }.toSortedMap()

        return buildList {
            grouped.forEach { (row, cells) ->
                val sorted = if (row % 2 == 0) {
                    cells.sortedBy { it.col }
                } else {
                    cells.sortedByDescending { it.col }
                }
                sorted.forEach { cell ->
                    indexByCell[cell]?.let(::add)
                }
            }
        }
    }
}
