package makza.afonsky.searchpair.ui.game

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import makza.afonsky.searchpair.R
import makza.afonsky.searchpair.audio.GameSound
import makza.afonsky.searchpair.audio.SoundManager
import makza.afonsky.searchpair.data.DifficultyBackground
import makza.afonsky.searchpair.data.DifficultyPage
import makza.afonsky.searchpair.data.HealthKitTier
import makza.afonsky.searchpair.game.GameEvent
import makza.afonsky.searchpair.game.GamePhase
import makza.afonsky.searchpair.game.GameViewModel
import makza.afonsky.searchpair.ui.components.CardGrid
import makza.afonsky.searchpair.ui.components.GameButton
import makza.afonsky.searchpair.ui.components.HealthBar
import makza.afonsky.searchpair.ui.components.HealthKitIcon
import makza.afonsky.searchpair.ui.components.WinLogoFlash
import makza.afonsky.searchpair.ui.theme.ColorGold
import makza.afonsky.searchpair.ui.theme.ColorRedDark

@Composable
fun GameScreen(
    viewModel: GameViewModel,
    soundManager: SoundManager,
    onNavigateToMenu: () -> Unit,
    onNavigateToLevel: (Int) -> Unit,
    onNavigateToBonus: () -> Unit,
) {
    val state by viewModel.uiState.collectAsState()
    val shakeOffset = remember { Animatable(0f) }

    LaunchedEffect(state.phase) {
        if (state.phase == GamePhase.DEFEAT) {
            repeat(6) {
                shakeOffset.animateTo(12f, tween(50))
                shakeOffset.animateTo(-12f, tween(50))
            }
            shakeOffset.animateTo(0f, tween(50))
        }
    }

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                GameEvent.NavigateToBonus -> onNavigateToBonus()
                is GameEvent.NavigateToLevel -> onNavigateToLevel(event.level)
            }
        }
    }

    BackHandler {
        if (state.phase == GamePhase.PLAYING || state.isWon) {
            soundManager.play(GameSound.DROP)
            onNavigateToMenu()
        }
    }

    val kitDrawable = when (state.config.healthKitTier) {
        HealthKitTier.SMALL -> R.drawable.restorehealth
        HealthKitTier.MEDIUM -> R.drawable.restorehealth2
        HealthKitTier.BIG -> R.drawable.restorehealth3
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(DifficultyBackground.forPage(state.config.difficultyPage)),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
                .graphicsLayer {
                    translationX = if (state.phase == GamePhase.DEFEAT) shakeOffset.value else 0f
                },
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = levelTitle(state.config.level, state.config.difficultyPage),
                style = androidx.compose.material3.MaterialTheme.typography.titleLarge.copy(
                    color = ColorGold,
                ),
                textAlign = TextAlign.Center,
                maxLines = 1,
            )

            Spacer(modifier = Modifier.height(4.dp))

            HealthBar(
                health = state.health,
                healthMax = state.config.healthMax,
                onCheatTap = viewModel::onHealthBarCheatTap,
                modifier = Modifier.fillMaxWidth(0.92f),
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 36.dp, max = 44.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                repeat(state.availableKits) {
                    HealthKitIcon(
                        drawableRes = kitDrawable,
                        onClick = viewModel::onHealthKitClick,
                        modifier = Modifier.padding(horizontal = 2.dp),
                        size = 34.dp,
                    )
                }
            }

            CardGrid(
                config = state.config,
                cards = state.cards,
                flippingCardId = state.flippingCardId,
                flipDurationMs = state.flipDurationMs,
                enabled = state.phase == GamePhase.PLAYING && !state.isInputBlocked,
                onCardClick = viewModel::onCardClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            )
        }

        // Temporary debug: skip to win
        if (!state.isWon && state.phase != GamePhase.DEFEAT) {
            GameButton(
                text = "WIN",
                onClick = viewModel::debugForceWin,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp),
            )
        }

        if (state.isWon) {
            WinOverlay(
                isFinalLevel = state.config.isFinalLevel,
                onNextClick = viewModel::onNextLevelClick,
            )
        }

        if (state.phase == GamePhase.DEFEAT) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(ColorRedDark.copy(alpha = 0.35f)),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(R.string.defeat_message),
                    style = androidx.compose.material3.MaterialTheme.typography.headlineMedium,
                )
            }
        }
    }
}

@Composable
private fun WinOverlay(
    isFinalLevel: Boolean,
    onNextClick: () -> Unit,
) {
    var showControls by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        if (!showControls) {
            WinLogoFlash(
                modifier = Modifier.fillMaxWidth(0.85f),
                onFinished = { showControls = true },
            )
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Image(
                    painter = painterResource(R.drawable.level_complite),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(0.75f),
                    contentScale = ContentScale.Fit,
                )

                GameButton(
                    text = if (isFinalLevel) {
                        "Cheers!"
                    } else {
                        stringResource(R.string.button_next_level)
                    },
                    onClick = onNextClick,
                )
            }
        }
    }
}

@Composable
private fun levelTitle(globalLevel: Int, page: DifficultyPage): String {
    val position = DifficultyPage.positionOnPage(globalLevel)
    val difficulty = when (page) {
        DifficultyPage.PAIRS -> stringResource(R.string.difficulty_pairs)
        DifficultyPage.TRIOS -> stringResource(R.string.difficulty_trios)
        DifficultyPage.QUARTETS -> stringResource(R.string.difficulty_quartets)
    }
    return "$difficulty — ${stringResource(R.string.level_number, position)}"
}
