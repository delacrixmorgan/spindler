package com.dontsaybojio.spindler.domain.model

import kotlinx.datetime.LocalDate

data class Family(
    val id: String,
    val nodes: List<com.dontsaybojio.spindler.domain.model.GedcomNode>,
) {
    val marriageDateRaw: String?
        get() = nodes.firstOrNull { it.tag == _root_ide_package_.com.dontsaybojio.spindler.domain.enum.Tag.MARRIAGE }?.children
            ?.firstOrNull { it.tag == _root_ide_package_.com.dontsaybojio.spindler.domain.enum.Tag.DATE }?.value

    val marriageDate: LocalDate?
        get() = _root_ide_package_.com.dontsaybojio.spindler.utils.DateParsing.tryParseDate(marriageDateRaw)

    val marriageDateFormatted: String
        get() = marriageDate?.toString() ?: "~${marriageDateRaw ?: "N/A"}"

    val marriagePlace: String?
        get() = nodes.firstOrNull { it.tag == _root_ide_package_.com.dontsaybojio.spindler.domain.enum.Tag.MARRIAGE }?.children
            ?.firstOrNull { it.tag == _root_ide_package_.com.dontsaybojio.spindler.domain.enum.Tag.PLACE }?.value

    val husbandID: String?
        get() = nodes.firstOrNull { it.tag == _root_ide_package_.com.dontsaybojio.spindler.domain.enum.Tag.HUSBAND }?.value

    val wifeID: String?
        get() = nodes.firstOrNull { it.tag == _root_ide_package_.com.dontsaybojio.spindler.domain.enum.Tag.WIFE }?.value

    val childrenIDs: List<String>
        get() = nodes.filter { it.tag == _root_ide_package_.com.dontsaybojio.spindler.domain.enum.Tag.CHILDREN }.mapNotNull { it.value ?: it.pointer }

    val macFamilyTreeLabel: String?
        get() = nodes
            .firstOrNull { it.tag == _root_ide_package_.com.dontsaybojio.spindler.domain.enum.MacFamilyTreeTag.LABEL }?.value

    val changeDate: String?
        get() = nodes
            .firstOrNull { it.tag == _root_ide_package_.com.dontsaybojio.spindler.domain.enum.Tag.CHANGE_DATE }?.value

    val creationDate: String?
        get() = nodes
            .firstOrNull { it.tag == _root_ide_package_.com.dontsaybojio.spindler.domain.enum.Tag.CREATION_DATE }?.value
}