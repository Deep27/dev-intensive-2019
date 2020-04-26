package ru.skillbranch.devintensive.extensions

import java.util.*

fun String.extractFirstUpperChar(): String? {
    val trimmed = this.trim()
    if (trimmed.isEmpty()) {
        return null
    }
    return this.substring(0, 1).toUpperCase(Locale.ROOT)
}

fun String.transliterateSingleChar(fromChar: String, toChar: String) =
    this.replace(fromChar.toRegex(), toChar)

fun String.truncate(to: Int = 16): String {
    var truncated = this.trim()
    if (truncated.length < to) {
        return truncated
    }
    truncated = truncated.substring(0, to).trim()
    return "$truncated..."
}
