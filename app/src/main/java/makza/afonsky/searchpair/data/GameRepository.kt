package makza.afonsky.searchpair.data

import android.content.Context

class GameRepository(context: Context) {

    private val savePrefs = context.getSharedPreferences(SAVE_PREFS, Context.MODE_PRIVATE)
    private val kitPrefs = context.getSharedPreferences(KIT_PREFS, Context.MODE_PRIVATE)

    fun getUnlockedLevel(): Int = savePrefs.getInt(KEY_LEVEL, 1).coerceIn(1, DifficultyPage.TOTAL_LEVELS)

    fun saveLevelProgress(completedLevel: Int) {
        val next = (completedLevel + 1).coerceAtMost(DifficultyPage.TOTAL_LEVELS)
        val current = getUnlockedLevel()
        if (next > current) {
            savePrefs.edit().putInt(KEY_LEVEL, next).apply()
        }
    }

    fun resetProgress() {
        savePrefs.edit().putInt(KEY_LEVEL, 1).apply()
    }

    fun unlockAllLevels() {
        savePrefs.edit().putInt(KEY_LEVEL, DifficultyPage.TOTAL_LEVELS).apply()
    }

    fun getKitCount(tier: HealthKitTier): Int = when (tier) {
        HealthKitTier.SMALL -> kitPrefs.getInt(KEY_KIT_SMALL, 0)
        HealthKitTier.MEDIUM -> kitPrefs.getInt(KEY_KIT_MEDIUM, 0)
        HealthKitTier.BIG -> kitPrefs.getInt(KEY_KIT_BIG, 0)
    }

    fun getAllKitCounts(): KitCounts = KitCounts(
        small = getKitCount(HealthKitTier.SMALL),
        medium = getKitCount(HealthKitTier.MEDIUM),
        big = getKitCount(HealthKitTier.BIG),
    )

    fun addKit(tier: HealthKitTier, amount: Int = 1) {
        val key = kitKey(tier)
        val current = kitPrefs.getInt(key, 0)
        if (current < MAX_KITS) {
            kitPrefs.edit().putInt(key, (current + amount).coerceAtMost(MAX_KITS)).apply()
        }
    }

    fun consumeKit(tier: HealthKitTier): Boolean {
        val key = kitKey(tier)
        val current = kitPrefs.getInt(key, 0)
        if (current <= 0) return false
        kitPrefs.edit().putInt(key, current - 1).apply()
        return true
    }

    fun setMaxKits() {
        kitPrefs.edit()
            .putInt(KEY_KIT_SMALL, MAX_KITS)
            .putInt(KEY_KIT_MEDIUM, MAX_KITS)
            .putInt(KEY_KIT_BIG, MAX_KITS)
            .apply()
    }

    fun exchangeKitsForLevel(level: Int) {
        when (DifficultyPage.fromGlobalLevel(level)) {
            DifficultyPage.PAIRS -> Unit
            DifficultyPage.TRIOS -> exchangeSmallToMedium()
            DifficultyPage.QUARTETS -> exchangeToBig()
        }
    }

    private fun exchangeSmallToMedium() {
        val small = getKitCount(HealthKitTier.SMALL)
        val medium = getKitCount(HealthKitTier.MEDIUM)
        if (small == 0) return

        val (addMedium, removeSmall) = when {
            small == 6 && medium < 5 -> 2 to 6
            small in 3..5 && medium < 6 -> 1 to 3
            else -> return
        }

        kitPrefs.edit()
            .putInt(KEY_KIT_MEDIUM, (medium + addMedium).coerceAtMost(MAX_KITS))
            .putInt(KEY_KIT_SMALL, small - removeSmall)
            .apply()
    }

    private fun exchangeToBig() {
        var exchanged = 0
        var removeSmall = 0
        var removeMedium = 0
        val small = getKitCount(HealthKitTier.SMALL)
        val medium = getKitCount(HealthKitTier.MEDIUM)
        var big = getKitCount(HealthKitTier.BIG)

        if (big < MAX_KITS) {
            if (small == 6 && big < MAX_KITS) {
                exchanged += 1
                removeSmall = 6
                big += 1
            }
            if (medium == 6 && big < 4) {
                exchanged += 3
                removeMedium = 6
                big += 3
            } else if (medium in 4..6 && big < 5) {
                exchanged += 2
                removeMedium = 4
                big += 2
            } else if (medium in 2..6 && big < MAX_KITS) {
                exchanged += 1
                removeMedium = 2
                big += 1
            }
        }

        if (exchanged > 0) {
            kitPrefs.edit()
                .putInt(KEY_KIT_SMALL, small - removeSmall)
                .putInt(KEY_KIT_MEDIUM, medium - removeMedium)
                .putInt(KEY_KIT_BIG, big.coerceAtMost(MAX_KITS))
                .apply()
        }
    }

    private fun kitKey(tier: HealthKitTier): String = when (tier) {
        HealthKitTier.SMALL -> KEY_KIT_SMALL
        HealthKitTier.MEDIUM -> KEY_KIT_MEDIUM
        HealthKitTier.BIG -> KEY_KIT_BIG
    }

    data class KitCounts(val small: Int, val medium: Int, val big: Int)

    companion object {
        private const val SAVE_PREFS = "Save"
        private const val KIT_PREFS = "bonusHealthSave"
        private const val KEY_LEVEL = "Level"
        private const val KEY_KIT_SMALL = "HealthKitSmall"
        private const val KEY_KIT_MEDIUM = "HealthKitMedium"
        private const val KEY_KIT_BIG = "HealthKitBig"
        const val MAX_KITS = 6
    }
}
