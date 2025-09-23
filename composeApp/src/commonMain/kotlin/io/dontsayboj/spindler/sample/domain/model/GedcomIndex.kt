package io.dontsayboj.spindler.sample.domain.model

data class GedcomIndex(
    val individuals: Map<String, Individual>,
    val families: Map<String, Family>
)