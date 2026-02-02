package com.dontsaybojio.spindler.domain.enum

import com.dontsaybojio.spindler.domain.enum.Sex.F
import com.dontsaybojio.spindler.domain.enum.Sex.M
import com.dontsaybojio.spindler.domain.enum.Sex.U
import com.dontsaybojio.spindler.domain.enum.Sex.X
import kotlin.enums.EnumEntries

/**
 * g7:enumset-SEX
 *
 * @param M Male
 * @param F Female
 * @param X Does not fit the typical definition of only Male or only Female
 * @param U Cannot be determined from available sources
 */
enum class Sex {
    M,
    F,
    X,
    U;

    companion object {
        val Default = U
    }
}

fun EnumEntries<Sex>.getOrDefault(input: String?): Sex {
    return firstOrNull { it.name == input } ?: Sex.Default
}