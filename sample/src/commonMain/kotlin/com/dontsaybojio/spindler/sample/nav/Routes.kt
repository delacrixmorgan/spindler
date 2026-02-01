package com.dontsaybojio.spindler.sample.nav

import kotlinx.serialization.Serializable

sealed class Routes {
    @Serializable
    data object Dashboard : Routes()

    @Serializable
    data class IndividualDetail(val id: String) : Routes()

    @Serializable
    data class FamilyDetail(val id: String) : Routes()
}