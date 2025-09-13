package io.dontsayboj.spindler.data.mapper

import io.dontsayboj.spindler.Gedcom
import io.dontsayboj.spindler.data.utils.Mapper
import io.dontsayboj.spindler.domain.model.Individual

class IndividualDtoToModelMapper(val id: String) : Mapper<Gedcom.GedcomNode, Individual> {

    override suspend fun invoke(input: Gedcom.GedcomNode): Individual {
        return Individual(
            id = id,
            nodes = input.children,
        )
    }
}