package makza.afonsky.searchpair.navigation

object Routes {
    const val SPLASH = "splash"
    const val MENU = "menu"
    const val GAME = "game/{level}"
    const val BONUS = "bonus"
    const val MULTIPLAYER_LOBBY = "multiplayer/lobby"
    const val MULTIPLAYER_GAME = "multiplayer/game"

    fun game(level: Int) = "game/$level"
}
