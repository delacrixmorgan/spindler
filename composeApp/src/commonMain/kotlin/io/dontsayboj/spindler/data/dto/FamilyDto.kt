package io.dontsayboj.spindler.data.dto

import kotlinx.datetime.LocalDate

/**
 * FAMILY_RECORD :=
 */
data class FamilyDto(
    val id: String? = null,
    val restriction: List<String>? = null, // RESN

    // Skipped FAMILY_ATTRIBUTE_STRUCTURE
    val event: List<String>? = null,
    // Skipped NON_EVENT_STRUCTURE

    val husbandID: String? = null, // HUSB
    val wifeID: String? = null, // WIFE
    val childrenIDs: List<String>? = null, // CHIL

    // Skipped ASSOCIATION_STRUCTURE
    // Skipped SUBM
    // Skipped LDS_SPOUSE_SEALING
    // Skipped IDENTIFIER_STRUCTURE
    // Skipped NOTE_STRUCTURE
    // Skipped SOURCE_CITATION
    // Skipped MULTIMEDIA_LINK

    val changeDate: LocalDate? = null, // CHAN
    val creationDate: LocalDate? = null, // CREA
) {
    companion object {
        const val HUSBAND_TAG = "HUSB"
        const val WIFE_TAG = "WIFE"
        const val CHILDREN_TAG = "CHIL"
        const val MARRIAGE_TAG = "MARR"
    }
}