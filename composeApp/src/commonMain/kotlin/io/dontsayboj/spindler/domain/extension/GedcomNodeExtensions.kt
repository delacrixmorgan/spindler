package io.dontsayboj.spindler.domain.extension

import io.dontsayboj.spindler.Gedcom.GedcomNode
import io.dontsayboj.spindler.data.dto.FamilyDto
import io.dontsayboj.spindler.domain.enum.GeneralTag

fun GedcomNode.getMarriageDate(): String? = children
    .firstOrNull { it.tag == FamilyDto.MARRIAGE_TAG }?.children?.firstOrNull { it.tag == GeneralTag.DATE.name }?.value

fun GedcomNode.getMarriagePlace(): String? = children
    .firstOrNull { it.tag == FamilyDto.MARRIAGE_TAG }?.children?.firstOrNull { it.tag == GeneralTag.PLAC.name }?.value

fun GedcomNode.getPointer(tag: String): String? = children
    .firstOrNull { it.tag == tag }?.value ?: children.firstOrNull { it.tag == tag }?.pointer

fun GedcomNode.getPointers(tag: String): List<String> = children
    .filter { it.tag == tag }.mapNotNull { it.value ?: it.pointer }

