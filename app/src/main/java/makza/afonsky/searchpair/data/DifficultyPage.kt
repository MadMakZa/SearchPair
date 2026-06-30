package makza.afonsky.searchpair.data

enum class DifficultyPage(val index: Int, val matchSize: Int, val healthKitTier: HealthKitTier) {
    PAIRS(0, 2, HealthKitTier.SMALL),
    TRIOS(1, 3, HealthKitTier.MEDIUM),
    QUARTETS(2, 4, HealthKitTier.BIG);

    val firstLevel: Int get() = index * LEVELS_PER_PAGE + 1
    val lastLevel: Int get() = (index + 1) * LEVELS_PER_PAGE

    companion object {
        const val LEVELS_PER_PAGE = 16
        const val PAGE_COUNT = 3
        const val TOTAL_LEVELS = LEVELS_PER_PAGE * PAGE_COUNT
        const val GRID_COLUMNS = 4
        const val GRID_ROWS = 4

        fun fromGlobalLevel(level: Int): DifficultyPage = when {
            level <= PAIRS.lastLevel -> PAIRS
            level <= TRIOS.lastLevel -> TRIOS
            else -> QUARTETS
        }

        fun globalLevel(page: DifficultyPage, positionOnPage: Int): Int {
            return page.firstLevel + positionOnPage - 1
        }

        fun positionOnPage(globalLevel: Int): Int {
            return ((globalLevel - 1) % LEVELS_PER_PAGE) + 1
        }

        fun isPageUnlocked(page: DifficultyPage, unlockedLevel: Int): Boolean =
            unlockedLevel >= page.firstLevel
    }
}
