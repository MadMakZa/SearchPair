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
    val snakeOrderSecondary: List<Int>? = null,
)

object GridPatternGenerator {

    const val MIN_COLUMNS = 5
    const val MAX_COLUMNS = 7
    const val MULTIPLAYER_MAX_COLUMNS = 8
    const val MAX_ROWS = 14
    private const val DUAL_SNAKE_THRESHOLD = 30

    val maxCardSlots: Int get() = MAX_COLUMNS * MAX_ROWS

    fun generate(totalCards: Int, maxColumns: Int = MAX_COLUMNS): GridLayout {
        require(totalCards in 1..maxCardSlots) {
            "Card count $totalCards exceeds grid capacity $maxCardSlots"
        }

        val columns = columnsForCardCount(totalCards, maxColumns)
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

        val (snakeOrder, snakeOrderSecondary) = if (totalCards > DUAL_SNAKE_THRESHOLD) {
            buildDualSnakeOrders(positions, columns, rows)
        } else {
            buildSnakeOrder(positions) to null
        }

        return GridLayout(
            columns = columns,
            rows = rows,
            positions = positions,
            snakeOrder = snakeOrder,
            snakeOrderSecondary = snakeOrderSecondary,
        )
    }

    /** More cards → wider grid (min … maxColumns). */
    private fun columnsForCardCount(totalCards: Int, maxColumns: Int = MAX_COLUMNS): Int {
        val minCards = 4
        val maxCards = 80
        val progress = ((totalCards - minCards).toFloat() / (maxCards - minCards)).coerceIn(0f, 1f)
        return (MIN_COLUMNS + progress * (maxColumns - MIN_COLUMNS))
            .roundToInt()
            .coerceIn(MIN_COLUMNS.coerceAtMost(maxColumns), maxColumns)
    }

    private fun buildDualSnakeOrders(
        positions: List<GridCell>,
        columns: Int,
        rows: Int,
    ): Pair<List<Int>, List<Int>> {
        val centerCol = (columns - 1) / 2.0
        val centerRow = (rows - 1) / 2.0
        val maxRow = positions.maxOf { it.row }
        val maxCol = positions.maxOf { it.col }

        val topLeftGroup = mutableListOf<Pair<Int, GridCell>>()
        val bottomRightGroup = mutableListOf<Pair<Int, GridCell>>()

        positions.forEachIndexed { index, cell ->
            val toTopLeft = cell.row + cell.col
            val toBottomRight = (maxRow - cell.row) + (maxCol - cell.col)
            if (toTopLeft <= toBottomRight) {
                topLeftGroup.add(index to cell)
            } else {
                bottomRightGroup.add(index to cell)
            }
        }

        val towardTopLeft = sortRadialFromCenter(
            items = topLeftGroup,
            centerRow = centerRow,
            centerCol = centerCol,
            cornerKey = { it.row + it.col },
        )
        val towardBottomRight = sortRadialFromCenter(
            items = bottomRightGroup,
            centerRow = centerRow,
            centerCol = centerCol,
            cornerKey = { (maxRow - it.row) + (maxCol - it.col) },
        )
        return towardTopLeft to towardBottomRight
    }

    private fun sortRadialFromCenter(
        items: List<Pair<Int, GridCell>>,
        centerRow: Double,
        centerCol: Double,
        cornerKey: (GridCell) -> Int,
    ): List<Int> {
        return items
            .sortedWith(
                compareBy(
                    { abs(it.second.col - centerCol) + abs(it.second.row - centerRow) },
                    { cornerKey(it.second) },
                ),
            )
            .map { it.first }
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
