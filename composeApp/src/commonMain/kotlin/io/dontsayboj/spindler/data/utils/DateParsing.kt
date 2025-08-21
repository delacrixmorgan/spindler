package io.dontsayboj.spindler.data.utils

import kotlinx.datetime.LocalDate

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