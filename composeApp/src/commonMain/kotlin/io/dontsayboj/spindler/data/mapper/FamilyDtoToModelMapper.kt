package io.dontsayboj.spindler.data.mapper

import io.dontsayboj.spindler.data.utils.Mapper
import io.dontsayboj.spindler.domain.model.Family
import io.dontsayboj.spindler.domain.model.GedcomNode

class FamilyDtoToModelMapper(val id: String) : Mapper<GedcomNode, Family> {

    override suspend fun invoke(input: GedcomNode): Family {
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
