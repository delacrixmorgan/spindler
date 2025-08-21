package io.dontsayboj.spindler

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
