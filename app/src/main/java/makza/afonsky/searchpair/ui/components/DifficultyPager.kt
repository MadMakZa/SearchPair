package makza.afonsky.searchpair.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import makza.afonsky.searchpair.data.DifficultyPage
import makza.afonsky.searchpair.ui.theme.ColorOrange2
import makza.afonsky.searchpair.ui.theme.ColorRedDark

@Composable
fun DifficultyPagerIndicator(
    pageCount: Int,
    currentPage: Int,
    unlockedPages: Set<Int>,
    onPageClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(pageCount) { index ->
            val isSelected = index == currentPage
            val isUnlocked = unlockedPages.contains(index)
            Box(
                modifier = Modifier
                    .size(if (isSelected) 14.dp else 10.dp)
                    .clip(CircleShape)
                    .background(
                        when {
                            isSelected -> ColorRedDark
                            isUnlocked -> ColorOrange2
                            else -> Color.Gray.copy(alpha = 0.4f)
                        },
                    )
                    .then(
                        if (isUnlocked) {
                            Modifier.clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() },
                                onClick = { onPageClick(index) },
                            )
                        } else {
                            Modifier
                        },
                    ),
            )
        }
    }
}

@Composable
fun rememberDifficultyPagerState(initialPage: Int = 0): PagerState =
    rememberPagerState(initialPage = initialPage) { DifficultyPage.entries.size }

@Composable
fun DifficultyHorizontalPager(
    pagerState: PagerState,
    isPageUnlocked: (Int) -> Boolean,
    modifier: Modifier = Modifier,
    content: @Composable (DifficultyPage) -> Unit,
) {
    val scope = rememberCoroutineScope()

    LaunchedEffect(pagerState.currentPage) {
        if (!isPageUnlocked(pagerState.currentPage)) {
            val fallback = (0 until DifficultyPage.entries.size)
                .lastOrNull(isPageUnlocked) ?: 0
            scope.launch { pagerState.animateScrollToPage(fallback) }
        }
    }

    HorizontalPager(
        state = pagerState,
        modifier = modifier,
        userScrollEnabled = true,
        pageSpacing = 12.dp,
    ) { pageIndex ->
        if (isPageUnlocked(pageIndex)) {
            content(DifficultyPage.entries[pageIndex])
        }
    }
}
