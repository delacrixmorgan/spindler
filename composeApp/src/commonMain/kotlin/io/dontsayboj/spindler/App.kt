package io.dontsayboj.spindler

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FamilyRestroom
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.dontsayboj.spindler.domain.model.Family
import io.dontsayboj.spindler.domain.model.Individual
import io.dontsayboj.spindler.ui.FamilyScreen
import io.dontsayboj.spindler.ui.IndividualScreen
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        val scope = rememberCoroutineScope()
        val individuals = mutableStateOf(emptyList<Individual>())
        val families = mutableStateOf(emptyList<Family>())
        scope.launch {
            val path = "files/sample.ged"
            val (_, index) = Gedcom.parseFile(path)
            individuals.value = index.individuals.values.toList()
            families.value = index.families.values.toList()
        }

        MaterialTheme {
            val navController = rememberNavController()
            Scaffold(bottomBar = { NavigationBar(navController) }) { innerPadding ->
                NavHost(navController = navController, startDestination = BottomNavItem.Individual.route) {
                    composable(BottomNavItem.Individual.route) { IndividualScreen(innerPadding, individuals.value) }
                    composable(BottomNavItem.Family.route) { FamilyScreen(innerPadding, individuals.value, families.value) }
                }
            }
        }
    }
}

@Composable
private fun NavigationBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        BottomNavItem.entries.forEach { screen ->
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

enum class BottomNavItem(
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