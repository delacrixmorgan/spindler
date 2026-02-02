package com.dontsaybojio.spindler.domain.model

import com.dontsaybojio.spindler.domain.enum.MacFamilyTreeTag
import com.dontsaybojio.spindler.domain.enum.Tag
import com.dontsaybojio.spindler.utils.DateParsing
import kotlinx.datetime.LocalDate

data class Family(
    val id: String,
    val nodes: List<GedcomNode>,
) {
    val marriageDateRaw: String?
        get() = nodes.firstOrNull { it.tag == Tag.MARRIAGE }?.children
            ?.firstOrNull { it.tag == Tag.DATE }?.value

    val marriageDate: LocalDate?
        get() = DateParsing.tryParseDate(marriageDateRaw)

    val marriageDateFormatted: String
        get() = marriageDate?.toString() ?: "~${marriageDateRaw ?: "N/A"}"

    val marriagePlace: String?
        get() = nodes.firstOrNull { it.tag == Tag.MARRIAGE }?.children
            ?.firstOrNull { it.tag == Tag.PLACE }?.value

    val husbandID: String?
        get() = nodes.firstOrNull { it.tag == Tag.HUSBAND }?.value

    val wifeID: String?
        get() = nodes.firstOrNull { it.tag == Tag.WIFE }?.value

    val childrenIDs: List<String>
        get() = nodes.filter { it.tag == Tag.CHILDREN }.mapNotNull { it.value ?: it.pointer }

    val macFamilyTreeLabel: String?
        get() = nodes
            .firstOrNull { it.tag == MacFamilyTreeTag.LABEL }?.value

    val changeDate: String?
        get() = nodes
            .firstOrNull { it.tag == Tag.CHANGE_DATE }?.value

    val creationDate: String?
        get() = nodes
            .firstOrNull { it.tag == Tag.CREATION_DATE }?.value
}