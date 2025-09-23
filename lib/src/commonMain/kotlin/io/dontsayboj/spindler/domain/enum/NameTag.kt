package io.dontsayboj.spindler.domain.enum

import io.dontsayboj.spindler.domain.enum.NameTag.GIVN
import io.dontsayboj.spindler.domain.enum.NameTag.NICK
import io.dontsayboj.spindler.domain.enum.NameTag.NPFX
import io.dontsayboj.spindler.domain.enum.NameTag.NSFX
import io.dontsayboj.spindler.domain.enum.NameTag.SPFX
import io.dontsayboj.spindler.domain.enum.NameTag.SURN

/**
 * PERSONAL_NAME_PIECES :=
 *
 * @param NPFX Name prefix
 * @param GIVN Given name
 * @param NICK Nickname
 * @param SPFX Surname prefix
 * @param SURN Surname
 * @param NSFX Name suffix
 */
enum class NameTag {
    NPFX,
    GIVN,
    NICK,
    SPFX,
    SURN,
    NSFX
}

data class NameStructure(
    val tag: NameTag,
    val text: String
)