package io.dontsayboj.spindler.ui.family

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
        content = {

        }
    )
}