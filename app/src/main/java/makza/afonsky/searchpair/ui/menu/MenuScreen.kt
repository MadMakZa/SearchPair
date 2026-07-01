package makza.afonsky.searchpair.ui.menu

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import makza.afonsky.searchpair.data.DifficultyBackground
import makza.afonsky.searchpair.R
import makza.afonsky.searchpair.ui.theme.ColorRedDark
import makza.afonsky.searchpair.data.DifficultyPage
import makza.afonsky.searchpair.ui.components.ChestDialog
import makza.afonsky.searchpair.ui.components.ConfirmDialog
import makza.afonsky.searchpair.ui.components.DifficultyHorizontalPager
import makza.afonsky.searchpair.ui.components.DifficultyPagerIndicator
import makza.afonsky.searchpair.ui.components.GameButton
import makza.afonsky.searchpair.ui.components.LevelButton
import makza.afonsky.searchpair.ui.components.rememberDifficultyPagerState

@Composable
fun MenuScreen(
    viewModel: MenuViewModel,
    onStartLevel: (Int) -> Unit,
    onExitApp: () -> Unit,
) {
    val state by viewModel.uiState.collectAsState()
    val pagerState = rememberDifficultyPagerState(
        initialPage = state.selectedDifficultyPage,
    )
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    LaunchedEffect(state.selectedDifficultyPage) {
        if (pagerState.currentPage != state.selectedDifficultyPage) {
            pagerState.scrollToPage(state.selectedDifficultyPage)
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        if (pagerState.currentPage != state.selectedDifficultyPage) {
            viewModel.onDifficultyPageChanged(pagerState.currentPage)
        }
    }

    BackHandler {
        viewModel.onBackRequest()
    }

    if (state.showResetDialog) {
        ConfirmDialog(
            message = stringResource(R.string.dialog_reset_progress),
            onConfirm = {
                viewModel.onConfirmReset()
                onStartLevel(1)
            },
            onDismiss = viewModel::onDismissReset,
        )
    }

    if (state.showExitDialog) {
        ConfirmDialog(
            message = stringResource(R.string.exit_game),
            onConfirm = onExitApp,
            onDismiss = viewModel::onDismissExit,
        )
    }

    if (state.showChestDialog) {
        ChestDialog(
            kitCounts = state.kitCounts,
            onDismiss = viewModel::onDismissChest,
        )
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
                .padding(horizontal = 10.dp, vertical = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(R.drawable.logo_name_game),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 72.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    ) { viewModel.onLogoCheatTap() },
                contentScale = ContentScale.FillWidth,
            )

            Spacer(modifier = Modifier.height(6.dp))

            GameButton(
                text = stringResource(R.string.button_new_game),
                onClick = {
                    viewModel.onNewGameClick()
                    if (state.unlockedLevel < 2) {
                        onStartLevel(1)
                    }
                },
                modifier = Modifier.fillMaxWidth(0.55f),
            )

            Spacer(modifier = Modifier.height(6.dp))

            Image(
                painter = painterResource(R.drawable.chestclosed),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .clickable { viewModel.onOpenChest() },
                contentScale = ContentScale.Fit,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = pageTitle(DifficultyPage.entries[pagerState.currentPage]),
                style = androidx.compose.material3.MaterialTheme.typography.titleMedium.copy(
                    fontSize = 50.sp,
                    color = ColorRedDark,
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    ) { viewModel.onOtherMenuTap() },
            )

            Spacer(modifier = Modifier.height(4.dp))

            Image(
                painter = painterResource(R.drawable.select_level_text),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 120.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    ) { viewModel.onLevelLabelCheatTap() },
                contentScale = ContentScale.Fit,
            )

            Spacer(modifier = Modifier.height(4.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) {
                DifficultyHorizontalPager(
                    pagerState = pagerState,
                    isPageUnlocked = { index ->
                        DifficultyPage.isPageUnlocked(
                            DifficultyPage.entries[index],
                            state.unlockedLevel,
                        )
                    },
                    modifier = Modifier.fillMaxSize(),
                ) { page ->
                    LevelPageGrid(
                        page = page,
                        unlockedLevel = state.unlockedLevel,
                        onLevelClick = { level ->
                            viewModel.onLevelClick(level)
                            onStartLevel(level)
                        },
                    )
                }
            }

            DifficultyPagerIndicator(
                pageCount = DifficultyPage.entries.size,
                currentPage = pagerState.currentPage,
                unlockedPages = state.unlockedPages,
                onPageClick = { pageIndex ->
                    viewModel.onPagerDotClick()
                    scope.launch { pagerState.animateScrollToPage(pageIndex) }
                },
                modifier = Modifier.padding(vertical = 4.dp),
            )
        }
    }
}

@Composable
private fun LevelPageGrid(
    page: DifficultyPage,
    unlockedLevel: Int,
    onLevelClick: (Int) -> Unit,
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        val spacing = 5.dp
        val columns = DifficultyPage.GRID_COLUMNS
        val rows = DifficultyPage.GRID_ROWS

        val cellFromWidth = (maxWidth - spacing * (columns - 1)) / columns
        val cellFromHeight = (maxHeight - spacing * (rows - 1)) / rows
        val cellSize = minOf(cellFromWidth, cellFromHeight)

        Column(
            verticalArrangement = Arrangement.spacedBy(spacing),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            repeat(rows) { row ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(spacing),
                ) {
                    repeat(columns) { col ->
                        val positionOnPage = row * columns + col + 1
                        val globalLevel = DifficultyPage.globalLevel(page, positionOnPage)
                        LevelButton(
                            text = positionOnPage.toString(),
                            unlocked = globalLevel <= unlockedLevel,
                            onClick = { onLevelClick(globalLevel) },
                            modifier = Modifier.size(cellSize),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun pageTitle(page: DifficultyPage): String = when (page) {
    DifficultyPage.PAIRS -> stringResource(R.string.difficulty_pairs)
    DifficultyPage.TRIOS -> stringResource(R.string.difficulty_trios)
    DifficultyPage.QUARTETS -> stringResource(R.string.difficulty_quartets)
}
