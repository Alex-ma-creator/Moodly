package com.moodly.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.moodly.app.ui.screens.*
import com.moodly.app.viewmodel.MoodViewModel

@Composable
fun MoodlyNavGraph(
    navController: NavHostController,
    viewModel: MoodViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {

        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = viewModel,
                onRegisterClick = { navController.navigate(Screen.Register.route) },
                onDayClick = { entryId -> navController.navigate(Screen.Detail.createRoute(entryId)) }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                viewModel = viewModel,
                onSaved = { navController.popBackStack() }
            )
        }

        composable(Screen.History.route) {
            HistoryScreen(
                viewModel = viewModel,
                onEntryClick = { entryId -> navController.navigate(Screen.Detail.createRoute(entryId)) }
            )
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("entryId") { type = NavType.LongType })
        ) { backStackEntry ->
            val entryId = backStackEntry.arguments?.getLong("entryId") ?: return@composable
            DetailScreen(
                entryId = entryId,
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Resources.route) {
            ResourcesScreen()
        }
    }
}
