package makza.afonsky.searchpair.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import makza.afonsky.searchpair.audio.SoundManager
import makza.afonsky.searchpair.data.GameRepository
import makza.afonsky.searchpair.game.GameViewModel
import makza.afonsky.searchpair.multiplayer.MultiplayerGameViewModel
import makza.afonsky.searchpair.multiplayer.MultiplayerLobbyViewModel
import makza.afonsky.searchpair.multiplayer.MultiplayerRepository
import makza.afonsky.searchpair.ui.bonus.BonusScreen
import makza.afonsky.searchpair.ui.game.GameScreen
import makza.afonsky.searchpair.ui.menu.MenuScreen
import makza.afonsky.searchpair.ui.menu.MenuViewModel
import makza.afonsky.searchpair.ui.multiplayer.MultiplayerGameScreen
import makza.afonsky.searchpair.ui.multiplayer.MultiplayerLobbyScreen
import makza.afonsky.searchpair.ui.splash.SplashScreen

@Composable
fun FindAPairNavHost(
    navController: NavHostController,
    soundManager: SoundManager,
    onExitApp: () -> Unit,
) {
    val context = LocalContext.current
    val repository = remember { GameRepository(context) }
    val multiplayerRepository = remember {
        MultiplayerRepository(context.applicationContext)
    }

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH,
    ) {
        composable(Routes.SPLASH) {
            SplashScreen(
                onFinished = {
                    navController.navigate(Routes.MENU) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                },
            )
        }

        composable(Routes.MENU) {
            val menuViewModel: MenuViewModel = viewModel(
                factory = MenuViewModel.Factory(repository, soundManager),
            )

            MenuScreen(
                viewModel = menuViewModel,
                onStartLevel = { level ->
                    navController.navigate(Routes.game(level))
                },
                onMultiplayer = {
                    navController.navigate(Routes.MULTIPLAYER_LOBBY)
                },
                onExitApp = onExitApp,
            )
        }

        composable(Routes.MULTIPLAYER_LOBBY) {
            val lobbyViewModel: MultiplayerLobbyViewModel = viewModel(
                factory = MultiplayerLobbyViewModel.Factory(
                    context.applicationContext as android.app.Application,
                    multiplayerRepository,
                ),
            )
            MultiplayerLobbyScreen(
                viewModel = lobbyViewModel,
                onNavigateToGame = {
                    navController.navigate(Routes.MULTIPLAYER_GAME)
                },
                onBack = {
                    navController.popBackStack()
                },
            )
        }

        composable(Routes.MULTIPLAYER_GAME) {
            val gameViewModel: MultiplayerGameViewModel = viewModel(
                factory = MultiplayerGameViewModel.Factory(
                    context.applicationContext as android.app.Application,
                    multiplayerRepository,
                ),
            )
            MultiplayerGameScreen(
                viewModel = gameViewModel,
                onNavigateToLobby = {
                    navController.popBackStack(Routes.MULTIPLAYER_LOBBY, inclusive = false)
                },
            )
        }

        composable(
            route = Routes.GAME,
            arguments = listOf(
                navArgument("level") { type = NavType.IntType },
            ),
        ) { backStackEntry ->
            val level = backStackEntry.arguments?.getInt("level") ?: 1
            val gameViewModel: GameViewModel = viewModel(
                key = "game_$level",
                factory = GameViewModel.Factory(level, repository, soundManager),
            )

            GameScreen(
                viewModel = gameViewModel,
                soundManager = soundManager,
                onNavigateToMenu = {
                    navController.popBackStack(Routes.MENU, inclusive = false)
                },
                onNavigateToLevel = { nextLevel ->
                    navController.navigate(Routes.game(nextLevel)) {
                        popUpTo(Routes.MENU)
                    }
                },
                onNavigateToBonus = {
                    navController.navigate(Routes.BONUS) {
                        popUpTo(Routes.MENU)
                    }
                },
            )
        }

        composable(Routes.BONUS) {
            BonusScreen(
                soundManager = soundManager,
                onNavigateToMenu = {
                    navController.popBackStack(Routes.MENU, inclusive = false)
                },
            )
        }
    }
}
