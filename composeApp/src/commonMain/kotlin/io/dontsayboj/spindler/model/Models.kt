package io.dontsayboj.spindler.model

enum class Sex { M, F, U } // based on g7:enumset-SEX

data class DateValue(
    val date: String,       // original string payload (e.g. "2 OCT 1937")
    val sortDate: String? = null  // optional SDATE for sorting
)

enum class EnumSetName {
    ADOP, EVEN, FACT_ATTR, MEDI, PEDI, QUAY, RESN, ROLE, SEX, FAMC_STAT, ORD_STAT, NAME_TYPE
}

data class UID(val version: String)
data class EXID(val value: String, val type: String)

data class Name(val full: String, val type: String? = null) // g7:NAME + NAME-TYPE

data class Event(
    val tag: String,          // e.g. "BIRT", "DEAT"
    val date: DateValue? = null,
    val place: String? = null,
    val cause: String? = null,
    val description: String? = null
)

data class Fact(val type: String, val value: String? = null)

data class MediaFile(
    val filePath: String,     // FilePath type
    val crop: Crop? = null
)

data class Crop(val left: Int, val top: Int, val width: Int, val height: Int)

data class Individual(
    val uid: UID,
    val externalIds: List<EXID> = emptyList(),
    val names: List<Name> = emptyList(),
    val sex: Sex? = null,
    val events: List<Event> = emptyList(),
    val facts: List<Fact> = emptyList(),
    val notes: List<String> = emptyList(),  // with markup maybe
    val language: String? = null            // LANG tag
)

// Similarly, family record:
data class Family(
    val uid: UID,
    val husband: String? = null,  // reference to INDI UID
    val wife: String? = null,
    val children: List<String> = emptyList(),
    val events: List<Event> = emptyList(),
    val facts: List<Fact> = emptyList(),
    val notes: List<String> = emptyList()
)

data class GedcomFile(
    val header: Header,
    val individuals: List<Individual> = emptyList(),
    val families: List<Family> = emptyList(),
    val sources: List<Source> = emptyList(),
    val media: List<MediaFile> = emptyList(),
    val version: String // from Gedcom 7.0 UID in HEAD-GEDC
)

data class Header(
    val version: String,
    val source: String?,
    val submitter: String?,
    val language: String? = null, // HEAD-LANG
    val schema: List<String>? = null
)

data class Source(
    val uid: UID,
    val title: String?,
    val author: String?,
    val repository: String?
)