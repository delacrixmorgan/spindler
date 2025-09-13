package io.dontsayboj.spindler.nav

import kotlinx.serialization.Serializable

sealed class Routes {
    @Serializable
    data object Dashboard : Routes()

    @Serializable
    data class Details(val id: String) : Routes()
}