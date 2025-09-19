package io.dontsayboj.spindler.data.mapper

import io.dontsayboj.spindler.data.utils.Mapper
import io.dontsayboj.spindler.domain.model.GedcomNode
import io.dontsayboj.spindler.domain.model.Individual

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
