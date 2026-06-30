package makza.afonsky.searchpair.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import makza.afonsky.searchpair.R
import makza.afonsky.searchpair.data.DifficultyBackground
import makza.afonsky.searchpair.ui.theme.ColorPrimaryDark

@Composable
fun SplashScreen(
    onFinished: () -> Unit,
) {
    LaunchedEffect(Unit) {
        delay(750)
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorPrimaryDark),
    ) {
        Image(
            painter = painterResource(DifficultyBackground.menu),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )

        Image(
            painter = painterResource(R.drawable.splashscreenlogo),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(vertical = 32.dp),
            contentScale = ContentScale.Fit,
        )
    }
}
