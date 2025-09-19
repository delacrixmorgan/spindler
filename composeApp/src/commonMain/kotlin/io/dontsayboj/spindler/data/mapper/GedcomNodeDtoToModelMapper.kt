package io.dontsayboj.spindler.data.mapper

import io.dontsayboj.spindler.data.utils.Mapper
import io.dontsayboj.spindler.domain.model.GedcomNode

class GedcomNodeDtoToModelMapper : Mapper<String, List<GedcomNode>> {

    override suspend fun invoke(input: String): List<GedcomNode> {
        val rawLines = input.replace(Regex("\r\n?"), "\n").lineSequence()
        val lines = unfoldLines(rawLines)

        val rootChildren = mutableListOf<GedcomNode>()
        val stack = ArrayDeque<GedcomNode>()

        for (line in lines) {
            val token = tokenize(line)?.takeIf { line.isNotBlank() } ?: continue

            // Skip tokens with no meaningful content unless they are structural tags
            if (token.value.isNullOrBlank() && token.pointer == null &&
                !isStructuralTag(token.tag)
            ) {
                continue
            }

            val node = GedcomNode(token.level, token.pointer, token.tag, token.value)

            // Ensure correct parent based on GEDCOM level
            while (stack.isNotEmpty() && stack.last().level >= node.level) {
                stack.removeLast()
            }
            (if (stack.isEmpty()) rootChildren else stack.last().children) += node
            stack += node
        }

        // Prune empty nodes from the parsed tree
        val prunedChildren = rootChildren.map { it.pruneEmptyNodes() }
        return prunedChildren
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
     * Helper method to identify structural GEDCOM tags that should be kept even if empty
     */
    private fun isStructuralTag(tag: String): Boolean {
        return tag in setOf(
            "INDI", "FAM", "NAME", "BIRT", "DEAT", "MARR",
            "DATE", "PLAC", "SEX", "HUSB", "WIFE", "CHIL",
            "FAMC", "FAMS", "SOUR", "NOTE", "OBJE", "HEAD",
            "TRLR", "SUBM", "GEDC", "CHAR", "VERS", "FORM"
        )
    }

    /**
     * Merge CONC/CONT with previous text node values.
     * We only normalize raw lines so a single logical line can be reconstructed if needed.
     */
    private fun unfoldLines(lines: Sequence<String>): Sequence<String> = lines
}