package makza.afonsky.searchpair.multiplayer

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import makza.afonsky.searchpair.multiplayer.network.ConnectionState
import makza.afonsky.searchpair.multiplayer.network.LobbyWebSocketClient
import makza.afonsky.searchpair.multiplayer.network.MultiplayerApi
import makza.afonsky.searchpair.multiplayer.network.dto.Commands
import makza.afonsky.searchpair.multiplayer.network.dto.IncomingMessage
import makza.afonsky.searchpair.multiplayer.network.dto.InfoTypes

class MultiplayerRepository(context: Context) {

    private val preferences = MultiplayerPreferences(context.applicationContext)
    private val api = MultiplayerApi()
    val webSocket = LobbyWebSocketClient()

    var pendingOpponentId: Long = 0L
        private set
    var pendingOpponentName: String = ""
        private set
    var pendingMatchSize: Int = 2
        private set
    var pendingCellCount: Int = MultiplayerFieldMapper.DEFAULT_CELL_COUNT
        private set
    var isSearchRoom: Boolean = false
        private set
    private var autoReadyOnGameEnter: Boolean = false

    private var lastGameStateData: Map<String, String>? = null
    private var gameStateCollectorJob: Job? = null
    private var currentRoomId: String? = null
    var suppressLobbyAutoNavigate: Boolean = false

    val connectionState = webSocket.connectionState
    val events: SharedFlow<IncomingMessage> = webSocket.events

    fun peekGameState(): Map<String, String>? = lastGameStateData

    fun getCurrentRoomId(): String? = currentRoomId

    fun setCurrentRoomId(roomId: String?) {
        currentRoomId = roomId
    }

    fun clearGameStateCache() {
        lastGameStateData = null
        currentRoomId = null
    }

    suspend fun ensureUserRegistered(): Long {
        val existingId = preferences.userId
        if (existingId != 0L) {
            val user = api.getUser(existingId)
            if (user != null) return existingId
        }
        val created = api.createUser(preferences.nickname)
        preferences.userId = created.id
        return created.id
    }

    fun getNickname(): String = preferences.nickname

    fun setNickname(value: String) {
        preferences.nickname = value.trim().take(20)
    }

    fun setMatchInfo(
        opponentId: Long,
        opponentName: String,
        matchSize: Int = 2,
        cellCount: Int = MultiplayerFieldMapper.DEFAULT_CELL_COUNT,
    ) {
        pendingOpponentId = opponentId
        pendingOpponentName = opponentName
        pendingMatchSize = matchSize
        pendingCellCount = cellCount
    }

    fun setSearchRoom(fromSearch: Boolean) {
        isSearchRoom = fromSearch
    }

    fun setAutoReadyOnGameEnter(enabled: Boolean) {
        autoReadyOnGameEnter = enabled
    }

    fun consumeAutoReadyOnGameEnter(): Boolean {
        val value = autoReadyOnGameEnter
        autoReadyOnGameEnter = false
        return value
    }

    fun clearMatchInfo() {
        pendingOpponentId = 0L
        pendingOpponentName = ""
        pendingMatchSize = 2
        pendingCellCount = MultiplayerFieldMapper.DEFAULT_CELL_COUNT
        isSearchRoom = false
        autoReadyOnGameEnter = false
        lastGameStateData = null
    }

    fun clearStaleGameState() {
        lastGameStateData = null
    }

    suspend fun connect(scope: CoroutineScope) {
        val userId = ensureUserRegistered()
        webSocket.connect(userId, preferences.nickname, scope)
        gameStateCollectorJob?.cancel()
        gameStateCollectorJob = scope.launch {
            webSocket.events.collect { message ->
                if (message.type == "INFO" && message.infoType == InfoTypes.GAME_STATE) {
                    lastGameStateData = message.data
                }
            }
        }
    }

    private suspend fun awaitConnected(scope: CoroutineScope) {
        when (webSocket.connectionState.value) {
            ConnectionState.CONNECTED -> return
            ConnectionState.CONNECTING -> {
                webSocket.connectionState.first {
                    it == ConnectionState.CONNECTED ||
                        it == ConnectionState.ERROR ||
                        it == ConnectionState.DISCONNECTED
                }
                if (webSocket.connectionState.value != ConnectionState.CONNECTED) {
                    connect(scope)
                }
            }
            else -> connect(scope)
        }
    }

    suspend fun disconnect() {
        gameStateCollectorJob?.cancel()
        gameStateCollectorJob = null
        clearGameStateCache()
        webSocket.disconnect()
    }

    suspend fun startSearch(scope: CoroutineScope) {
        awaitConnected(scope)
        val nickname = preferences.nickname
        if (nickname.isNotBlank()) {
            webSocket.sendCommand(Commands.SET_USERNAME, preferences.userId, nickname)
        }
        webSocket.sendCommand(Commands.SEARCH_START, preferences.userId)
    }

    suspend fun stopSearch(scope: CoroutineScope) {
        awaitConnected(scope)
        webSocket.sendCommand(Commands.SEARCH_STOP, preferences.userId)
    }

    suspend fun listLobbies(scope: CoroutineScope) {
        awaitConnected(scope)
        webSocket.sendCommand(Commands.LIST_LOBBIES, preferences.userId)
    }

    suspend fun createLobby(scope: CoroutineScope, matchSize: Int, cellCount: Int) {
        awaitConnected(scope)
        webSocket.sendCommand(Commands.CREATE_LOBBY, preferences.userId, matchSize = matchSize, cellCount = cellCount)
    }

    suspend fun joinLobby(scope: CoroutineScope, roomId: String) {
        awaitConnected(scope)
        webSocket.sendCommand(Commands.JOIN_LOBBY, preferences.userId, roomId = roomId)
    }

    suspend fun leaveLobby(scope: CoroutineScope) {
        awaitConnected(scope)
        webSocket.sendCommand(Commands.LEAVE_LOBBY, preferences.userId)
    }

    suspend fun sendReady() {
        webSocket.sendCommand(Commands.READY, preferences.userId, roomId = currentRoomId)
    }

    suspend fun sendUnready() {
        webSocket.sendCommand(Commands.UNREADY, preferences.userId, roomId = currentRoomId)
    }

    suspend fun sendSurrender() {
        webSocket.sendCommand(Commands.SURRENDER, preferences.userId)
    }

    suspend fun sendTurn(cardId: Int) {
        webSocket.sendTurn(cardId, preferences.userId)
    }

    suspend fun updateUsernameOnServer(username: String) {
        preferences.nickname = username
        if (preferences.userId != 0L) {
            webSocket.sendCommand(Commands.SET_USERNAME, preferences.userId, username)
        }
    }

    fun getUserId(): Long = preferences.userId
}
