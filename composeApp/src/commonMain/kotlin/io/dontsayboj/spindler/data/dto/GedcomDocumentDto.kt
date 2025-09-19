package io.dontsayboj.spindler.data.dto

import io.dontsayboj.spindler.domain.model.GedcomNode

data class GedcomDocumentDto(
    val nodes: List<GedcomNode>
)