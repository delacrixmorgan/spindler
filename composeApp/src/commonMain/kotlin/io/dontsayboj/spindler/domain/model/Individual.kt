package io.dontsayboj.spindler.domain.model

import io.dontsayboj.spindler.domain.enum.NameStructure
import io.dontsayboj.spindler.domain.enum.Restrictions
import io.dontsayboj.spindler.domain.enum.Sex
import kotlinx.datetime.LocalDate

data class Individual(
    val id: String,
    val restriction: List<Restrictions>,
    val names: List<NameStructure>,
    val sex: Sex,

    val birthDate: String?,
    val birthPlace: String?,
    val deathDate: String?,

    val familyIDAsChild: String?,
    val familyIDAsSpouse: String?,

    val changeDate: LocalDate?,
    val creationDate: LocalDate?
)