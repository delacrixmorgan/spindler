package io.dontsayboj.spindler.domain.model

import io.dontsayboj.spindler.domain.enum.Restrictions
import kotlinx.datetime.LocalDate

data class Family(
    val id: String,
    val restriction: List<Restrictions>,
    val husbandID: String?,
    val wifeID: String?,
    val childrenIDs: List<String>,
    val marriageDate: String?,
    val marriagePlace: String?,

    val changeDate: LocalDate?,
    val creationDate: LocalDate?
)