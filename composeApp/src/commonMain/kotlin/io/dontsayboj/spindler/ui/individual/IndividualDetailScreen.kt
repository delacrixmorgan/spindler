package io.dontsayboj.spindler.ui.individual

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavHostController
import io.dontsayboj.spindler.data.SpindlerRepository
import io.dontsayboj.spindler.domain.enum.Tag
import io.dontsayboj.spindler.nav.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IndividualDetailScreen(
    id: String,
    navHostController: NavHostController
) {
    val individual = SpindlerRepository.getIndividual(id)
    if (individual == null) {
        navHostController.navigateUp()
        return
    }
    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = {
                    Text(
                        individual.formattedName,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navHostController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Go back",
                        )
                    }
                },
            )
        },
        content = { innerPadding ->
            LazyColumn(Modifier.padding(innerPadding)) {
                item {
                    ListItem(headlineContent = { Text("Node Tags (${individual.nodes.size})") }, supportingContent = {
                        Text(individual.nodes.joinToString("\n") {
                            if (it.tag == Tag.OBJECT) {
                                Tag.OBJECT + ": " + it.value
                            } else {
                                it.tag
                            }
                        })
                    })
                }
                item { ListItem(headlineContent = { Text("Names Nodes") }, supportingContent = { Text(individual.names.joinToString(", ")) }) }
                item { HorizontalDivider() }

                item { ListItem(headlineContent = { Text("Given Names") }, supportingContent = { Text(individual.givenNames.joinToString(", ").ifEmpty { "N/A" }) }) }
                item { ListItem(headlineContent = { Text("Surnames") }, supportingContent = { Text(individual.surnames.joinToString(", ")) }) }
                item { ListItem(headlineContent = { Text("Sex") }, supportingContent = { Text(individual.sex.name) }) }
                item { ListItem(headlineContent = { Text("Birth Date") }, supportingContent = { Text(individual.birthDateFormatted) }) }
                item { ListItem(headlineContent = { Text("Birth Place") }, supportingContent = { Text(individual.birthPlace ?: "N/A") }) }
                item { ListItem(headlineContent = { Text("Death Date") }, supportingContent = { Text(individual.deathDateFormatted) }) }
                item { HorizontalDivider() }

                item { ListItem(headlineContent = { Text("Education") }, supportingContent = { Text(individual.education ?: "N/A") }) }
                item { ListItem(headlineContent = { Text("Religion") }, supportingContent = { Text(individual.religion ?: "N/A") }) }
                item { HorizontalDivider() }

                item {
                    individual.familyIDAsChild?.let { familyID ->
                        ListItem(
                            modifier = Modifier.clickable { navHostController.navigate(Routes.FamilyDetail(id = familyID)) },
                            headlineContent = { Text("FamilyID as Child") },
                            supportingContent = { Text(familyID) },
                            trailingContent = { Icon(Icons.Rounded.ChevronRight, null) }
                        )
                    } ?: ListItem(
                        headlineContent = { Text("FamilyID as Child") },
                        supportingContent = { Text("N/A") }
                    )
                }
                item {
                    individual.familyIDAsSpouse?.let { familyID ->
                        ListItem(
                            modifier = Modifier.clickable { navHostController.navigate(Routes.FamilyDetail(id = familyID)) },
                            headlineContent = { Text("FamilyID as Spouse") },
                            supportingContent = { Text(familyID) },
                            trailingContent = { Icon(Icons.Rounded.ChevronRight, null) }
                        )
                    } ?: ListItem(
                        headlineContent = { Text("FamilyID as Spouse") },
                        supportingContent = { Text("N/A") }
                    )
                }
                item { HorizontalDivider() }

                if (individual.macFamilyTreeID?.isNotEmpty() == true) {
                    item { ListItem(headlineContent = { Text("MacFamilyTree ID (_FID)") }, supportingContent = { Text(individual.macFamilyTreeID ?: "N/A") }) }
                    item { HorizontalDivider() }
                }

                item { ListItem(headlineContent = { Text("Change Date") }, supportingContent = { Text(individual.changeDate ?: "N/A") }) }
                item { ListItem(headlineContent = { Text("Creation Date") }, supportingContent = { Text(individual.creationDate ?: "N/A") }) }
            }
        }
    )
}