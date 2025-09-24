package io.dontsayboj.spindler.sample

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import io.dontsayboj.spindler.sample.data.SpindlerRepository
import io.dontsayboj.spindler.sample.nav.SpindlerNavHost
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    SpindlerNavHost()
    LaunchedEffect(Unit) {
        SpindlerRepository.getLocalData()
    }
}