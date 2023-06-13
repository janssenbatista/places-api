package dev.janssenbatitsa.placesapi.utils

import java.util.regex.Pattern

fun String.generateSlug(separator: String = "-"): String =
    this.trim().split(Pattern.compile("\\s+")).joinToString(separator) {
        it.lowercase()
    }