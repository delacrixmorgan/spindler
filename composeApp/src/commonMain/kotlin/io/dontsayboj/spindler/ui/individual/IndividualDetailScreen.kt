package io.dontsayboj.spindler.ui.individual

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavHostController
import io.dontsayboj.spindler.data.SpindlerRepository

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
        content = {

        }
    )
}