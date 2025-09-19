package io.dontsayboj.spindler.data.dto

import io.dontsayboj.spindler.domain.model.Family
import io.dontsayboj.spindler.domain.model.Individual

data class GedcomIndexDto(
    val individuals: Map<String, Individual>,
    val families: Map<String, Family>
)