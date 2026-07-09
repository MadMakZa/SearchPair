package makza.afonsky.searchpair.multiplayer

enum class LobbyScreenMode {
    Browse,
    InRoom,
}

data class PublicLobbyItem(
    val roomId: String,
    val hostName: String,
    val matchSize: Int,
    val cellCount: Int,
    val hostId: Long,
)

data class RoomPlayer(
    val userId: Long,
    val name: String,
    val isReady: Boolean,
)

fun matchSizeLabel(matchSize: Int): String = when (matchSize) {
    3 -> "Trios"
    4 -> "Quartets"
    else -> "Pairs"
}

fun lobbyDescription(matchSize: Int, cellCount: Int): String =
    "${matchSizeLabel(matchSize)} · $cellCount cells"

fun matchSizeToDifficultyPage(matchSize: Int): makza.afonsky.searchpair.data.DifficultyPage = when (matchSize) {
    3 -> makza.afonsky.searchpair.data.DifficultyPage.TRIOS
    4 -> makza.afonsky.searchpair.data.DifficultyPage.QUARTETS
    else -> makza.afonsky.searchpair.data.DifficultyPage.PAIRS
}
