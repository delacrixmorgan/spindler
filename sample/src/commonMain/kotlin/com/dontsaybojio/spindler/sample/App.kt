package com.dontsaybojio.spindler.sample

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.dontsaybojio.spindler.sample.data.SpindlerRepository
import com.dontsaybojio.spindler.sample.nav.SpindlerNavHost

@Composable
fun App() {
    MaterialTheme {
        LaunchedEffect(Unit) {
            SpindlerRepository.getLocalData()
        }
        SpindlerNavHost()
    }
}