package makza.afonsky.searchpair.multiplayer.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.url
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import makza.afonsky.searchpair.BuildConfig
import makza.afonsky.searchpair.multiplayer.network.dto.IncomingMessage
import makza.afonsky.searchpair.multiplayer.network.dto.OutgoingCommand
import makza.afonsky.searchpair.multiplayer.network.dto.OutgoingTurn

enum class ConnectionState {
    DISCONNECTED,
    CONNECTING,
    CONNECTED,
    ERROR,
}

class LobbyWebSocketClient {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
    }

    private val client = HttpClient(OkHttp) {
        install(WebSockets) {
            pingIntervalMillis = 20_000
        }
    }

    private var session: DefaultClientWebSocketSession? = null
    private var listenJob: Job? = null

    private val _connectionState = MutableStateFlow(ConnectionState.DISCONNECTED)
    val connectionState: StateFlow<ConnectionState> = _connectionState.asStateFlow()

    private val _events = MutableSharedFlow<IncomingMessage>(replay = 0, extraBufferCapacity = 64)
    val events: SharedFlow<IncomingMessage> = _events.asSharedFlow()

    suspend fun connect(userId: Long, username: String, scope: CoroutineScope) {
        if (_connectionState.value == ConnectionState.CONNECTED && session != null && listenJob?.isActive == true) {
            return
        }
        disconnect()
        _connectionState.value = ConnectionState.CONNECTING
        val wsUrl = "ws://${BuildConfig.SERVER_HOST}:${BuildConfig.SERVER_PORT}/Lobby?userId=$userId"
        runCatching {
            withContext(Dispatchers.IO) {
                session = client.webSocketSession { url(wsUrl) }
            }
            _connectionState.value = ConnectionState.CONNECTED
            listenJob = scope.launch(Dispatchers.IO) { listenIncoming() }
        }.onFailure {
            _connectionState.value = ConnectionState.ERROR
            throw it
        }
    }

    suspend fun disconnect() {
        listenJob?.cancel()
        listenJob = null
        withContext(Dispatchers.IO) {
            session?.close()
        }
        session = null
        _connectionState.value = ConnectionState.DISCONNECTED
    }

    suspend fun sendCommand(
        command: String,
        userId: Long,
        username: String? = null,
        roomId: String? = null,
        matchSize: Int? = null,
        cellCount: Int? = null,
    ) {
        val ws = session ?: error("WebSocket not connected")
        val payload = json.encodeToString(
            OutgoingCommand(
                command = command,
                userId = userId,
                username = username,
                roomId = roomId,
                matchSize = matchSize,
                cellCount = cellCount,
            ),
        )
        ws.send(Frame.Text(payload))
    }

    suspend fun sendTurn(cardId: Int, userId: Long) {
        val ws = session ?: error("WebSocket not connected")
        val payload = json.encodeToString(OutgoingTurn(cardId = cardId, userId = userId))
        ws.send(Frame.Text(payload))
    }

    private suspend fun listenIncoming() {
        val ws = session ?: return
        while (listenJob?.isActive == true) {
            runCatching {
                when (val frame = ws.incoming.receive()) {
                    is Frame.Text -> {
                        val message = IncomingMessageParser.parse(frame.readText())
                        if (message != null) {
                            _events.tryEmit(message)
                        }
                    }
                    else -> Unit
                }
            }.onFailure { error ->
                if (listenJob?.isActive == true) throw error
            }
        }
    }
}
