package io.dontsayboj.spindler.nav

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FamilyRestroom
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.dontsayboj.spindler.ui.DashboardScreen
import io.dontsayboj.spindler.ui.DetailsScreen
import io.dontsayboj.spindler.ui.FamilyScreen
import io.dontsayboj.spindler.ui.IndividualScreen

@Composable
fun SpindlerNavHost(navHostController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navHostController,
        startDestination = Routes.Dashboard
    ) {
        composable<Routes.Dashboard> { DashboardScreen(navHostController) }
        formGraph(navHostController)
    }
}

@Composable
fun DashboardBottomNavHost(
    navHostController: NavHostController,
    bottomNavHostController: NavHostController,
    innerPadding: PaddingValues,
) {
    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = bottomNavHostController,
        startDestination = DashboardBottomNavItem.Individual.route
    ) {
        composable(DashboardBottomNavItem.Individual.route) { IndividualScreen(innerPadding, navHostController) }
        composable(DashboardBottomNavItem.Family.route) { FamilyScreen(innerPadding, navHostController) }
    }
}

enum class DashboardBottomNavItem(
    val title: String,
    val route: String,
    val icon: ImageVector,
) {
    Individual(
        "Individual",
        "individual",
        Icons.Rounded.Person
    ),
    Family(
        "Family",
        "family",
        Icons.Rounded.FamilyRestroom
    )
}

fun NavGraphBuilder.formGraph(navHostController: NavHostController) {
    composable<Routes.Details> {
        DetailsScreen(id = it.id)
    }
}