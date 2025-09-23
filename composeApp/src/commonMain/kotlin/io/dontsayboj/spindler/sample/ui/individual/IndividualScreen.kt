package io.dontsayboj.spindler.sample.ui.individual

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
import io.dontsayboj.spindler.sample.data.SpindlerRepository
import io.dontsayboj.spindler.sample.nav.Routes

@Composable
fun IndividualScreen(
    innerPadding: PaddingValues,
    navHostController: NavHostController
) {
    val individuals by SpindlerRepository.individuals.collectAsStateWithLifecycle()
    Box(
        Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        LazyColumn(state = rememberLazyListState()) {
            items(count = individuals.size, key = { individuals[it].id }) { index ->
                val individual = individuals[index]
                ListItem(
                    modifier = Modifier.clickable { navHostController.navigate(Routes.IndividualDetail(individual.id)) },
                    overlineContent = { Text(individual.id) },
                    headlineContent = { Text(individual.formattedName) },
                    trailingContent = {
                        Column(horizontalAlignment = Alignment.End) {
                            Text(individual.birthDateFormatted)
                            individual.birthPlace?.let { Text(it) }
                        }
                    }
                )
            }
        }
    }
}