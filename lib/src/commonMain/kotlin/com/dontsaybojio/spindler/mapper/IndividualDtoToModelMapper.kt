package com.dontsaybojio.spindler.mapper

import com.dontsaybojio.spindler.domain.model.GedcomNode
import com.dontsaybojio.spindler.domain.model.Individual
import com.dontsaybojio.spindler.utils.Mapper

class IndividualDtoToModelMapper(val id: String) : Mapper<GedcomNode, Individual> {

    override suspend fun invoke(input: GedcomNode): Individual {
        val meaningfulChildren = input.children.filter { child ->
            child.hasContent() && (child.value?.isNotBlank() == true ||
                    child.pointer != null ||
                    child.children.isNotEmpty())
        }

        return Individual(
            id = id,
            nodes = meaningfulChildren,
        )
    }
}
