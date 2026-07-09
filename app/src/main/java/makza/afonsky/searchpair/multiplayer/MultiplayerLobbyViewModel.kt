package makza.afonsky.searchpair.multiplayer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import makza.afonsky.searchpair.R
import makza.afonsky.searchpair.multiplayer.network.ConnectionState
import makza.afonsky.searchpair.multiplayer.network.dto.InfoTypes

data class MultiplayerLobbyUiState(
    val nickname: String = "",
    val nicknameDraft: String = "",
    val isEditingNickname: Boolean = false,
    val onlineCount: Int = 0,
    val isSearching: Boolean = false,
    val isConnecting: Boolean = false,
    val navigateToGame: Boolean = false,
    val connectionState: ConnectionState = ConnectionState.DISCONNECTED,
    val statusMessage: String = "",
    val screenMode: LobbyScreenMode = LobbyScreenMode.Browse,
    val publicLobbies: List<PublicLobbyItem> = emptyList(),
    val showCreateLobby: Boolean = false,
    val createLobbyMatchSize: Int = 2,
    val createLobbyCellCount: Int = 24,
    val roomId: String? = null,
    val matchSize: Int = 2,
    val cellCount: Int = 24,
    val players: List<RoomPlayer> = emptyList(),
    val countdownSeconds: Int? = null,
    val myUserId: Long = 0L,
)

class MultiplayerLobbyViewModel(
    application: Application,
    private val repository: MultiplayerRepository,
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(MultiplayerLobbyUiState())
    val uiState: StateFlow<MultiplayerLobbyUiState> = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(
                nickname = repository.getNickname(),
                nicknameDraft = repository.getNickname(),
                myUserId = repository.getUserId(),
            )
        }
        viewModelScope.launch {
            repository.connectionState.collect { state ->
                _uiState.update { current ->
                    current.copy(
                        connectionState = state,
                        isSearching = if (state == ConnectionState.DISCONNECTED || state == ConnectionState.ERROR) {
                            false
                        } else {
                            current.isSearching
                        },
                        isConnecting = if (state == ConnectionState.DISCONNECTED || state == ConnectionState.ERROR) {
                            false
                        } else {
                            current.isConnecting
                        },
                    )
                }
            }
        }
        viewModelScope.launch(Dispatchers.Default) {
            repository.events.collect { message ->
                withContext(Dispatchers.Main) {
                    handleIncomingMessage(message)
                }
            }
        }
    }

    private fun handleIncomingMessage(message: makza.afonsky.searchpair.multiplayer.network.dto.IncomingMessage) {
        if (!message.type.equals("INFO", ignoreCase = true)) return
        when (message.infoType?.uppercase()) {
            InfoTypes.ONLINE_COUNT -> {
                val count = message.data["count"]?.toIntOrNull() ?: 0
                _uiState.update { it.copy(onlineCount = count) }
            }
            InfoTypes.LOBBY_LIST -> {
                _uiState.update { it.copy(publicLobbies = parseLobbyList(message.data["lobbies"].orEmpty())) }
            }
            InfoTypes.SEARCHING -> {
                message.data["roomId"]?.let { repository.setCurrentRoomId(it) }
                _uiState.update {
                    it.copy(
                        isSearching = true,
                        isConnecting = false,
                        isEditingNickname = false,
                        nicknameDraft = it.nickname,
                        statusMessage = "",
                    )
                }
            }
            InfoTypes.SEARCH_STOPPED -> {
                _uiState.update {
                    it.copy(isSearching = false, isConnecting = false, statusMessage = "")
                }
            }
            InfoTypes.MATCH_FOUND -> {
                applyRoomState(message.data)
            }
            InfoTypes.LOBBY_STATE -> {
                applyRoomState(message.data)
            }
            InfoTypes.READY_STATUS -> {
                applyReadyStatus(message.data)
            }
            InfoTypes.COUNTDOWN_STARTED, InfoTypes.COUNTDOWN_TICK -> {
                val seconds = message.data["seconds"]?.toIntOrNull()
                _uiState.update { it.copy(countdownSeconds = seconds) }
            }
            InfoTypes.COUNTDOWN_CANCELLED -> {
                _uiState.update { it.copy(countdownSeconds = null) }
            }
            InfoTypes.GAME_STARTED -> {
                if (repository.suppressLobbyAutoNavigate) {
                    viewModelScope.launch {
                        runCatching { repository.sendSurrender() }
                    }
                    return
                }
                _uiState.update {
                    it.copy(navigateToGame = true, countdownSeconds = null, statusMessage = "")
                }
            }
            InfoTypes.GAME_FINISHED -> {
                _uiState.update { state ->
                    state.copy(
                        screenMode = if (repository.isSearchRoom) {
                            LobbyScreenMode.Browse
                        } else {
                            LobbyScreenMode.InRoom
                        },
                        navigateToGame = false,
                        countdownSeconds = null,
                        isSearching = false,
                        isConnecting = false,
                        players = state.players.map { player -> player.copy(isReady = false) },
                        statusMessage = "",
                    )
                }
            }
            InfoTypes.PARTNER_LEFT_LOBBY -> {
                handlePartnerLeftLobby()
            }
            InfoTypes.ERROR -> {
                _uiState.update {
                    it.copy(
                        isSearching = false,
                        isConnecting = false,
                        statusMessage = message.data["message"] ?: "Error",
                    )
                }
            }
            InfoTypes.INFO -> {
                when (message.data["message"]) {
                    "Not searching" -> _uiState.update {
                        it.copy(isSearching = false, isConnecting = false, statusMessage = "")
                    }
                    "Left lobby" -> resetToBrowse()
                    "Lobby created" -> _uiState.update {
                        it.copy(showCreateLobby = false, screenMode = LobbyScreenMode.InRoom)
                    }
                }
            }
        }
    }

    private fun applyRoomState(data: Map<String, String>) {
        val roomId = data["roomId"] ?: return
        repository.setCurrentRoomId(roomId)
        val matchSize = data["matchSize"]?.toIntOrNull() ?: 2
        val cellCount = data["cellCount"]?.toIntOrNull() ?: 24
        val fromSearch = data["fromSearch"]?.toBooleanStrictOrNull() ?: false
        repository.setSearchRoom(fromSearch)
        val p1Id = data["player1Id"]?.toLongOrNull() ?: 0L
        val p2Id = data["player2Id"]?.toLongOrNull()
        val players = buildList {
            if (p1Id != 0L) {
                add(
                    RoomPlayer(
                        userId = p1Id,
                        name = data["player1Username"].orEmpty(),
                        isReady = data["player1Ready"]?.toBooleanStrictOrNull() ?: false,
                    ),
                )
            }
            if (p2Id != null && p2Id != 0L) {
                add(
                    RoomPlayer(
                        userId = p2Id,
                        name = data["player2Username"].orEmpty(),
                        isReady = data["player2Ready"]?.toBooleanStrictOrNull() ?: false,
                    ),
                )
                val myId = repository.getUserId()
                val opponentId = if (myId == p1Id) p2Id else p1Id
                val opponentName = if (myId == p1Id) {
                    data["player2Username"].orEmpty()
                } else {
                    data["player1Username"].orEmpty()
                }
                repository.setMatchInfo(opponentId, opponentName, matchSize, cellCount)
            }
        }
        val skipLobby = fromSearch && players.size >= 2
        if (skipLobby) {
            repository.setAutoReadyOnGameEnter(true)
            repository.clearStaleGameState()
        }
        _uiState.update {
            it.copy(
                screenMode = if (skipLobby) LobbyScreenMode.Browse else LobbyScreenMode.InRoom,
                roomId = roomId,
                matchSize = matchSize,
                cellCount = cellCount,
                players = players,
                isSearching = false,
                isConnecting = false,
                isEditingNickname = false,
                nicknameDraft = it.nickname,
                navigateToGame = skipLobby,
                statusMessage = when {
                    skipLobby -> ""
                    players.size < 2 -> "Waiting for opponent..."
                    else -> ""
                },
            )
        }
    }

    private fun applyReadyStatus(data: Map<String, String>) {
        val p1Id = data["player1Id"]?.toLongOrNull() ?: return
        val p2Id = data["player2Id"]?.toLongOrNull()
        val players = buildList {
            add(
                RoomPlayer(
                    userId = p1Id,
                    name = data["player1Username"].orEmpty(),
                    isReady = data["player1Ready"]?.toBooleanStrictOrNull() ?: false,
                ),
            )
            if (p2Id != null && p2Id != 0L) {
                add(
                    RoomPlayer(
                        userId = p2Id,
                        name = data["player2Username"].orEmpty(),
                        isReady = data["player2Ready"]?.toBooleanStrictOrNull() ?: false,
                    ),
                )
            }
        }
        _uiState.update { it.copy(players = players) }
    }

    private fun parseLobbyList(raw: String): List<PublicLobbyItem> {
        if (raw.isBlank()) return emptyList()
        return raw.split("|").mapNotNull { entry ->
            val parts = entry.split(";")
            if (parts.size < 4) return@mapNotNull null
            PublicLobbyItem(
                roomId = parts[0],
                hostName = parts[1],
                matchSize = parts[2].toIntOrNull() ?: 2,
                cellCount = if (parts.size >= 5) {
                    parts[3].toIntOrNull() ?: 24
                } else {
                    24
                },
                hostId = if (parts.size >= 5) {
                    parts[4].toLongOrNull() ?: 0L
                } else {
                    parts[3].toLongOrNull() ?: 0L
                },
            )
        }
    }

    private fun handlePartnerLeftLobby() {
        val message = getApplication<Application>().getString(R.string.multiplayer_partner_left)
        repository.setCurrentRoomId(null)
        repository.setSearchRoom(false)
        repository.clearMatchInfo()
        _uiState.update {
            it.copy(
                screenMode = LobbyScreenMode.Browse,
                roomId = null,
                players = emptyList(),
                countdownSeconds = null,
                isSearching = false,
                isConnecting = false,
                navigateToGame = false,
                statusMessage = message,
            )
        }
    }

    private fun resetToBrowse() {
        clearLobbyAutoNavigateSuppress()
        repository.setCurrentRoomId(null)
        repository.setSearchRoom(false)
        repository.clearMatchInfo()
        _uiState.update {
            it.copy(
                screenMode = LobbyScreenMode.Browse,
                roomId = null,
                players = emptyList(),
                countdownSeconds = null,
                isSearching = false,
                isConnecting = false,
                statusMessage = "",
            )
        }
    }

    fun onNavigatedToGame() {
        _uiState.update { it.copy(navigateToGame = false) }
    }

    fun onScreenEnter() {
        viewModelScope.launch {
            runCatching {
                repository.connect(viewModelScope)
                _uiState.update { it.copy(myUserId = repository.getUserId()) }
                repository.listLobbies(viewModelScope)
            }.onFailure { e ->
                _uiState.update { it.copy(statusMessage = e.message ?: "Connection failed") }
            }
        }
    }

    fun onScreenResumed() {
        viewModelScope.launch {
            runCatching { repository.listLobbies(viewModelScope) }
            if (repository.suppressLobbyAutoNavigate) {
                _uiState.update { it.copy(navigateToGame = false) }
            }
        }
    }

    private fun clearLobbyAutoNavigateSuppress() {
        repository.suppressLobbyAutoNavigate = false
    }

    private suspend fun leaveCurrentRoomIfNeeded() {
        if (_uiState.value.roomId == null) return
        runCatching { repository.leaveLobby(viewModelScope) }
        resetToBrowse()
    }

    fun onExitLobby(onFinished: () -> Unit) {
        viewModelScope.launch {
            if (_uiState.value.roomId != null) {
                runCatching { repository.leaveLobby(viewModelScope) }
                resetToBrowse()
            }
            runCatching { repository.disconnect() }
            onFinished()
        }
    }

    fun onScreenLeave() {
        viewModelScope.launch {
            if (_uiState.value.roomId != null) {
                runCatching { repository.leaveLobby(viewModelScope) }
                resetToBrowse()
            }
            repository.disconnect()
        }
    }

    fun onStartNicknameEdit() {
        val state = _uiState.value
        if (state.screenMode != LobbyScreenMode.Browse || state.isSearching || state.isConnecting) return
        _uiState.update {
            it.copy(isEditingNickname = true, nicknameDraft = it.nickname)
        }
    }

    fun onNicknameDraftChange(value: String) {
        if (value.length <= 20) {
            _uiState.update { it.copy(nicknameDraft = value) }
        }
    }

    fun onCancelNicknameEdit() {
        _uiState.update {
            it.copy(isEditingNickname = false, nicknameDraft = it.nickname)
        }
    }

    fun onConfirmNicknameEdit() {
        val nickname = _uiState.value.nicknameDraft.trim().take(20).ifBlank { repository.getNickname() }
        repository.setNickname(nickname)
        _uiState.update { it.copy(nickname = nickname, isEditingNickname = false) }
        viewModelScope.launch {
            runCatching { repository.updateUsernameOnServer(nickname) }
        }
    }

    fun onSearchToggle() {
        viewModelScope.launch {
            val searchActive = _uiState.value.isSearching || _uiState.value.isConnecting
            if (searchActive) {
                runCatching { repository.stopSearch(viewModelScope) }
                    .onSuccess {
                        _uiState.update {
                            it.copy(isSearching = false, isConnecting = false, statusMessage = "")
                        }
                    }
            } else {
                clearLobbyAutoNavigateSuppress()
                repository.clearStaleGameState()
                _uiState.update { it.copy(isConnecting = true, statusMessage = "") }
                runCatching {
                    leaveCurrentRoomIfNeeded()
                    repository.startSearch(viewModelScope)
                }.onFailure { e ->
                    _uiState.update {
                        it.copy(isSearching = false, isConnecting = false, statusMessage = e.message ?: "Failed")
                    }
                }
            }
        }
    }

    fun onRefreshLobbies() {
        viewModelScope.launch {
            runCatching { repository.listLobbies(viewModelScope) }
        }
    }

    fun onShowCreateLobby() {
        _uiState.update {
            it.copy(showCreateLobby = true, createLobbyMatchSize = 2, createLobbyCellCount = 24)
        }
    }

    fun onDismissCreateLobby() {
        _uiState.update { it.copy(showCreateLobby = false) }
    }

    fun onCreateLobbyMatchSizeChange(matchSize: Int) {
        _uiState.update { it.copy(createLobbyMatchSize = matchSize) }
    }

    fun onCreateLobbyCellCountChange(cellCount: Int) {
        _uiState.update { it.copy(createLobbyCellCount = cellCount) }
    }

    fun onCreateLobbyConfirm() {
        clearLobbyAutoNavigateSuppress()
        viewModelScope.launch {
            runCatching {
                leaveCurrentRoomIfNeeded()
                repository.createLobby(
                    viewModelScope,
                    _uiState.value.createLobbyMatchSize,
                    _uiState.value.createLobbyCellCount,
                )
            }.onFailure { e ->
                _uiState.update { it.copy(statusMessage = e.message ?: "Failed to create lobby") }
            }
        }
    }

    fun onJoinLobby(roomId: String) {
        clearLobbyAutoNavigateSuppress()
        viewModelScope.launch {
            runCatching {
                if (_uiState.value.roomId != null && _uiState.value.roomId != roomId) {
                    leaveCurrentRoomIfNeeded()
                }
                repository.joinLobby(viewModelScope, roomId)
            }.onFailure { e ->
                _uiState.update { it.copy(statusMessage = e.message ?: "Failed to join") }
            }
        }
    }

    fun onLeaveLobby() {
        clearLobbyAutoNavigateSuppress()
        viewModelScope.launch {
            runCatching { repository.leaveLobby(viewModelScope) }
                .onFailure { e ->
                    _uiState.update { it.copy(statusMessage = e.message ?: "Failed to leave") }
                }
            resetToBrowse()
            repository.listLobbies(viewModelScope)
        }
    }

    fun onToggleReady() {
        clearLobbyAutoNavigateSuppress()
        val myId = repository.getUserId()
        val me = _uiState.value.players.find { it.userId == myId }
        val willBeReady = !(me?.isReady ?: false)
        if (me != null) {
            _uiState.update { state ->
                state.copy(
                    players = state.players.map { player ->
                        if (player.userId == myId) player.copy(isReady = willBeReady) else player
                    },
                )
            }
        }
        viewModelScope.launch {
            if (willBeReady) {
                repository.sendReady()
            } else {
                repository.sendUnready()
            }
        }
    }

    class Factory(
        private val application: Application,
        private val repository: MultiplayerRepository,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MultiplayerLobbyViewModel(application, repository) as T
        }
    }
}
