package com.dontsaybojio.spindler.domain.enum

import com.dontsaybojio.spindler.domain.enum.IndividualAttribute.CAST
import com.dontsaybojio.spindler.domain.enum.IndividualAttribute.DSCR
import com.dontsaybojio.spindler.domain.enum.IndividualAttribute.EDUC
import com.dontsaybojio.spindler.domain.enum.IndividualAttribute.IDNO
import com.dontsaybojio.spindler.domain.enum.IndividualAttribute.NATI
import com.dontsaybojio.spindler.domain.enum.IndividualAttribute.NCHI
import com.dontsaybojio.spindler.domain.enum.IndividualAttribute.NMR
import com.dontsaybojio.spindler.domain.enum.IndividualAttribute.OCCU
import com.dontsaybojio.spindler.domain.enum.IndividualAttribute.PROP
import com.dontsaybojio.spindler.domain.enum.IndividualAttribute.RELI
import com.dontsaybojio.spindler.domain.enum.IndividualAttribute.RESI
import com.dontsaybojio.spindler.domain.enum.IndividualAttribute.SSN
import com.dontsaybojio.spindler.domain.enum.IndividualAttribute.TITL
import com.dontsaybojio.spindler.domain.enum.IndividualEvent.ADOP
import com.dontsaybojio.spindler.domain.enum.IndividualEvent.BAPM
import com.dontsaybojio.spindler.domain.enum.IndividualEvent.BARM
import com.dontsaybojio.spindler.domain.enum.IndividualEvent.BASM
import com.dontsaybojio.spindler.domain.enum.IndividualEvent.BIRT
import com.dontsaybojio.spindler.domain.enum.IndividualEvent.BLES
import com.dontsaybojio.spindler.domain.enum.IndividualEvent.BURI
import com.dontsaybojio.spindler.domain.enum.IndividualEvent.CENS
import com.dontsaybojio.spindler.domain.enum.IndividualEvent.CHR
import com.dontsaybojio.spindler.domain.enum.IndividualEvent.CHRA
import com.dontsaybojio.spindler.domain.enum.IndividualEvent.CONF
import com.dontsaybojio.spindler.domain.enum.IndividualEvent.CREM
import com.dontsaybojio.spindler.domain.enum.IndividualEvent.DEAT
import com.dontsaybojio.spindler.domain.enum.IndividualEvent.EMIG
import com.dontsaybojio.spindler.domain.enum.IndividualEvent.FCOM
import com.dontsaybojio.spindler.domain.enum.IndividualEvent.GRAD
import com.dontsaybojio.spindler.domain.enum.IndividualEvent.IMMI
import com.dontsaybojio.spindler.domain.enum.IndividualEvent.NATU
import com.dontsaybojio.spindler.domain.enum.IndividualEvent.ORDN
import com.dontsaybojio.spindler.domain.enum.IndividualEvent.PROB
import com.dontsaybojio.spindler.domain.enum.IndividualEvent.RETI
import com.dontsaybojio.spindler.domain.enum.IndividualEvent.WILL

interface IndividualTag

/**
 * 3.3.2.1 - Individual Attributes
 *
 * @param CAST The name of an individual’s rank or status in society which is sometimes based on racial or religious differences, or differences in wealth, inherited rank, profession, or occupation
 * @param DSCR The physical characteristics of a person
 * @param EDUC Indicator of a level of education attained
 * @param IDNO A number or other string assigned to identify a person within some significant external system. It must have a TYPE substructure to define what kind of identification number is being provided
 * @param NATI An individual’s national heritage or origin, or other folk, house, kindred, lineage, or tribal interest
 * @param NCHI The number of children that this person is known to be the parent of (all marriages)
 * @param NMR The number of times this person has participated in a family as a spouse or parent
 * @param OCCU The type of work or profession of an individual
 * @param PROP Pertaining to possessions such as real estate or other property of interest
 * @param RELI A religious denomination to which a person is affiliated or for which a record applies
 * @param RESI An address or place of residence where an individual resided
 * @param SSN A number assigned by the United States Social Security Administration, used for tax identification purposes. It is a type of IDNO
 * @param TITL A formal designation used by an individual in connection with positions of royalty or other social status, such as Grand Duke
 */
enum class IndividualAttribute : com.dontsaybojio.spindler.domain.enum.IndividualTag {
    CAST,
    DSCR,
    EDUC,
    IDNO,
    NATI,
    NCHI,
    NMR,
    OCCU,
    PROP,
    RELI,
    RESI,
    SSN,
    TITL
}

/**
 * g7:enumset-INDIVIDUAL-EVENT
 *
 * @param ADOP Creation of a legally approved child-parent relationship that does not exist biologically
 * @param BAPM Baptism, performed in infancy or later. (See also BAPL and CHR.)
 * @param BARM The ceremonial event held when a Jewish boy reaches age 13
 * @param BASM The ceremonial event held when a Jewish girl reaches age 13, also known as “Bat Mitzvah”
 * @param BIRT Entering into life
 * @param BLES Bestowing divine care or intercession. Sometimes given in connection with a naming ceremony
 * @param BURI Depositing the mortal remains of a deceased person
 * @param CENS Periodic count of the population for a designated locality, such as a national or state census
 * @param CHR Baptism or naming events for a child
 * @param CHRA Baptism or naming events for an adult person
 * @param CONF Conferring full church membership
 * @param CREM The act of reducing a dead body to ashes by fire
 * @param DEAT Mortal life terminates
 * @param EMIG Leaving one’s homeland with the intent of residing elsewhere
 * @param FCOM The first act of sharing in the Lord’s supper as part of church worship
 * @param GRAD Awarding educational diplomas or degrees to individuals
 * @param IMMI Entering into a new locality with the intent of residing there
 * @param NATU Obtaining citizenship
 * @param ORDN Receiving authority to act in religious matters
 * @param PROB Judicial determination of the validity of a will. It may indicate several related court activities over several dates
 * @param RETI Exiting an occupational relationship with an employer after a qualifying time period
 * @param WILL A legal document treated as an event, by which a person disposes of their estate. It takes effect after death. The event date is the date the will was signed while the person was alive. (See also PROB)
 */
enum class IndividualEvent : com.dontsaybojio.spindler.domain.enum.IndividualTag {
    ADOP,
    BAPM,
    BARM,
    BASM,
    BIRT,
    BLES,
    BURI,
    CENS,
    CHR,
    CHRA,
    CONF,
    CREM,
    DEAT,
    EMIG,
    FCOM,
    GRAD,
    IMMI,
    NATU,
    ORDN,
    PROB,
    RETI,
    WILL
}