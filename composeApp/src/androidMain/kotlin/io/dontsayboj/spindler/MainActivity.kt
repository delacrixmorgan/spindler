package io.dontsayboj.spindler

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import io.dontsayboj.spindler.data.SpindlerRepository

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                App()
            }
            LaunchedEffect(Unit) {
                SpindlerRepository.getLocalData()
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}