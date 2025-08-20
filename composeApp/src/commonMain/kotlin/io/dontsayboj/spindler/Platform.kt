package io.dontsayboj.spindler

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform