package io.dontsayboj.spindler

import spindler.composeapp.generated.resources.Res

object Gedcom {

    suspend fun parseFile(path: String): Pair<GedcomDocument, GedcomIndex> {
        val text = Res.readBytes(path = path).decodeToString()
        return parseString(text)
    }

    fun parseString(text: String): Pair<GedcomDocument, GedcomIndex> {
        val doc = GedcomParser.parseString(text)
        val index = Mapper.toIndex(doc)
        return doc to index
    }
}
