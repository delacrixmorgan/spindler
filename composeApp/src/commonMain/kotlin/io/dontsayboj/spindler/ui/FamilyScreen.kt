package io.dontsayboj.spindler.ui

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import io.dontsayboj.spindler.Gedcom
import io.dontsayboj.spindler.data.utils.DateParsing
import io.dontsayboj.spindler.domain.model.Family
import io.dontsayboj.spindler.domain.model.Individual
import kotlinx.coroutines.launch

@Composable
fun FamilyScreen(
    innerPadding: PaddingValues,
    navHostController: NavHostController
) {
    val scope = rememberCoroutineScope()
    val individuals = mutableStateOf(emptyList<Individual>())
    val families = mutableStateOf(emptyList<Family>())
    scope.launch {
        val path = "files/sample.ged"
        val (_, index) = Gedcom.parseFile(path)
        individuals.value = index.individuals.values.toList()
        families.value = index.families.values.toList()
    }
    Box(
        Modifier
            .fillMaxSize()
            .padding(innerPadding),
    ) {
        LazyColumn(state = rememberLazyListState()) {
            items(count = families.value.size, key = { families.value[it].id }) { index ->
                val family = families.value[index]
                val husband = individuals.value.find { it.id == family.husbandID }
                val wife = individuals.value.find { it.id == family.wifeID }
                ListItem(
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