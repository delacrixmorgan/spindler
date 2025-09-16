package io.dontsayboj.spindler.domain.model

import io.dontsayboj.spindler.Gedcom
import io.dontsayboj.spindler.domain.enum.MacFamilyTreeTag
import io.dontsayboj.spindler.domain.enum.Tag

data class Family(
    val id: String,
    val nodes: List<Gedcom.GedcomNode>,
) {
    val marriageDate: String?
        get() = nodes.firstOrNull { it.tag == Tag.MARRIAGE }?.children
            ?.firstOrNull { it.tag == Tag.DATE }?.value

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