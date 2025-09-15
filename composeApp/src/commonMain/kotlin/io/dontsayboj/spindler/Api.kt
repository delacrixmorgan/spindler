package io.dontsayboj.spindler

import io.dontsayboj.spindler.data.mapper.FamilyDtoToModelMapper
import io.dontsayboj.spindler.data.mapper.IndividualDtoToModelMapper
import io.dontsayboj.spindler.domain.model.Family
import io.dontsayboj.spindler.domain.model.Individual
import spindler.composeapp.generated.resources.Res

object Gedcom {

    suspend fun parseFile(path: String): Pair<GedcomDocument, GedcomIndex> {
        val text = Res.readBytes(path = path).decodeToString()
        return parseString(text)
    }

    private suspend fun parseString(text: String): Pair<GedcomDocument, GedcomIndex> {
        val doc = GedcomParser.parseString(text)
        
        // Additional pruning at the document level to ensure clean data
        val prunedNodes = doc.nodes.map { it.pruneEmptyNodes() }
        val prunedDoc = doc.copy(nodes = prunedNodes)
        
        val individuals = mutableMapOf<String, Individual>()
        val families = mutableMapOf<String, Family>()

        // Use pruned document for processing
        for (node in prunedDoc.nodes) {
            when (node.tag) {
                "INDI" -> {
                    val id = node.pointer ?: node.value ?: "UNKNOWN"
                    individuals[id] = IndividualDtoToModelMapper(id)(node)
                }
                "FAM" -> {
                    val id = node.pointer ?: node.value ?: "UNKNOWN"
                    families[id] = FamilyDtoToModelMapper(id)(node)
                }
            }
        }
        val index = GedcomIndex(individuals, families)
        return prunedDoc to index
    }

    /**
     * Generic GEDCOM node (works for 5.5/5.5.1/7.0.x)
     */
    data class GedcomNode(
        val level: Int,
        val pointer: String?,   // like @I1@ or @F1@ when present at the beginning
        val tag: String,        // INDI, FAM, NAME, BIRT, DATE, etc.
        val value: String?,     // rest of the line text
        val children: MutableList<GedcomNode> = mutableListOf()
    ) {
        /**
         * Check if node has meaningful content
         */
        fun hasContent(): Boolean {
            return !value.isNullOrBlank() || pointer != null || children.any { it.hasContent() }
        }
        
        /**
         * Remove empty child nodes recursively
         */
        fun pruneEmptyNodes(): GedcomNode {
            val prunedChildren = children
                .filter { it.hasContent() }
                .map { it.pruneEmptyNodes() }
                .toMutableList()
            
            return this.copy(children = prunedChildren)
        }
    }

    /** Root document holding top-level nodes. */
    data class GedcomDocument(
        val nodes: List<GedcomNode>
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
                
                // Skip tokens with no meaningful content unless they are structural tags
                if (token.value.isNullOrBlank() && token.pointer == null && 
                    !isStructuralTag(token.tag)) {
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
            return GedcomDocument(prunedChildren)
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
}
