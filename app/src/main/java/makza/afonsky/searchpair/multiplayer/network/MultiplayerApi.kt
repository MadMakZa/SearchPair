package makza.afonsky.searchpair.multiplayer.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import makza.afonsky.searchpair.BuildConfig
import makza.afonsky.searchpair.multiplayer.network.dto.CreateUserRequest
import makza.afonsky.searchpair.multiplayer.network.dto.UserDto

class MultiplayerApi {

    private val json = Json { ignoreUnknownKeys = true }

    private val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(json)
        }
    }

    private val baseUrl: String
        get() = "http://${BuildConfig.SERVER_HOST}:${BuildConfig.SERVER_PORT}"

    suspend fun createUser(username: String): UserDto {
        return client.post("$baseUrl/users") {
            contentType(ContentType.Application.Json)
            setBody(CreateUserRequest(username))
        }.body()
    }

    suspend fun getUser(id: Long): UserDto? {
        return runCatching {
            client.get("$baseUrl/users/$id").body<UserDto>()
        }.getOrNull()
    }
}
