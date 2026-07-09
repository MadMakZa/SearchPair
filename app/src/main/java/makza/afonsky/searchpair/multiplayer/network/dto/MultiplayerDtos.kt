package makza.afonsky.searchpair.multiplayer.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: Long,
    val username: String,
    val wins: Int = 0,
    val losses: Int = 0,
)

@Serializable
data class CreateUserRequest(
    val username: String,
)

@Serializable
data class OutgoingCommand(
    val type: String = "COMMAND",
    val command: String,
    val userId: Long? = null,
    val username: String? = null,
    val roomId: String? = null,
    val matchSize: Int? = null,
    val cellCount: Int? = null,
)

@Serializable
data class OutgoingTurn(
    val type: String = "TURN",
    val cardId: Int,
    val userId: Long,
)

@Serializable
data class IncomingMessage(
    // Server InfoMessage JSON omits "type"; kotlinx defaults apply when the key is missing.
    val type: String = "INFO",
    val infoType: String? = null,
    val data: Map<String, String> = emptyMap(),
    val command: String? = null,
)

object InfoTypes {
    const val ONLINE_COUNT = "ONLINE_COUNT"
    const val SEARCHING = "SEARCHING"
    const val SEARCH_STOPPED = "SEARCH_STOPPED"
    const val MATCH_FOUND = "MATCH_FOUND"
    const val GAME_STARTED = "GAME_STARTED"
    const val GAME_STATE = "GAME_STATE"
    const val GAME_FINISHED = "GAME_FINISHED"
    const val TURN_CHANGED = "TURN_CHANGED"
    const val CARD_OPENED = "CARD_OPENED"
    const val PAIR_MATCHED = "PAIR_MATCHED"
    const val PAIR_NOT_MATCHED = "PAIR_NOT_MATCHED"
    const val READY_STATUS = "READY_STATUS"
    const val LOBBY_LIST = "LOBBY_LIST"
    const val LOBBY_STATE = "LOBBY_STATE"
    const val COUNTDOWN_STARTED = "COUNTDOWN_STARTED"
    const val COUNTDOWN_TICK = "COUNTDOWN_TICK"
    const val COUNTDOWN_CANCELLED = "COUNTDOWN_CANCELLED"
    const val PARTNER_LEFT_LOBBY = "PARTNER_LEFT_LOBBY"
    const val ERROR = "ERROR"
    const val INFO = "INFO"
}

object Commands {
    const val SEARCH_START = "SEARCH_START"
    const val SEARCH_STOP = "SEARCH_STOP"
    const val READY = "READY"
    const val UNREADY = "UNREADY"
    const val SURRENDER = "SURRENDER"
    const val SET_USERNAME = "SET_USERNAME"
    const val CREATE_LOBBY = "CREATE_LOBBY"
    const val JOIN_LOBBY = "JOIN_LOBBY"
    const val LEAVE_LOBBY = "LEAVE_LOBBY"
    const val LIST_LOBBIES = "LIST_LOBBIES"
}
