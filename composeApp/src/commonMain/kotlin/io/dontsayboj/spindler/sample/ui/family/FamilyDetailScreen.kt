package io.dontsayboj.spindler.sample.ui.family

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
import io.dontsayboj.spindler.domain.enum.Tag
import io.dontsayboj.spindler.sample.data.SpindlerRepository
import io.dontsayboj.spindler.sample.nav.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FamilyDetailScreen(
    id: String,
    navHostController: NavHostController
) {
    val family = SpindlerRepository.getFamily(id)
    if (family == null) {
        navHostController.navigateUp()
        return
    }
    val husband = SpindlerRepository.getIndividual(family.husbandID)
    val wife = SpindlerRepository.getIndividual(family.wifeID)
    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = {
                    Text(
                        listOfNotNull(husband?.names?.firstOrNull()?.text, wife?.names?.firstOrNull()?.text).joinToString(" + "),
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
                    ListItem(headlineContent = { Text("Node Tags (${family.nodes.size})") }, supportingContent = {
                        Text(family.nodes.joinToString("\n") {
                            if (it.tag == Tag.OBJECT) {
                                Tag.OBJECT + ": " + it.value
                            } else {
                                it.tag
                            }
                        })
                    })
                }
                item { HorizontalDivider() }

                item { ListItem(headlineContent = { Text("Marriage Date") }, supportingContent = { Text(family.marriageDateFormatted) }) }
                item { ListItem(headlineContent = { Text("Marriage Place") }, supportingContent = { Text(family.marriagePlace ?: "N/A") }) }
                husband?.let {
                    item {
                        ListItem(
                            modifier = Modifier.clickable { navHostController.navigate(Routes.IndividualDetail(id = husband.id)) },
                            headlineContent = { Text("Husband") },
                            supportingContent = { Text(husband.formattedName) },
                            trailingContent = { Icon(Icons.Rounded.ChevronRight, null) }
                        )
                    }
                }
                wife?.let {
                    item {
                        ListItem(
                            modifier = Modifier.clickable { navHostController.navigate(Routes.IndividualDetail(id = wife.id)) },
                            headlineContent = { Text("Wife") },
                            supportingContent = { Text(wife.formattedName) },
                            trailingContent = { Icon(Icons.Rounded.ChevronRight, null) }
                        )
                    }
                }
                item { HorizontalDivider() }

                items(family.childrenIDs.size) { index ->
                    val childIndividual = SpindlerRepository.getIndividual(family.childrenIDs[index])
                    ListItem(
                        modifier = Modifier.clickable { navHostController.navigate(Routes.IndividualDetail(id = childIndividual?.id!!)) },
                        headlineContent = { Text("Child #${index + 1} – ${childIndividual?.formattedName ?: "N/A"}") },
                        supportingContent = { Text("Birth Date: ${childIndividual?.birthDateFormatted}") },
                        trailingContent = { Icon(Icons.Rounded.ChevronRight, null) }
                    )
                }
                item { HorizontalDivider() }

                if (family.macFamilyTreeLabel?.isNotEmpty() == true) {
                    item { ListItem(headlineContent = { Text("MacFamilyTree Label (_LABL)") }, supportingContent = { Text(family.macFamilyTreeLabel ?: "N/A") }) }
                    item { HorizontalDivider() }
                }

                item { ListItem(headlineContent = { Text("Change Date") }, supportingContent = { Text(family.changeDate ?: "N/A") }) }
                item { ListItem(headlineContent = { Text("Creation Date") }, supportingContent = { Text(family.creationDate ?: "N/A") }) }
            }
        }
    )
}