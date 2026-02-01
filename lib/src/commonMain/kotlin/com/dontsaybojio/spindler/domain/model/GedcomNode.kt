package com.dontsaybojio.spindler.domain.model

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