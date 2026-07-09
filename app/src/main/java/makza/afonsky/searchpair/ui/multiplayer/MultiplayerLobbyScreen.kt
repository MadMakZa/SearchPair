package makza.afonsky.searchpair.ui.multiplayer

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import makza.afonsky.searchpair.R
import makza.afonsky.searchpair.data.DifficultyBackground
import makza.afonsky.searchpair.multiplayer.LobbyScreenMode
import makza.afonsky.searchpair.multiplayer.MultiplayerLobbyViewModel
import makza.afonsky.searchpair.multiplayer.PublicLobbyItem
import makza.afonsky.searchpair.multiplayer.RoomPlayer
import makza.afonsky.searchpair.multiplayer.lobbyDescription
import makza.afonsky.searchpair.ui.components.GameButton
import makza.afonsky.searchpair.ui.theme.ButtonBorder
import makza.afonsky.searchpair.ui.theme.ColorGold
import makza.afonsky.searchpair.ui.theme.ColorOrange2
import makza.afonsky.searchpair.ui.theme.ColorRedDark

private val LobbyCardShape = RoundedCornerShape(16.dp)
private val PlayerCardShape = RoundedCornerShape(10.dp)

@Composable
fun MultiplayerLobbyScreen(
    viewModel: MultiplayerLobbyViewModel,
    onNavigateToGame: () -> Unit,
    onBack: () -> Unit,
) {
    val state by viewModel.uiState.collectAsState()
    val searchActive = state.isSearching || state.isConnecting
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(Unit) {
        viewModel.onScreenEnter()
        onDispose { }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.onScreenResumed()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    val leaveLobby = {
        viewModel.onExitLobby(onBack)
    }

    BackHandler(onBack = leaveLobby)

    LaunchedEffect(state.navigateToGame) {
        if (state.navigateToGame) {
            viewModel.onNavigatedToGame()
            onNavigateToGame()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(DifficultyBackground.menu),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(ColorOrange2.copy(alpha = 0.18f), LobbyCardShape)
                    .border(2.dp, ButtonBorder, LobbyCardShape)
                    .padding(horizontal = 16.dp, vertical = 14.dp),
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = stringResource(R.string.multiplayer_title),
                        fontSize = 32.sp,
                        color = ColorGold,
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = stringResource(R.string.multiplayer_nickname_label),
                        color = ColorRedDark,
                    )
                    EditableNicknameRow(
                        nickname = state.nickname,
                        draft = state.nicknameDraft,
                        isEditing = state.isEditingNickname,
                        editable = state.screenMode == LobbyScreenMode.Browse &&
                            !searchActive,
                        onStartEdit = viewModel::onStartNicknameEdit,
                        onDraftChange = viewModel::onNicknameDraftChange,
                        onConfirm = viewModel::onConfirmNicknameEdit,
                        onCancel = viewModel::onCancelNicknameEdit,
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = stringResource(R.string.multiplayer_online_label) + ": ${state.onlineCount}",
                        color = ColorRedDark,
                        fontSize = 16.sp,
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    when (state.screenMode) {
                        LobbyScreenMode.Browse -> BrowseContent(
                            modifier = Modifier.weight(1f),
                            searchActive = searchActive,
                            publicLobbies = state.publicLobbies,
                            statusMessage = state.statusMessage,
                            onSearchToggle = viewModel::onSearchToggle,
                            onRefreshLobbies = viewModel::onRefreshLobbies,
                            onCreateLobby = viewModel::onShowCreateLobby,
                            onJoinLobby = viewModel::onJoinLobby,
                        )
                        LobbyScreenMode.InRoom -> InRoomContent(
                            modifier = Modifier.weight(1f),
                            matchSize = state.matchSize,
                            cellCount = state.cellCount,
                            players = state.players,
                            myUserId = state.myUserId,
                            countdownSeconds = state.countdownSeconds,
                            statusMessage = state.statusMessage,
                            onToggleReady = viewModel::onToggleReady,
                            onLeaveLobby = viewModel::onLeaveLobby,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            GameButton(
                text = stringResource(R.string.button_close),
                onClick = leaveLobby,
                modifier = Modifier.fillMaxWidth(0.5f),
            )
        }
    }

    if (state.showCreateLobby) {
        CreateLobbyDialog(
            selectedMatchSize = state.createLobbyMatchSize,
            selectedCellCount = state.createLobbyCellCount,
            onMatchSizeChange = viewModel::onCreateLobbyMatchSizeChange,
            onCellCountChange = viewModel::onCreateLobbyCellCountChange,
            onConfirm = viewModel::onCreateLobbyConfirm,
            onDismiss = viewModel::onDismissCreateLobby,
        )
    }
}

@Composable
private fun BrowseContent(
    modifier: Modifier = Modifier,
    searchActive: Boolean,
    publicLobbies: List<PublicLobbyItem>,
    statusMessage: String,
    onSearchToggle: () -> Unit,
    onRefreshLobbies: () -> Unit,
    onCreateLobby: () -> Unit,
    onJoinLobby: (String) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        GameButton(
            text = if (searchActive) {
                stringResource(R.string.multiplayer_stop)
            } else {
                stringResource(R.string.multiplayer_search)
            },
            onClick = onSearchToggle,
            modifier = Modifier.fillMaxWidth(0.75f),
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            GameButton(
                text = stringResource(R.string.multiplayer_create),
                onClick = onCreateLobby,
                modifier = Modifier.weight(1f),
            )
            Spacer(modifier = Modifier.padding(4.dp))
            GameButton(
                text = stringResource(R.string.multiplayer_refresh),
                onClick = onRefreshLobbies,
                modifier = Modifier.weight(1f),
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.multiplayer_lobby_list_title),
            color = ColorRedDark,
            fontSize = 18.sp,
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .border(2.dp, ColorRedDark, RoundedCornerShape(8.dp))
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            if (publicLobbies.isEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.multiplayer_no_lobbies),
                        color = ColorRedDark,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                    )
                }
            } else {
                items(publicLobbies, key = { it.roomId }) { lobby ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, ColorRedDark, RoundedCornerShape(6.dp))
                            .clickable { onJoinLobby(lobby.roomId) }
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Column {
                            Text(text = lobby.hostName, color = ColorRedDark, fontSize = 16.sp)
                            Text(
                                text = lobbyDescription(lobby.matchSize, lobby.cellCount),
                                color = ColorRedDark,
                                fontSize = 13.sp,
                            )
                        }
                        Text(text = stringResource(R.string.multiplayer_join), color = ColorRedDark)
                    }
                }
            }
        }

        if (statusMessage.isNotBlank()) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = statusMessage, color = ColorRedDark, textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun InRoomContent(
    modifier: Modifier = Modifier,
    matchSize: Int,
    cellCount: Int,
    players: List<RoomPlayer>,
    myUserId: Long,
    countdownSeconds: Int?,
    statusMessage: String,
    onToggleReady: () -> Unit,
    onLeaveLobby: () -> Unit,
) {
    val me = players.find { it.userId == myUserId }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(
                R.string.multiplayer_room_title,
                lobbyDescription(matchSize, cellCount),
            ),
            color = ColorRedDark,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(8.dp))

        players.forEach { player ->
            PlayerRow(player = player)
        }

        if (players.size < 2) {
            Text(
                text = stringResource(R.string.multiplayer_waiting_opponent),
                color = ColorRedDark,
                fontSize = 14.sp,
            )
        }

        countdownSeconds?.let { seconds ->
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.multiplayer_countdown, seconds),
                color = ColorRedDark,
                fontSize = 24.sp,
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        if (me != null && players.size >= 2) {
            GameButton(
                text = stringResource(
                    if (me.isReady) {
                        R.string.multiplayer_not_ready_button
                    } else {
                        R.string.multiplayer_ready_button
                    },
                ),
                onClick = onToggleReady,
                modifier = Modifier.fillMaxWidth(0.65f),
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        GameButton(
            text = stringResource(R.string.multiplayer_leave_lobby),
            onClick = onLeaveLobby,
            modifier = Modifier.fillMaxWidth(0.65f),
        )

        if (statusMessage.isNotBlank()) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = statusMessage, color = ColorRedDark, textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun PlayerRow(player: RoomPlayer) {
    Row(
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .padding(vertical = 4.dp)
            .background(ColorOrange2.copy(alpha = 0.35f), PlayerCardShape)
            .border(1.dp, ColorRedDark.copy(alpha = 0.5f), PlayerCardShape)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = player.name, color = ColorRedDark, fontSize = 18.sp)
        Text(
            text = if (player.isReady) "✓" else "✕",
            fontSize = 22.sp,
            color = ColorRedDark,
        )
    }
}

@Composable
private fun CreateLobbyDialog(
    selectedMatchSize: Int,
    selectedCellCount: Int,
    onMatchSizeChange: (Int) -> Unit,
    onCellCountChange: (Int) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .background(ColorOrange2.copy(alpha = 0.92f), RoundedCornerShape(12.dp))
                .border(2.dp, ColorRedDark, RoundedCornerShape(12.dp))
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = stringResource(R.string.multiplayer_create_lobby),
                color = ColorRedDark,
                fontSize = 22.sp,
            )
            Text(text = stringResource(R.string.multiplayer_difficulty_label), color = ColorRedDark)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                DifficultyChip(stringResource(R.string.difficulty_pairs), 2, selectedMatchSize, onMatchSizeChange)
                DifficultyChip(stringResource(R.string.difficulty_trios), 3, selectedMatchSize, onMatchSizeChange)
                DifficultyChip(stringResource(R.string.difficulty_quartets), 4, selectedMatchSize, onMatchSizeChange)
            }
            Text(text = stringResource(R.string.multiplayer_field_size_label), color = ColorRedDark)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(18, 24, 36, 60).forEach { size ->
                    CellCountChip(size.toString(), size, selectedCellCount, onCellCountChange)
                }
            }
            GameButton(text = stringResource(R.string.multiplayer_create), onClick = onConfirm)
            GameButton(text = stringResource(R.string.button_close), onClick = onDismiss)
        }
    }
}

@Composable
private fun DifficultyChip(
    label: String,
    matchSize: Int,
    selected: Int,
    onSelect: (Int) -> Unit,
) {
    val selectedModifier = if (selected == matchSize) {
        Modifier.border(3.dp, ColorRedDark, RoundedCornerShape(8.dp))
    } else {
        Modifier.border(1.dp, ColorRedDark, RoundedCornerShape(8.dp))
    }
    Text(
        text = label,
        color = ColorRedDark,
        modifier = selectedModifier
            .clickable { onSelect(matchSize) }
            .padding(horizontal = 10.dp, vertical = 6.dp),
        fontSize = 14.sp,
    )
}

@Composable
private fun CellCountChip(
    label: String,
    cellCount: Int,
    selected: Int,
    onSelect: (Int) -> Unit,
) {
    val selectedModifier = if (selected == cellCount) {
        Modifier.border(3.dp, ColorRedDark, RoundedCornerShape(8.dp))
    } else {
        Modifier.border(1.dp, ColorRedDark, RoundedCornerShape(8.dp))
    }
    Text(
        text = label,
        color = ColorRedDark,
        modifier = selectedModifier
            .clickable { onSelect(cellCount) }
            .padding(horizontal = 10.dp, vertical = 6.dp),
        fontSize = 14.sp,
    )
}
