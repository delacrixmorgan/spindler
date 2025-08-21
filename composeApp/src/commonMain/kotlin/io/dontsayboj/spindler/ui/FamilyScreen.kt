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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.dontsayboj.spindler.data.utils.DateParsing
import io.dontsayboj.spindler.domain.model.Family
import io.dontsayboj.spindler.domain.model.Individual

@Composable
fun FamilyScreen(
    innerPadding: PaddingValues,
    individuals: List<Individual>,
    families: List<Family>
) {
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