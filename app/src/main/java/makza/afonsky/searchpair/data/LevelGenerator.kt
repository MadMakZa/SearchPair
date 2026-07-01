package makza.afonsky.searchpair.data

object LevelGenerator {

    fun generate(level: Int): LevelConfig {
        require(level in 1..DifficultyPage.TOTAL_LEVELS) {
            "Level must be between 1 and ${DifficultyPage.TOTAL_LEVELS}"
        }

        val page = DifficultyPage.fromGlobalLevel(level)
        val positionInTier = DifficultyPage.positionOnPage(level)
        val uniqueSymbols = symbolsForPosition(page, positionInTier)
        val totalCards = uniqueSymbols * page.matchSize
        val grid = GridPatternGenerator.generate(totalCards)

        return LevelConfig(
            level = level,
            matchSize = page.matchSize,
            uniqueSymbols = uniqueSymbols,
            columns = grid.columns,
            rows = grid.rows,
            slotPositions = grid.positions,
            snakeOrder = grid.snakeOrder,
            snakeOrderSecondary = grid.snakeOrderSecondary,
            healthMax = healthMaxFor(page, positionInTier),
            healOnMatch = healFor(page, positionInTier),
            damageOnMiss = 10,
            cardBack = CardBackStyle.forLevel(level),
            healthKitTier = page.healthKitTier,
            difficultyPage = page,
        )
    }

    private fun maxUniqueSymbols(matchSize: Int): Int {
        val gridLimit = GridPatternGenerator.maxCardSlots / matchSize
        return minOf(CardAssets.maxSymbol, gridLimit)
    }

    private fun symbolsForPosition(page: DifficultyPage, positionInTier: Int): Int {
        val max = maxUniqueSymbols(page.matchSize)
        val base = when (page) {
            DifficultyPage.PAIRS -> 4
            DifficultyPage.TRIOS -> 3
            DifficultyPage.QUARTETS -> 3
        }
        if (positionInTier >= DifficultyPage.LEVELS_PER_PAGE) return max
        val progress = (positionInTier - 1).toFloat() / (DifficultyPage.LEVELS_PER_PAGE - 1)
        return (base + (max - base) * progress).toInt().coerceIn(base, max)
    }

    private fun healthMaxFor(page: DifficultyPage, positionInTier: Int): Int {
        val tierBase = when (page) {
            DifficultyPage.PAIRS -> 60
            DifficultyPage.TRIOS -> 120
            DifficultyPage.QUARTETS -> 200
        }
        return tierBase + positionInTier * 12 + page.index * 10
    }

    private fun healFor(page: DifficultyPage, positionInTier: Int): Int = when (page) {
        DifficultyPage.PAIRS -> 10
        DifficultyPage.TRIOS -> 10 + positionInTier / 4
        DifficultyPage.QUARTETS -> 15 + positionInTier / 3
    }
}
