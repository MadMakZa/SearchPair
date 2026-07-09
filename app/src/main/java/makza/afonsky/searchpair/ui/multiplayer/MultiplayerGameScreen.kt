package makza.afonsky.searchpair.ui.multiplayer

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import makza.afonsky.searchpair.R
import makza.afonsky.searchpair.data.DifficultyBackground
import makza.afonsky.searchpair.multiplayer.MultiplayerGameEvent
import makza.afonsky.searchpair.multiplayer.MultiplayerGameViewModel
import makza.afonsky.searchpair.ui.components.ConfirmDialog
import makza.afonsky.searchpair.ui.components.GameButton
import makza.afonsky.searchpair.ui.components.MultiplayerCardGrid
import makza.afonsky.searchpair.ui.components.TugOfWarBar
import makza.afonsky.searchpair.multiplayer.matchSizeToDifficultyPage
import makza.afonsky.searchpair.ui.theme.ColorGold
import makza.afonsky.searchpair.ui.theme.ColorOrange2
import makza.afonsky.searchpair.ui.theme.ColorRedDark

@Composable
fun MultiplayerGameScreen(
    viewModel: MultiplayerGameViewModel,
    onNavigateToLobby: () -> Unit,
) {
    val state by viewModel.uiState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) {
                viewModel.onAppBackgrounded()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    BackHandler(enabled = !state.isGameFinished || state.isSearchGame) {
        if (state.isGameFinished && state.isSearchGame) {
            viewModel.onLeaveSearchGame()
        } else {
            viewModel.onBackPressed()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                MultiplayerGameEvent.NavigateToLobby -> onNavigateToLobby()
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(
                DifficultyBackground.forPage(matchSizeToDifficultyPage(state.matchSize)),
            ),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.multiplayer_game_title),
                fontSize = 28.sp,
                color = ColorRedDark,
            )

            Spacer(modifier = Modifier.height(8.dp))

            TugOfWarBar(
                leftName = state.myName,
                rightName = state.opponentName.ifBlank { "..." },
                leftScore = state.myScore,
                rightScore = state.opponentScore,
                totalGroups = state.totalGroups,
                turnSecondsLeft = state.turnSecondsLeft,
                timerOnLeft = state.isMyTurn,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(8.dp))

            MultiplayerCardGrid(
                cards = state.cards,
                gridColumns = state.gridColumns,
                gridRows = state.gridRows,
                enabled = state.isInputEnabled,
                onCardClick = viewModel::onCardClick,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            )
        }

        if (state.turnBanner.visible) {
            TurnChangeBanner(text = state.turnBanner.text)
        }

        if (state.isGameFinished) {
            if (state.isSearchGame) {
                SearchGameFinishedOverlay(
                    statusMessage = state.statusMessage,
                    scoreText = "${state.myScore} : ${state.opponentScore}",
                    onLeave = viewModel::onLeaveSearchGame,
                )
            } else {
                GameFinishedOverlay(
                    statusMessage = state.statusMessage,
                    scoreText = "${state.myScore} : ${state.opponentScore}",
                    myReady = state.myRematchReady,
                    opponentReady = state.opponentRematchReady,
                    opponentName = state.opponentName.ifBlank { "Opponent" },
                    countdownSeconds = state.countdownSeconds,
                    onPlayAgain = viewModel::onPlayAgain,
                    onBackToLobby = viewModel::onBackToLobby,
                )
            }
        } else if (state.countdownSeconds != null) {
            PreGameCountdownOverlay(
                seconds = state.countdownSeconds!!,
                isSearchGame = state.isSearchGame,
            )
        }
    }

    if (state.showSurrenderDialog) {
        ConfirmDialog(
            message = stringResource(R.string.multiplayer_surrender),
            confirmLabel = stringResource(R.string.button_ok),
            dismissLabel = stringResource(R.string.button_no),
            onConfirm = viewModel::onConfirmSurrender,
            onDismiss = viewModel::onDismissSurrenderDialog,
        )
    }
}

@Composable
private fun PreGameCountdownOverlay(seconds: Int, isSearchGame: Boolean) {
    val displayText = when {
        isSearchGame && seconds == 0 -> stringResource(R.string.multiplayer_play)
        isSearchGame -> seconds.toString()
        else -> stringResource(R.string.multiplayer_countdown, seconds)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xAA000000)),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = displayText,
            color = Color.White,
            fontSize = if (isSearchGame) 72.sp else 48.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun SearchGameFinishedOverlay(
    statusMessage: String,
    scoreText: String,
    onLeave: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xAA000000)),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = statusMessage,
                color = Color.White,
                fontSize = 32.sp,
                textAlign = TextAlign.Center,
            )
            Text(
                text = scoreText,
                color = Color.White,
                fontSize = 24.sp,
            )
            Spacer(modifier = Modifier.height(32.dp))
            GameButton(
                text = stringResource(R.string.multiplayer_leave),
                onClick = onLeave,
                modifier = Modifier.fillMaxWidth(0.6f),
            )
        }
    }
}

@Composable
private fun GameFinishedOverlay(
    statusMessage: String,
    scoreText: String,
    myReady: Boolean,
    opponentReady: Boolean,
    opponentName: String,
    countdownSeconds: Int?,
    onPlayAgain: () -> Unit,
    onBackToLobby: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xAA000000)),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = statusMessage,
                color = Color.White,
                fontSize = 32.sp,
                textAlign = TextAlign.Center,
            )
            Text(
                text = scoreText,
                color = Color.White,
                fontSize = 24.sp,
            )
            countdownSeconds?.let { seconds ->
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(R.string.multiplayer_countdown, seconds),
                    color = Color.White,
                    fontSize = 22.sp,
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = rematchReadyStatus(myReady, opponentReady, opponentName),
                color = ColorGold,
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .background(ColorOrange2.copy(alpha = 0.35f), RoundedCornerShape(10.dp))
                    .border(1.dp, ColorRedDark.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            )
            Spacer(modifier = Modifier.height(12.dp))
            GameButton(
                text = stringResource(R.string.multiplayer_play_again),
                onClick = onPlayAgain,
                enabled = !myReady,
                modifier = Modifier.fillMaxWidth(0.6f),
            )
            Spacer(modifier = Modifier.height(8.dp))
            GameButton(
                text = stringResource(R.string.multiplayer_back_lobby),
                onClick = onBackToLobby,
                modifier = Modifier.fillMaxWidth(0.6f),
            )
        }
    }
}

@Composable
private fun rematchReadyStatus(myReady: Boolean, opponentReady: Boolean, opponentName: String): String {
    val you = stringResource(
        if (myReady) R.string.multiplayer_ready_you else R.string.multiplayer_not_ready_you,
    )
    val them = stringResource(
        if (opponentReady) R.string.multiplayer_ready_opponent else R.string.multiplayer_not_ready_opponent,
        opponentName,
    )
    return "$you\n$them"
}

@Composable
private fun TurnChangeBanner(text: String) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        val density = LocalDensity.current
        val maxScreenWidthPx = with(density) { maxWidth.toPx() }
        var textWidthPx by remember(text) { mutableStateOf(0f) }
        val scale = remember(text) { Animatable(0f) }
        val alpha = remember(text) { Animatable(0f) }

        LaunchedEffect(text, textWidthPx) {
            if (textWidthPx <= 0f) return@LaunchedEffect
            scale.snapTo(0f)
            alpha.snapTo(0f)
            coroutineScope {
                launch { scale.animateTo(1f, tween(1100)) }
                launch { alpha.animateTo(1f, tween(1100)) }
            }
            alpha.animateTo(0f, tween(500))
        }

        val targetScale = if (textWidthPx > 0f) maxScreenWidthPx / textWidthPx else 1f

        Text(
            text = text,
            color = Color.White,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            onTextLayout = { textWidthPx = it.size.width.toFloat() },
            modifier = Modifier.graphicsLayer {
                val currentScale = (scale.value * targetScale).coerceAtLeast(0.01f)
                scaleX = currentScale
                scaleY = currentScale
                this.alpha = alpha.value
            },
        )
    }
}
