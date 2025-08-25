package io.dontsayboj.spindler.data.mapper

import io.dontsayboj.spindler.Gedcom
import io.dontsayboj.spindler.data.dto.FamilyDto
import io.dontsayboj.spindler.data.utils.Mapper
import io.dontsayboj.spindler.domain.extension.getMarriageDate
import io.dontsayboj.spindler.domain.extension.getMarriagePlace
import io.dontsayboj.spindler.domain.extension.getPointer
import io.dontsayboj.spindler.domain.extension.getPointers
import io.dontsayboj.spindler.domain.model.Family

class FamilyDtoToModelMapper(val id: String) : Mapper<Gedcom.GedcomNode, Family> {
    override fun invoke(input: Gedcom.GedcomNode): Family {
        return Family(
            id = id,
            restriction = emptyList(),
            husbandID = input.getPointer(FamilyDto.HUSBAND_TAG),
            wifeID = input.getPointer(FamilyDto.WIFE_TAG),
            childrenIDs = input.getPointers(FamilyDto.CHILDREN_TAG),
            marriageDate = input.getMarriageDate(),
            marriagePlace = input.getMarriagePlace(),
            changeDate = null,
            creationDate = null,
        )
    }
}