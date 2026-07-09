import kotlinx.serialization.json.Json
import makza.afonsky.searchpair.multiplayer.network.dto.IncomingMessage

fun main() {
    val json = Json { ignoreUnknownKeys = true; isLenient = true; encodeDefaults = true }
    val sample = """{"type":"INFO","infoType":"MATCH_FOUND","data":{"roomId":"fb7d1674-0204-4246-9679-ab4f58fab69d","opponentId":"7","opponentUsername":"42473372","player1Id":"7","player2Id":"8","player1Username":"42473372","player2Username":"15903501"}}"""
    val online = """{"type":"INFO","infoType":"ONLINE_COUNT","data":{"count":"2"}}"""
    println(json.decodeFromString<IncomingMessage>(sample))
    println(json.decodeFromString<IncomingMessage>(online))
}
