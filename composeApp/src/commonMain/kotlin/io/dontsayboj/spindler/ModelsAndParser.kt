package io.dontsayboj.spindler

import io.dontsayboj.spindler.data.mapper.IndividualDtoToModelMapper
import io.dontsayboj.spindler.domain.model.Individual
import kotlinx.datetime.LocalDate

/**
 * Generic GEDCOM node (works for 5.5/5.5.1/7.0.x)
 */
data class GedcomNode(
    val level: Int,
    val pointer: String?,   // like @I1@ or @F1@ when present at the beginning
    val tag: String,        // INDI, FAM, NAME, BIRT, DATE, etc.
    val value: String?,     // rest of the line text
    val children: MutableList<GedcomNode> = mutableListOf()
)

/** Root document holding top-level nodes. */
data class GedcomDocument(
    val nodes: List<GedcomNode>
)

/** Domain models (pragmatic subset) */
data class Event(
    val type: String,            // BIRT, DEAT, MARR, etc.
    val date: String? = null,
    val place: String? = null,
    val parsedDate: LocalDate? = null
)

data class Family(
    val id: String,
    val husband: String?,
    val wife: String?,
    val children: List<String>,
    val marriage: Event?
)

data class GedcomIndex(
    val individuals: Map<String, Individual>,
    val families: Map<String, Family>
)

/** ------- Parser ------- */
object GedcomParser {
    /**
     * Parse GEDCOM text into a node tree.
     * Handles CONT/CONC line folding and common quoting, is intentionally lenient.
     */
    fun parseString(text: String): GedcomDocument {
        val rawLines = text.replace(Regex("\r\n?"), "\n").lineSequence()
        val lines = unfoldLines(rawLines)

        val rootChildren = mutableListOf<GedcomNode>()
        val stack = ArrayDeque<GedcomNode>()

        for (line in lines) {
            val token = tokenize(line)?.takeIf { line.isNotBlank() } ?: continue
            val node = GedcomNode(token.level, token.pointer, token.tag, token.value)

            // Ensure correct parent based on GEDCOM level
            while (stack.isNotEmpty() && stack.last().level >= node.level) {
                stack.removeLast()
            }
            (if (stack.isEmpty()) rootChildren else stack.last().children) += node
            stack += node
        }

        return GedcomDocument(rootChildren)
    }

    /** Simple token structure for one GEDCOM line. */
    private data class Token(val level: Int, val pointer: String?, val tag: String, val value: String?)

    /** Split a single GEDCOM line into a [Token], or null if invalid. */
    private fun tokenize(line: String): Token? {
        val firstSpace = line.indexOf(' ')
        if (firstSpace <= 0) return null

        val level = line.substring(0, firstSpace).toIntOrNull() ?: return null
        var rest = line.substring(firstSpace + 1).trim()

        val pointer = extractPointer(rest).also { if (it != null) rest = rest.removePrefix(it).trim() }
        val (tag, value) = splitTagAndValue(rest)

        return Token(level, pointer, tag, value)
    }

    /** Extract pointer of form "@...@" if present at start. */
    private fun extractPointer(text: String): String? =
        if (text.startsWith("@")) text.indexOf('@', startIndex = 1)
            .takeIf { it > 1 }
            ?.let { text.substring(0, it + 1) }
        else null

    /** Split into (tag, value). */
    private fun splitTagAndValue(text: String): Pair<String, String?> {
        val nextSpace = text.indexOf(' ')
        return if (nextSpace == -1) text to null
        else text.substring(0, nextSpace) to text.substring(nextSpace + 1).ifBlank { null }
    }

    /**
     * Merge CONC/CONT with previous text node values.
     * We only normalize raw lines so a single logical line can be reconstructed if needed.
     */
    private fun unfoldLines(lines: Sequence<String>): Sequence<String> = lines
}

/** ------- Mapping (Nodes -> Domain) ------- */
object Mapper {
    fun toIndex(doc: GedcomDocument): GedcomIndex {
        val individuals = mutableMapOf<String, Individual>()
        val families = mutableMapOf<String, Family>()

        for (node in doc.nodes) {
            when (node.tag) {
                "INDI" -> {
                    val id = node.pointer ?: node.value ?: "UNKNOWN"
                    individuals[id] = IndividualDtoToModelMapper(id)(node)
                }
                "FAM" -> {
                    val id = node.pointer ?: node.value ?: "UNKNOWN"
                    families[id] = toFamily(id, node)
                }
            }
        }
        return GedcomIndex(individuals, families)
    }

    private fun toFamily(id: String, node: GedcomNode): Family {
        val husb = node.children.firstOrNull { it.tag in listOf("HUSB", "HUSBAND") }?.value ?: node.children.firstOrNull { it.tag == "HUSB" }?.pointer
        val wife = node.children.firstOrNull { it.tag in listOf("WIFE", "WIFE") }?.value ?: node.children.firstOrNull { it.tag == "WIFE" }?.pointer
        val children = node.children.filter { it.tag == "CHIL" }.mapNotNull { it.value ?: it.pointer }
        val marr = node.children.firstOrNull { it.tag == "MARR" }?.let { toEvent("MARR", it) }
        return Family(id, husb, wife, children, marr)
    }

    private fun toEvent(type: String, node: GedcomNode): Event {
        val date = node.children.firstOrNull { it.tag == "DATE" }?.value
        val place = node.children.firstOrNull { it.tag == "PLAC" }?.value
        val parsed = date?.let { DateParsing.tryParseDate(it) }
        return Event(type, date, place, parsed)
    }
}

/** ------- Date Parsing (tolerant) ------- */
object DateParsing {
    // Attempt a few common GEDCOM formats (both 5.5 and 7 styles)
    private val monthMap = mapOf(
        "JAN" to 1, "FEB" to 2, "MAR" to 3, "APR" to 4, "MAY" to 5, "JUN" to 6,
        "JUL" to 7, "AUG" to 8, "SEP" to 9, "OCT" to 10, "NOV" to 11, "DEC" to 12
    )

    fun tryParseDate(raw: String): LocalDate? {
        val s = raw.trim().uppercase()

        // Examples: 1 JAN 1900 | JAN 1900 | 1900
        val parts = s.split(' ', limit = 3).filter { it.isNotBlank() }
        return when (parts.size) {
            3 -> {
                val d = parts[0].toIntOrNull()
                val m = monthMap[parts[1]]
                val y = parts[2].toIntOrNull()
                if (d != null && m != null && y != null) {
                    try {
                        LocalDate(y, m, d)
                    } catch (_: Throwable) {
                        null
                    }
                } else null
            }
            2 -> {
                val m = monthMap[parts[0]]
                val y = parts[1].toIntOrNull()
                if (m != null && y != null) {
                    try {
                        LocalDate(y, m, 1)
                    } catch (_: Throwable) {
                        null
                    }
                } else null
            }
            1 -> {
                val y = parts[0].toIntOrNull()
                if (y != null) try {
                    LocalDate(y, 1, 1)
                } catch (_: Throwable) {
                    null
                } else null
            }
            else -> null
        }
    }
}

/** ------- Queries ------- */
//object Queries {
//
//    /**
//     * Compute generation index relative to a root person: root=0, parents=-1, children=+1, etc.
//     * Uses family links (FAMC/FAMS/HUSB/WIFE/CHIL).
//     */
//    fun generationMap(index: GedcomIndex, rootId: String): Map<String, Int> {
//        val gen = mutableMapOf<String, Int>()
//        val queue = ArrayDeque<String>()
//        gen[rootId] = 0
//        queue.add(rootId)
//
//        while (queue.isNotEmpty()) {
//            val id = queue.removeFirst()
//            val g = gen[id]!!
//            val ind = index.individuals[id] ?: continue
//
//            // parents via FAMC -> family -> HUSB/WIFE
//            for (famId in ind.famc) {
//                val fam = index.families[famId] ?: continue
//                listOfNotNull(fam.husband, fam.wife).forEach { pId ->
//                    if (pId !in gen) {
//                        gen[pId] = g - 1; queue.add(pId)
//                    }
//                }
//                // siblings in same generation
//                fam.children.forEach { cId ->
//                    if (cId !in gen) {
//                        gen[cId] = g; queue.add(cId)
//                    }
//                }
//            }
//
//            // spouses & children via FAMS
//            for (famId in ind.fams) {
//                val fam = index.families[famId] ?: continue
//                listOfNotNull(fam.husband, fam.wife).forEach { sId ->
//                    if (sId != id && sId !in gen) {
//                        gen[sId] = g; queue.add(sId)
//                    }
//                }
//                fam.children.forEach { cId ->
//                    if (cId !in gen) {
//                        gen[cId] = g + 1; queue.add(cId)
//                    }
//                }
//            }
//        }
//        return gen
//    }
//
//    fun sameGeneration(index: GedcomIndex, personId: String): List<Individual> {
//        val gmap = generationMap(index, personId)
//        val g0 = gmap[personId] ?: 0
//        return gmap.filterValues { it == g0 }.keys.mapNotNull { index.individuals[it] }
//    }
//
//    fun generationsAboveBelow(index: GedcomIndex, personId: String): Pair<Int, Int> {
//        val gmap = generationMap(index, personId)
//        val me = gmap[personId] ?: 0
//        val minG = gmap.values.minOrNull() ?: me
//        val maxG = gmap.values.maxOrNull() ?: me
//        return (me - minG) to (maxG - me)
//    }
//
//    fun sortByBirthEldestToYoungest(people: List<Individual>): List<Individual> =
//        people.sortedWith(
//            compareBy(
//                { it.birth?.parsedDate ?: LocalDate(9999, 12, 31) },
//                { it.givenNames.firstOrNull() ?: "" }
//            ))
//
//    fun birthdaysCsv(index: GedcomIndex): String {
//        val sb = StringBuilder().appendLine("Name,Birthday")
//        index.individuals.values.forEach { ind ->
//            val name = ind.givenNames.firstOrNull() ?: "Unknown"
//            val date = ind.birth?.date ?: ""
//            sb.appendLine("\"$name\",\"$date\"")
//        }
//        return sb.toString()
//    }
//}
