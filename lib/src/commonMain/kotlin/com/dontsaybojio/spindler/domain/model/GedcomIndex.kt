package com.dontsaybojio.spindler.domain.model

data class GedcomIndex(
    val individuals: Map<String, com.dontsaybojio.spindler.domain.model.Individual>,
    val families: Map<String, com.dontsaybojio.spindler.domain.model.Family>
)