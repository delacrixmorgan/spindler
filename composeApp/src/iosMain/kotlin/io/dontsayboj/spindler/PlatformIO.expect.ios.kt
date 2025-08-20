package io.dontsayboj.spindler

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.stringWithContentsOfFile

actual object PlatformIO {
    @OptIn(ExperimentalForeignApi::class)
    actual fun readText(path: String): String {
        val ns = NSString.stringWithContentsOfFile(path, NSUTF8StringEncoding, null)
        return ns ?: error("Cannot read file: $path")
    }
}