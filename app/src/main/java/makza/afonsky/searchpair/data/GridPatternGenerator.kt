package makza.afonsky.searchpair.data

import kotlin.math.abs

data class GridCell(val row: Int, val col: Int)

data class GridLayout(
    val columns: Int,
    val rows: Int,
    val positions: List<GridCell>,
    val snakeOrder: List<Int>,
)

object GridPatternGenerator {

    const val MAX_COLUMNS = 8
    const val MAX_ROWS = 8
    val maxCardSlots: Int get() = MAX_COLUMNS * MAX_ROWS

    fun generate(totalCards: Int): GridLayout {
        require(totalCards in 1..maxCardSlots) {
            "Card count $totalCards exceeds grid capacity $maxCardSlots"
        }

        val centerCol = (MAX_COLUMNS - 1) / 2.0
        val centerRow = (MAX_ROWS - 1) / 2.0

        val allCells = buildList {
            for (row in 0 until MAX_ROWS) {
                for (col in 0 until MAX_COLUMNS) {
                    add(GridCell(row, col))
                }
            }
        }

        val symmetric = allCells
            .sortedWith(
                compareBy<GridCell>(
                    { abs(it.col - centerCol) + abs(it.row - centerRow) },
                    { it.row },
                    { abs(it.col - centerCol) },
                ),
            )
            .take(totalCards)

        check(symmetric.size == totalCards) {
            "Grid layout failed for $totalCards cards"
        }

        return GridLayout(
            columns = MAX_COLUMNS,
            rows = MAX_ROWS,
            positions = symmetric,
            snakeOrder = buildSnakeOrder(symmetric),
        )
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
