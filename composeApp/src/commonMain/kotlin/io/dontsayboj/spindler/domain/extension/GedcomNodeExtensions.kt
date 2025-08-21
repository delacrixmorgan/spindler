package io.dontsayboj.spindler.domain.extension

import io.dontsayboj.spindler.GedcomNode
import io.dontsayboj.spindler.data.dto.FamilyDto
import io.dontsayboj.spindler.data.dto.IndividualDto
import io.dontsayboj.spindler.domain.enum.GeneralTag
import io.dontsayboj.spindler.domain.enum.IndividualEvent
import io.dontsayboj.spindler.domain.enum.NameStructure
import io.dontsayboj.spindler.domain.enum.NameTag
import io.dontsayboj.spindler.domain.enum.Sex

fun GedcomNode.getNames(): List<NameStructure> = children
    .filter { it.tag == IndividualDto.NAME_TAG }
    .flatMap { it.children }
    .filter { NameTag.entries.firstOrNull { structure -> structure.name == it.tag && it.value?.isNotEmpty() == true } != null }
    .map { NameStructure(tag = NameTag.valueOf(it.tag), text = it.value ?: "") }

fun GedcomNode.getSex(): Sex {
    val value = children.firstOrNull { it.tag == IndividualDto.SEX_TAG }?.value
    return Sex.entries.firstOrNull { it.name == value } ?: Sex.Default
}

fun GedcomNode.getBirthDate(): String? = children
    .firstOrNull { it.tag == IndividualEvent.BIRT.name }?.children
    ?.firstOrNull { it.tag == GeneralTag.DATE.name }?.value

fun GedcomNode.getBirthPlace(): String? = children
    .firstOrNull { it.tag == IndividualEvent.BIRT.name }?.children
    ?.firstOrNull { it.tag == GeneralTag.PLAC.name }?.value

fun GedcomNode.getDeathDate(): String? = children
    .firstOrNull { it.tag == IndividualEvent.DEAT.name }?.children
    ?.firstOrNull { it.tag == GeneralTag.DATE.name }?.value

fun GedcomNode.getMarriageDate(): String? = children
    .firstOrNull { it.tag == FamilyDto.MARRIAGE_TAG }?.children?.firstOrNull { it.tag == GeneralTag.DATE.name }?.value

fun GedcomNode.getMarriagePlace(): String? = children
    .firstOrNull { it.tag == FamilyDto.MARRIAGE_TAG }?.children?.firstOrNull { it.tag == GeneralTag.PLAC.name }?.value

fun GedcomNode.getFamilyIDAsChild(): String? = children
    .firstOrNull { it.tag == IndividualDto.FAMILY_ID_AS_CHILD_TAG }?.value

fun GedcomNode.getFamilyIDAsSpouse(): String? = children
    .firstOrNull { it.tag == IndividualDto.FAMILY_ID_AS_SPOUSE_TAG }?.value

fun GedcomNode.getPointer(tag: String): String? = children
    .firstOrNull { it.tag == tag }?.value ?: children.firstOrNull { it.tag == tag }?.pointer

fun GedcomNode.getPointers(tag: String): List<String> = children
    .filter { it.tag == tag }.mapNotNull { it.value ?: it.pointer }

