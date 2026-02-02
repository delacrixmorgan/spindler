package com.dontsaybojio.spindler.sample

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.dontsaybojio.spindler.sample.data.SpindlerRepository
import com.dontsaybojio.spindler.sample.nav.SpindlerNavHost
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    SpindlerNavHost()
    LaunchedEffect(Unit) {
        SpindlerRepository.getLocalData()
    }
}