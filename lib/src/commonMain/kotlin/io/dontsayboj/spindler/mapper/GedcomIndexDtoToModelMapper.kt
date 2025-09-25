package io.dontsayboj.spindler.mapper

import io.dontsayboj.spindler.domain.enum.GedcomIndexTag
import io.dontsayboj.spindler.domain.model.Family
import io.dontsayboj.spindler.domain.model.GedcomIndex
import io.dontsayboj.spindler.domain.model.Individual
import io.dontsayboj.spindler.utils.Mapper

class GedcomIndexDtoToModelMapper : Mapper<String, GedcomIndex> {
    private val gedcomNodeDtoToModelMapper: GedcomNodeDtoToModelMapper by lazy { GedcomNodeDtoToModelMapper() }

    override suspend fun invoke(input: String): GedcomIndex {
        val nodes = gedcomNodeDtoToModelMapper(input).map { it.pruneEmptyNodes() }
        val individuals = mutableMapOf<String, Individual>()
        val families = mutableMapOf<String, Family>()

        for (node in nodes) {
            when (node.tag) {
                GedcomIndexTag.INDI.name -> {
                    val id = node.pointer ?: node.value ?: "UNKNOWN"
                    individuals[id] = IndividualDtoToModelMapper(id)(node)
                }
                GedcomIndexTag.FAM.name -> {
                    val id = node.pointer ?: node.value ?: "UNKNOWN"
                    families[id] = FamilyDtoToModelMapper(id)(node)
                }
            }
        }
        return GedcomIndex(individuals, families)
    }
}