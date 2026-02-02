package com.dontsaybojio.spindler.domain.model

import com.dontsaybojio.spindler.domain.enum.MacFamilyTreeTag
import com.dontsaybojio.spindler.domain.enum.NameStructure
import com.dontsaybojio.spindler.domain.enum.NameTag
import com.dontsaybojio.spindler.domain.enum.Sex
import com.dontsaybojio.spindler.domain.enum.Tag
import com.dontsaybojio.spindler.domain.enum.getOrDefault
import com.dontsaybojio.spindler.utils.DateParsing
import kotlinx.datetime.LocalDate

data class Individual(
    val id: String,
    val nodes: List<GedcomNode>,
) {
    val names: List<NameStructure>
        get() = nodes
            .filter { it.tag == Tag.NAME }
            .flatMap { it.children }
            .filter { NameTag.entries.firstOrNull { structure -> structure.name == it.tag && it.value?.isNotEmpty() == true } != null }
            .map { NameStructure(tag = NameTag.valueOf(it.tag), text = it.value ?: "") }

    val givenNames: List<String>
        get() = nodes
            .filter { it.tag == Tag.NAME }
            .flatMap { it.children }
            .filter { it.tag == NameTag.GIVN.name }
            .mapNotNull { it.value }

    val surnames: List<String>
        get() = nodes
            .filter { it.tag == Tag.NAME }
            .flatMap { it.children }
            .filter { it.tag == NameTag.SURN.name }
            .mapNotNull { it.value }

    val formattedName: String
        get() = (givenNames + surnames).joinToString(" ")

    val sex: Sex
        get() = Sex.entries.getOrDefault(nodes.firstOrNull { it.tag == Tag.SEX }?.value)

    val birthDateRaw: String?
        get() = nodes
            .firstOrNull { it.tag == Tag.BIRTH }?.children
            ?.firstOrNull { it.tag == Tag.DATE }?.value

    val birthDate: LocalDate?
        get() = DateParsing.tryParseDate(birthDateRaw)

    val birthDateFormatted: String
        get() = birthDate?.toString() ?: "~${birthDateRaw ?: "N/A"}"

    val birthPlace: String?
        get() = nodes
            .firstOrNull { it.tag == Tag.BIRTH }?.children
            ?.firstOrNull { it.tag == Tag.PLACE }?.value

    val deathDateRaw: String?
        get() = nodes
            .firstOrNull { it.tag == Tag.DEATH }?.children
            ?.firstOrNull { it.tag == Tag.DATE }?.value

    val deathDate: LocalDate?
        get() = DateParsing.tryParseDate(deathDateRaw)

    val deathDateFormatted: String
        get() = deathDate?.toString() ?: "~${deathDateRaw ?: "N/A"}"

    val education: String?
        get() = nodes
            .firstOrNull { it.tag == Tag.EDUCATION }?.value

    val religion: String?
        get() = nodes
            .firstOrNull { it.tag == Tag.RELIGION }?.value

    val familyIDAsChild: String?
        get() = nodes
            .firstOrNull { it.tag == Tag.FAMILY_ID_AS_CHILD }?.value

    val familyIDAsSpouse: String?
        get() = nodes
            .firstOrNull { it.tag == Tag.FAMILY_ID_AS_SPOUSE }?.value

    val macFamilyTreeID: String?
        get() = nodes
            .firstOrNull { it.tag == MacFamilyTreeTag.FID }?.value

    val changeDate: String?
        get() = nodes
            .firstOrNull { it.tag == Tag.CHANGE_DATE }?.value

    val creationDate: String?
        get() = nodes
            .firstOrNull { it.tag == Tag.CREATION_DATE }?.value
}