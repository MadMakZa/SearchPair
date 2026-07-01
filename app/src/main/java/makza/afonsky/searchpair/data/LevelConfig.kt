package makza.afonsky.searchpair.data

import makza.afonsky.searchpair.R

enum class HealthKitTier {
    SMALL, MEDIUM, BIG
}

enum class CardBackStyle {
    GREEN, BLUE, PURPLE, RED;

    val drawableRes: Int
        get() = when (this) {
            GREEN -> R.drawable.shirtgreen
            BLUE -> R.drawable.shirtblue
            PURPLE -> R.drawable.shirtpurple
            RED -> R.drawable.shirtred
        }

    companion object {
        fun forLevel(level: Int): CardBackStyle = when (level % 4) {
            0 -> RED
            1 -> GREEN
            2 -> BLUE
            else -> PURPLE
        }
    }
}

data class LevelConfig(
    val level: Int,
    val matchSize: Int,
    val uniqueSymbols: Int,
    val columns: Int,
    val rows: Int,
    val slotPositions: List<GridCell>,
    val snakeOrder: List<Int>,
    val snakeOrderSecondary: List<Int>? = null,
    val healthMax: Int,
    val healOnMatch: Int,
    val damageOnMiss: Int,
    val cardBack: CardBackStyle,
    val healthKitTier: HealthKitTier,
    val difficultyPage: DifficultyPage,
    val isFinalLevel: Boolean = level == DifficultyPage.TOTAL_LEVELS,
) {
    val totalCards: Int get() = uniqueSymbols * matchSize
    val groupsToWin: Int get() = uniqueSymbols
    val positionOnPage: Int get() = DifficultyPage.positionOnPage(level)
}
