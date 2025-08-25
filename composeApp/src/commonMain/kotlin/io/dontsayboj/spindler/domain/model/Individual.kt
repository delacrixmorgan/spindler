package io.dontsayboj.spindler.domain.model

import io.dontsayboj.spindler.Gedcom
import io.dontsayboj.spindler.data.dto.TagDto
import io.dontsayboj.spindler.domain.enum.NameStructure
import io.dontsayboj.spindler.domain.enum.NameTag
import io.dontsayboj.spindler.domain.enum.Sex

data class Individual(
    val id: String,
    val nodes: List<Gedcom.GedcomNode>,
) {
    val names: List<NameStructure>
        get() = nodes
            .filter { it.tag == TagDto.NAME }
            .flatMap { it.children }
            .filter { NameTag.entries.firstOrNull { structure -> structure.name == it.tag && it.value?.isNotEmpty() == true } != null }
            .map { NameStructure(tag = NameTag.valueOf(it.tag), text = it.value ?: "") }

    val sex: Sex
        get() {
            val value = nodes.firstOrNull { it.tag == TagDto.SEX }?.value
            return Sex.entries.firstOrNull { it.name == value } ?: Sex.Default
        }

    val birthDate: String?
        get() = nodes
            .firstOrNull { it.tag == TagDto.BIRTH }?.children
            ?.firstOrNull { it.tag == TagDto.DATE }?.value

    val birthPlace: String?
        get() = nodes
            .firstOrNull { it.tag == TagDto.BIRTH }?.children
            ?.firstOrNull { it.tag == TagDto.PLACE }?.value

    val deathDate: String?
        get() = nodes
            .firstOrNull { it.tag == TagDto.DEATH }?.children
            ?.firstOrNull { it.tag == TagDto.DATE }?.value

    val familyIDAsChild: String?
        get() = nodes
            .firstOrNull { it.tag == TagDto.FAMILY_ID_AS_CHILD }?.value

    val familyIDAsSpouse: String?
        get() = nodes
            .firstOrNull { it.tag == TagDto.FAMILY_ID_AS_SPOUSE }?.value

//    val changeDate: LocalDate?,
//    val creationDate: LocalDate?
}