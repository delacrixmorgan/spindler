package io.dontsayboj.spindler.data.mapper

import io.dontsayboj.spindler.Gedcom
import io.dontsayboj.spindler.data.utils.Mapper
import io.dontsayboj.spindler.domain.model.Family

class FamilyDtoToModelMapper(val id: String) : Mapper<Gedcom.GedcomNode, Family> {

    override suspend fun invoke(input: Gedcom.GedcomNode): Family {
        val meaningfulChildren = input.children.filter { child ->
            child.hasContent() && (child.value?.isNotBlank() == true ||
                    child.pointer != null ||
                    child.children.isNotEmpty())
        }

        return Family(
            id = id,
            nodes = meaningfulChildren,
        )
    }
}
