package makza.afonsky.searchpair.game

import makza.afonsky.searchpair.data.LevelConfig

data class GameCard(
    val id: Int,
    val symbol: Int,
    val gridRow: Int,
    val gridCol: Int,
    val isFaceUp: Boolean = false,
    val isMatched: Boolean = false,
)

enum class MatchEvaluation {
    CONTINUE,
    MATCH,
    MISMATCH,
}

object MatchEngine {

    fun createDeck(config: LevelConfig): List<GameCard> {
        val symbols = (1..config.uniqueSymbols).flatMap { symbol ->
            List(config.matchSize) { symbol }
        }.shuffled()

        return symbols.mapIndexed { index, symbol ->
            val cell = config.slotPositions[index]
            GameCard(
                id = index,
                symbol = symbol,
                gridRow = cell.row,
                gridCol = cell.col,
            )
        }
    }

    fun evaluate(
        openedCardIds: List<Int>,
        cards: List<GameCard>,
        matchSize: Int,
    ): MatchEvaluation {
        if (openedCardIds.isEmpty()) return MatchEvaluation.CONTINUE

        val symbols = openedCardIds.map { id -> cards[id].symbol }

        if (matchSize >= 3 && openedCardIds.size == 2 && symbols[0] != symbols[1]) {
            return MatchEvaluation.MISMATCH
        }

        if (matchSize == 4 && openedCardIds.size == 3 && symbols.distinct().size > 1) {
            return MatchEvaluation.MISMATCH
        }

        if (openedCardIds.size < matchSize) {
            return MatchEvaluation.CONTINUE
        }

        return if (symbols.all { it == symbols.first() }) {
            MatchEvaluation.MATCH
        } else {
            MatchEvaluation.MISMATCH
        }
    }

    fun applyHeal(currentHealth: Int, healAmount: Int): Int {
        return (currentHealth - healAmount).coerceAtLeast(0)
    }

    fun applyDamage(currentHealth: Int, damage: Int, healthMax: Int): Int {
        return currentHealth + damage
    }

    fun isDefeated(health: Int, healthMax: Int): Boolean = health > healthMax
}
