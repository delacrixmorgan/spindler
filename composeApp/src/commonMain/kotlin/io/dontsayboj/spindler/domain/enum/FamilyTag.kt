package io.dontsayboj.spindler.domain.enum

import io.dontsayboj.spindler.domain.enum.FamilyEvent.ANUL
import io.dontsayboj.spindler.domain.enum.FamilyEvent.CENS
import io.dontsayboj.spindler.domain.enum.FamilyEvent.DIV
import io.dontsayboj.spindler.domain.enum.FamilyEvent.DIVF
import io.dontsayboj.spindler.domain.enum.FamilyEvent.ENGA
import io.dontsayboj.spindler.domain.enum.FamilyEvent.MARB
import io.dontsayboj.spindler.domain.enum.FamilyEvent.MARC
import io.dontsayboj.spindler.domain.enum.FamilyEvent.MARL
import io.dontsayboj.spindler.domain.enum.FamilyEvent.MARR
import io.dontsayboj.spindler.domain.enum.FamilyEvent.MARS

interface FamilyTag

/**
 * g7:enumset-FAMILY-EVENT
 *
 * @param ANUL Declaring a marriage void from the beginning (never existed)
 * @param CENS Periodic count of the population for a designated locality, such as a national or state census
 * @param DIV Dissolving a marriage through civil action
 * @param DIVF Filing for a divorce by a spouse
 * @param ENGA Recording or announcing an agreement between two people to become married
 * @param MARB Official public notice given that two people intend to marry
 * @param MARC Recording a formal agreement of marriage, including the prenuptial agreement in which marriage partners reach agreement about the property rights of one or both, securing property to their children
 * @param MARL Obtaining a legal license to marry
 * @param MARR A legal, common-law, or customary event such as a wedding or marriage ceremony that joins two partners to create or extend a family unit
 * @param MARS Creating an agreement between two people contemplating marriage, at which time they agree to release or modify property rights that would otherwise arise from the marriage
 */
enum class FamilyEvent : FamilyTag {
    ANUL,
    CENS,
    DIV,
    DIVF,
    ENGA,
    MARB,
    MARC,
    MARL,
    MARR,
    MARS
}

data class FamilyStructure(
    val tag: FamilyTag,
    val text: String
)