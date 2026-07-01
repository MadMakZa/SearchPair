package makza.afonsky.searchpair.navigation

object Routes {
    const val SPLASH = "splash"
    const val MENU = "menu"
    const val GAME = "game/{level}"
    const val BONUS = "bonus"

    fun game(level: Int) = "game/$level"
}
