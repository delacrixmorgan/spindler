package io.dontsayboj.spindler.domain.model

import io.dontsayboj.spindler.Gedcom
import io.dontsayboj.spindler.domain.enum.NameStructure
import io.dontsayboj.spindler.domain.enum.NameTag
import io.dontsayboj.spindler.domain.enum.Sex
import io.dontsayboj.spindler.domain.enum.Tag
import io.dontsayboj.spindler.domain.enum.getOrDefault

data class Individual(
    val id: String,
    val nodes: List<Gedcom.GedcomNode>,
) {
    val formattedName: String
        get() = names.joinToString(" ") { it.text }

    val names: List<NameStructure>
        get() = nodes
            .filter { it.tag == Tag.NAME }
            .flatMap { it.children }
            .filter { NameTag.entries.firstOrNull { structure -> structure.name == it.tag && it.value?.isNotEmpty() == true } != null }
            .map { NameStructure(tag = NameTag.valueOf(it.tag), text = it.value ?: "") }

    val sex: Sex
        get() = Sex.entries.getOrDefault(nodes.firstOrNull { it.tag == Tag.SEX }?.value)

    val birthDate: String?
        get() = nodes
            .firstOrNull { it.tag == Tag.BIRTH }?.children
            ?.firstOrNull { it.tag == Tag.DATE }?.value

    val birthPlace: String?
        get() = nodes
            .firstOrNull { it.tag == Tag.BIRTH }?.children
            ?.firstOrNull { it.tag == Tag.PLACE }?.value

    val deathDate: String?
        get() = nodes
            .firstOrNull { it.tag == Tag.DEATH }?.children
            ?.firstOrNull { it.tag == Tag.DATE }?.value

    val familyIDAsChild: String?
        get() = nodes
            .firstOrNull { it.tag == Tag.FAMILY_ID_AS_CHILD }?.value

    val familyIDAsSpouse: String?
        get() = nodes
            .firstOrNull { it.tag == Tag.FAMILY_ID_AS_SPOUSE }?.value

    val changeDate: String?
        get() = nodes
            .firstOrNull { it.tag == Tag.CHANGE_DATE }?.value

    val creationDate: String?
        get() = nodes
            .firstOrNull { it.tag == Tag.CREATION_DATE }?.value
}