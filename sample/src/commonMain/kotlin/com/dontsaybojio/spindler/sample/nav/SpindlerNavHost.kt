package com.dontsaybojio.spindler.sample.nav

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
import androidx.navigation.toRoute
import com.dontsaybojio.spindler.sample.ui.DashboardScreen
import com.dontsaybojio.spindler.sample.ui.family.FamilyDetailScreen
import com.dontsaybojio.spindler.sample.ui.family.FamilyScreen
import com.dontsaybojio.spindler.sample.ui.individual.IndividualDetailScreen
import com.dontsaybojio.spindler.sample.ui.individual.IndividualScreen

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
    composable<Routes.IndividualDetail> {
        IndividualDetailScreen(id = it.toRoute<Routes.IndividualDetail>().id, navHostController = navHostController)
    }

    composable<Routes.FamilyDetail> {
        FamilyDetailScreen(id = it.toRoute<Routes.FamilyDetail>().id, navHostController = navHostController)
    }
}