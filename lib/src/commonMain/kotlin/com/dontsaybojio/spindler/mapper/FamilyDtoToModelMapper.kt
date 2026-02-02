package com.dontsaybojio.spindler.mapper

import com.dontsaybojio.spindler.domain.model.Family
import com.dontsaybojio.spindler.domain.model.GedcomNode
import com.dontsaybojio.spindler.utils.Mapper

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
