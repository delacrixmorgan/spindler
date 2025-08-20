package io.dontsayboj.spindler

import java.io.File

actual object PlatformIO {
    actual fun readText(path: String): String = File(path).readText()
}