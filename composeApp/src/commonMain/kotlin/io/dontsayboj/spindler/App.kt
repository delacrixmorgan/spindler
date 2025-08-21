package io.dontsayboj.spindler

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.dontsayboj.spindler.domain.model.Individual
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        val scope = rememberCoroutineScope()
        val individuals = mutableStateOf(emptyList<Individual>())
        scope.launch {
            val path = "files/sample.ged"
            val (_, index) = Gedcom.parseFile(path)
            individuals.value = index.individuals.values.toList()
        }

        Scaffold { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = innerPadding.calculateTopPadding()),
            ) {
                LazyColumn(state = rememberLazyListState()) {
                    items(count = individuals.value.size, key = { individuals.value[it].id }) { index ->
                        val individual = individuals.value[index]
                        ListItem(
                            overlineContent = { Text(individual.id) },
                            headlineContent = { Text(individual.names.joinToString(" ") { it.text }) },
//                            supportingContent = { Text(individual.lastName) },
                            trailingContent = {
                                Column(horizontalAlignment = Alignment.End) {
                                    individual.birthDate?.let { Text(it) }
                                    individual.birthPlace?.let { Text(it) }
                                }
                            }
//                            trailingContent = { Text(individual.sex.toString()) }
                        )
                    }
                }
            }
        }
    }
}