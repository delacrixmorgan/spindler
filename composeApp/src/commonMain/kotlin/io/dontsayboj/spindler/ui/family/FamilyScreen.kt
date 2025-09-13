package io.dontsayboj.spindler.ui.family

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import io.dontsayboj.spindler.data.SpindlerRepository
import io.dontsayboj.spindler.data.utils.DateParsing
import io.dontsayboj.spindler.nav.Routes

@Composable
fun FamilyScreen(
    innerPadding: PaddingValues,
    navHostController: NavHostController
) {
    val families by SpindlerRepository.families.collectAsStateWithLifecycle()
    val individuals by SpindlerRepository.individuals.collectAsStateWithLifecycle()
    Box(
        Modifier
            .fillMaxSize()
            .padding(innerPadding),
    ) {
        LazyColumn(state = rememberLazyListState()) {
            items(count = families.size, key = { families[it].id }) { index ->
                val family = families[index]
                val husband = individuals.find { it.id == family.husbandID }
                val wife = individuals.find { it.id == family.wifeID }
                ListItem(
                    modifier = Modifier.clickable { navHostController.navigate(Routes.FamilyDetail(family.id)) },
                    overlineContent = { Text(family.id) },
                    headlineContent = { Text(listOfNotNull(husband?.names?.firstOrNull()?.text, wife?.names?.firstOrNull()?.text).joinToString(" + ")) },
                    trailingContent = {
                        Column(horizontalAlignment = Alignment.End) {
                            family.marriageDate?.let { Text(DateParsing.tryParseDate(it)?.toString() ?: it) }
                            family.marriagePlace?.let { Text(it) }
                        }
                    }
                )
            }
        }
    }
}