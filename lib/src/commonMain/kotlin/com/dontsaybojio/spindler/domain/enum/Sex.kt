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

fun EnumEntries<com.dontsaybojio.spindler.domain.enum.Sex>.getOrDefault(input: String?): com.dontsaybojio.spindler.domain.enum.Sex {
    return firstOrNull { it.name == input } ?: _root_ide_package_.com.dontsaybojio.spindler.domain.enum.Sex.Companion.Default
}