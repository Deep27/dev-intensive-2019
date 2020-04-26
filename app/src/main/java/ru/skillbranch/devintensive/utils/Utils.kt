package ru.skillbranch.devintensive.utils

object Utils {
    fun parseFullName(fullName: String?): Pair<String?, String?> {
        val parts: List<String>? = fullName?.trim()?.split(" ")
        var firstName = parts?.getOrNull(0)
        firstName = when (firstName) {
            "" -> null
            else -> firstName
        }
        val lastName = parts?.getOrNull(1)
        return firstName to lastName
    }

    fun transliteration(payload: String, divider: String = " "): String {
        return "TODO"
    }

    fun toInitials(firstName: String?, lastName: String?): String {
        return "TODO"
    }
}