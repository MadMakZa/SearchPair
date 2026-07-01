package makza.afonsky.searchpair.data

import makza.afonsky.searchpair.R

object DifficultyBackground {

    fun forPage(page: DifficultyPage): Int = when (page) {
        DifficultyPage.PAIRS -> R.drawable.background_pyramides_day
        DifficultyPage.TRIOS -> R.drawable.background_pyramides_dark
        DifficultyPage.QUARTETS -> R.drawable.background_pyramides_scary
    }

    val menu: Int get() = R.drawable.background_menu_oasis
}
