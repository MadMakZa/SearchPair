package makza.afonsky.searchpair.multiplayer.network

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import makza.afonsky.searchpair.multiplayer.network.dto.IncomingMessage

object IncomingMessageParser {

    private val json = Json { ignoreUnknownKeys = true; isLenient = true }

    fun parse(text: String): IncomingMessage? {
        return runCatching {
            val root = json.parseToJsonElement(text).jsonObject
            val infoType = root.stringField("infoType")
            val type = root.stringField("type")
                ?: if (infoType != null) "INFO" else return null
            val data = root["data"]?.jsonObject?.toStringMap().orEmpty()
            IncomingMessage(type = type, infoType = infoType, data = data)
        }.getOrNull()
    }

    private fun JsonObject.stringField(key: String): String? =
        this[key]?.jsonPrimitive?.content

    private fun JsonObject.toStringMap(): Map<String, String> =
        entries.associate { (key, value) ->
            key to when {
                value is kotlinx.serialization.json.JsonPrimitive -> value.content
                else -> value.toString()
            }
        }
}
