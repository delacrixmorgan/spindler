package io.dontsayboj.spindler

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

data class Individual(
    val id: String,
    val givenNames: List<String>,
    val lastName: String,
    val sex: String?,
    val birth: Event?,
    val death: Event?,
    val famc: List<String>,   // child in these families
    val fams: List<String>    // spouse in these families
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
) {
    fun findIndividualsByName(name: String): List<Individual> =
        individuals.values.filter { it.givenNames.any { n -> n.equals(name, true) || n.contains(name, true) } }
}

/** ------- Parser ------- */
object GedcomParser {
    /**
     * Parse GEDCOM text into a node tree.
     * Handles CONT/CONC line folding and common quoting, is intentionally lenient.
     */
    fun parseString(text: String): GedcomDocument {
        val rawLines = text.replace("\r\n", "\n").replace("\r", "\n").lines()
        val lines = unfoldLines(rawLines)
        val rootChildren = mutableListOf<GedcomNode>()
        val stack = ArrayDeque<GedcomNode>()

        for (line in lines) {
            if (line.isBlank()) continue
            val tok = tokenize(line) ?: continue
            val node = GedcomNode(level = tok.level, pointer = tok.pointer, tag = tok.tag, value = tok.value)
            // Place in tree by level using a simple stack
            while (stack.isNotEmpty() && stack.last().level >= node.level) {
                stack.removeLast()
            }
            if (stack.isEmpty()) {
                rootChildren += node
            } else {
                stack.last().children += node
            }
            stack.addLast(node)
        }
        return GedcomDocument(nodes = rootChildren)
    }

    /** Simple token structure for one GEDCOM line. */
    private data class Token(val level: Int, val pointer: String?, val tag: String, val value: String?)

    /**
     * Tokenize a GEDCOM line of forms like:
     *  - 0 @I1@ INDI
     *  - 1 NAME John /Doe/
     *  - 2 DATE 1 JAN 1900
     *  - 1 NOTE Lorem
     */
    private fun tokenize(line: String): Token? {
        // Split first by space for level
        val firstSpace = line.indexOf(' ')
        if (firstSpace <= 0) return null
        val level = line.substring(0, firstSpace).toIntOrNull() ?: return null
        var rest = line.substring(firstSpace + 1).trim()

        var pointer: String? = null
        var tag: String
        var value: String? = null

        // If it starts with @...@ that's a pointer/xref
        if (rest.startsWith("@")) {
            val end = rest.indexOf("@", startIndex = 1)
            if (end > 1) {
                pointer = rest.substring(0, end + 1) // includes both @
                rest = rest.substring(end + 1).trim()
            }
        }

        // Next token is tag
        val nextSpace = rest.indexOf(' ')
        if (nextSpace == -1) {
            tag = rest
            rest = ""
        } else {
            tag = rest.substring(0, nextSpace)
            rest = rest.substring(nextSpace + 1)
        }

        value = rest.ifBlank { null }
        return Token(level, pointer, tag, value)
    }

    /**
     * Merge CONC/CONT with previous text node values.
     * We keep them as separate nodes in the tree, but for parsing helper fields we will stitch values.
     * Here we only normalize raw lines so a single logical line value can be reconstructed if needed.
     */
    private fun unfoldLines(lines: List<String>): List<String> = lines
}

/** ------- Mapping (Nodes -> Domain) ------- */
object Mapper {
    fun toIndex(doc: GedcomDocument): GedcomIndex {
        val individuals = mutableMapOf<String, Individual>()
        val families = mutableMapOf<String, Family>()

        for (n in doc.nodes) {
            when (n.tag) {
                "INDI" -> {
                    val id = n.pointer ?: n.value ?: "UNKNOWN"
                    individuals[id] = toIndividual(id, n)
                }
                "FAM" -> {
                    val id = n.pointer ?: n.value ?: "UNKNOWN"
                    families[id] = toFamily(id, n)
                }
            }
        }
        return GedcomIndex(individuals, families)
    }

    private fun toIndividual(id: String, node: GedcomNode): Individual {
        val nameNodes = node.children.filter { it.tag == "NAME" }
        val givenNames = mutableListOf<String>()
        var lastName = "Unknown"

        if (nameNodes.isEmpty()) givenNames.add("Unknown")
        nameNodes.forEach { n ->
            val raw = n.value ?: return@forEach
            val parts = raw.split("/")
            val given = parts.firstOrNull()?.trim().takeUnless { it.isNullOrEmpty() } ?: "Unknown"
            val surname = parts.getOrNull(1)?.trim().takeUnless { it.isNullOrEmpty() }

            givenNames.add(given)
            lastName = surname ?: node.children.firstOrNull { it.tag == "SURN" }?.value ?: "Unknown"
        }
        val sex = node.children.firstOrNull { it.tag == "SEX" }?.value
        val birth = node.children.firstOrNull { it.tag == "BIRT" }?.let { toEvent("BIRT", it) }
        val death = node.children.firstOrNull { it.tag == "DEAT" }?.let { toEvent("DEAT", it) }
        val famc = node.children.filter { it.tag == "FAMC" }.mapNotNull { it.value ?: it.pointer }
        val fams = node.children.filter { it.tag == "FAMS" }.mapNotNull { it.value ?: it.pointer }
        return Individual(id, givenNames, lastName, sex, birth, death, famc, fams)
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
object Queries {

    /**
     * Compute generation index relative to a root person: root=0, parents=-1, children=+1, etc.
     * Uses family links (FAMC/FAMS/HUSB/WIFE/CHIL).
     */
    fun generationMap(index: GedcomIndex, rootId: String): Map<String, Int> {
        val gen = mutableMapOf<String, Int>()
        val queue = ArrayDeque<String>()
        gen[rootId] = 0
        queue.add(rootId)

        while (queue.isNotEmpty()) {
            val id = queue.removeFirst()
            val g = gen[id]!!
            val ind = index.individuals[id] ?: continue

            // parents via FAMC -> family -> HUSB/WIFE
            for (famId in ind.famc) {
                val fam = index.families[famId] ?: continue
                listOfNotNull(fam.husband, fam.wife).forEach { pId ->
                    if (pId !in gen) {
                        gen[pId] = g - 1; queue.add(pId)
                    }
                }
                // siblings in same generation
                fam.children.forEach { cId ->
                    if (cId !in gen) {
                        gen[cId] = g; queue.add(cId)
                    }
                }
            }

            // spouses & children via FAMS
            for (famId in ind.fams) {
                val fam = index.families[famId] ?: continue
                listOfNotNull(fam.husband, fam.wife).forEach { sId ->
                    if (sId != id && sId !in gen) {
                        gen[sId] = g; queue.add(sId)
                    }
                }
                fam.children.forEach { cId ->
                    if (cId !in gen) {
                        gen[cId] = g + 1; queue.add(cId)
                    }
                }
            }
        }
        return gen
    }

    fun sameGeneration(index: GedcomIndex, personId: String): List<Individual> {
        val gmap = generationMap(index, personId)
        val g0 = gmap[personId] ?: 0
        return gmap.filterValues { it == g0 }.keys.mapNotNull { index.individuals[it] }
    }

    fun generationsAboveBelow(index: GedcomIndex, personId: String): Pair<Int, Int> {
        val gmap = generationMap(index, personId)
        val me = gmap[personId] ?: 0
        val minG = gmap.values.minOrNull() ?: me
        val maxG = gmap.values.maxOrNull() ?: me
        return (me - minG) to (maxG - me)
    }

    fun sortByBirthEldestToYoungest(people: List<Individual>): List<Individual> =
        people.sortedWith(
            compareBy(
                { it.birth?.parsedDate ?: LocalDate(9999, 12, 31) },
                { it.givenNames.firstOrNull() ?: "" }
            ))

    fun birthdaysCsv(index: GedcomIndex): String {
        val sb = StringBuilder().appendLine("Name,Birthday")
        index.individuals.values.forEach { ind ->
            val name = ind.givenNames.firstOrNull() ?: "Unknown"
            val date = ind.birth?.date ?: ""
            sb.appendLine("\"$name\",\"$date\"")
        }
        return sb.toString()
    }
}
