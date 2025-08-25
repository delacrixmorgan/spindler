package io.dontsayboj.spindler.data.dto

import io.dontsayboj.spindler.domain.enum.Restrictions
import kotlinx.datetime.LocalDate

/**
 * INDIVIDUAL_RECORD :=
 */
data class IndividualDto(
    val id: String? = null,
    val restriction: List<Restrictions>? = null, // RESN
    val names: List<String>? = null,
    val sex: String? = null, // SEX

    val attribute: List<String>? = null,
    val event: List<String>? = null,

//    Skipped NON_EVENT_STRUCTURE
//    Skipped LDS_INDIVIDUAL_ORDINANCE

    val familyIDAsChild: String? = null, // FAMC
    val familyIDAsSpouse: String? = null, // FAMS

    // Skipped SUBM
    // Skipped ALIA
    // Skipped ANCI
    // Skipped DESI
    // Skipped IDENTIFIER_STRUCTURE
    // Skipped NOTE_STRUCTURE
    // Skipped SOURCE_CITATION
    // Skipped MULTIMEDIA_LINK

    val changeDate: LocalDate? = null, // CHAN
    val creationDate: LocalDate? = null, // CREA
) {
    companion object {

    }
}