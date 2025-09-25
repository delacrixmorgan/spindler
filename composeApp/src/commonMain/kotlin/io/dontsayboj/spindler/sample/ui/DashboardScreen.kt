package io.dontsayboj.spindler.sample.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.dontsayboj.spindler.sample.nav.DashboardBottomNavHost
import io.dontsayboj.spindler.sample.nav.DashboardBottomNavItem

@Composable
fun DashboardScreen(
    navHostController: NavHostController,
    bottomNavHostController: NavHostController = rememberNavController(),
) {
    Scaffold(bottomBar = { BottomNavigationBar(bottomNavHostController) }) { innerPadding ->
        DashboardBottomNavHost(navHostController, bottomNavHostController, innerPadding)
    }
}

@Composable
private fun BottomNavigationBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        DashboardBottomNavItem.entries.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.title) },
                label = { Text(screen.title) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}