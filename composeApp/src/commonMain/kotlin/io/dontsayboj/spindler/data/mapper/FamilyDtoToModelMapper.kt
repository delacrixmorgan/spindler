package io.dontsayboj.spindler.data.mapper

import io.dontsayboj.spindler.Gedcom
import io.dontsayboj.spindler.data.utils.Mapper
import io.dontsayboj.spindler.domain.model.Family

class FamilyDtoToModelMapper(val id: String) : Mapper<Gedcom.GedcomNode, Family> {

    // Make it suspend again
    override fun invoke(input: Gedcom.GedcomNode): Family {
        return Family(
            id = id,
            nodes = input.children,
        )
    }
}