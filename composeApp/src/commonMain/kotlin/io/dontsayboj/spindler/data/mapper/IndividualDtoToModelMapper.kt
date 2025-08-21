package io.dontsayboj.spindler.data.mapper

import io.dontsayboj.spindler.GedcomNode
import io.dontsayboj.spindler.data.utils.Mapper
import io.dontsayboj.spindler.domain.extension.getBirthDate
import io.dontsayboj.spindler.domain.extension.getBirthPlace
import io.dontsayboj.spindler.domain.extension.getDeathDate
import io.dontsayboj.spindler.domain.extension.getFamilyIDAsChild
import io.dontsayboj.spindler.domain.extension.getFamilyIDAsSpouse
import io.dontsayboj.spindler.domain.extension.getNames
import io.dontsayboj.spindler.domain.extension.getSex
import io.dontsayboj.spindler.domain.model.Individual

class IndividualDtoToModelMapper(val id: String) : Mapper<GedcomNode, Individual> {

    // Make it suspend again
    override fun invoke(input: GedcomNode): Individual {
        return Individual(
            id = id,
            restriction = emptyList(),
            names = input.getNames(),
            sex = input.getSex(),
            birthDate = input.getBirthDate(),
            birthPlace = input.getBirthPlace(),
            deathDate = input.getDeathDate(),
            familyIDAsChild = input.getFamilyIDAsChild(),
            familyIDAsSpouse = input.getFamilyIDAsSpouse(),
            changeDate = null,
            creationDate = null,
        )
    }
}