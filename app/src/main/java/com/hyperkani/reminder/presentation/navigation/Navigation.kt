package com.hyperkani.reminder.presentation.navigation

import com.hyperkani.reminder.presentation.screens.addReminder.AddReminderScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.hyperkani.reminder.presentation.screens.mainScreen.MainScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(
    navController = navController,
    startDestination = Screens.MAIN_SCREEN.route,
    ) {
        composable(Screens.MAIN_SCREEN.route) { MainScreen(navHostController = navController) }
        composable(
            route = "${Screens.ADD_REMINDER_SCREEN.route}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getLong("id")
            AddReminderScreen(navController, itemId ?: -1)
        }
    }
}
