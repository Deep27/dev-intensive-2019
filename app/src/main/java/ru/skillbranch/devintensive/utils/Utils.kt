package ru.skillbranch.devintensive.utils

import ru.skillbranch.devintensive.extensions.extractFirstUpperChar
import ru.skillbranch.devintensive.extensions.transliterateSingleChar
import java.lang.StringBuilder
import java.util.*

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

    fun toInitials(firstName: String?, lastName: String?): String? {
        val firstChar = firstName?.extractFirstUpperChar()
        val secondChar = lastName?.extractFirstUpperChar()
        if (firstChar == null && secondChar == null) {
            return null
        }
        if (firstChar == null) {
            return secondChar
        }
        if (secondChar == null) {
            return firstChar
        }
        return firstChar + secondChar
    }

    fun transliteration(payload: String, divider: String = " "): String {
        val transliteratedParts = payload.split(" ")
            .filter { p -> p.isNotEmpty() }
            .map { p ->
                var transliteratedPart = transliterate(p.toLowerCase(Locale.ROOT))
//                if (p[0].isUpperCase()) {
                    transliteratedPart =
                        transliteratedPart[0].toUpperCase() + transliteratedPart.substring(
                            1,
                            transliteratedPart.length
                        )
//                }
                transliteratedPart
            }

        val result = StringBuilder()
        transliteratedParts.forEach { p ->
            result.append(p).append(divider)
        }
        return if (result.isEmpty()) "" else result.deleteCharAt(result.length - 1).toString()
    }

    private fun transliterate(cyr: String): String {
        var lat = cyr.transliterateSingleChar("а", "а")
        lat = lat.transliterateSingleChar("б", "b")
        lat = lat.transliterateSingleChar("в", "v")
        lat = lat.transliterateSingleChar("г", "g")
        lat = lat.transliterateSingleChar("д", "d")
        lat = lat.transliterateSingleChar("е", "e")
        lat = lat.transliterateSingleChar("ё", "e")
        lat = lat.transliterateSingleChar("ж", "zh")
        lat = lat.transliterateSingleChar("з", "z")
        lat = lat.transliterateSingleChar("и", "i")
        lat = lat.transliterateSingleChar("й", "i")
        lat = lat.transliterateSingleChar("к", "k")
        lat = lat.transliterateSingleChar("л", "l")
        lat = lat.transliterateSingleChar("м", "m")
        lat = lat.transliterateSingleChar("н", "n")
        lat = lat.transliterateSingleChar("о", "o")
        lat = lat.transliterateSingleChar("п", "p")
        lat = lat.transliterateSingleChar("р", "r")
        lat = lat.transliterateSingleChar("с", "s")
        lat = lat.transliterateSingleChar("т", "t")
        lat = lat.transliterateSingleChar("у", "u")
        lat = lat.transliterateSingleChar("ф", "f")
        lat = lat.transliterateSingleChar("х", "h")
        lat = lat.transliterateSingleChar("ц", "c")
        lat = lat.transliterateSingleChar("ч", "ch")
        lat = lat.transliterateSingleChar("ш", "sh")
        lat = lat.transliterateSingleChar("щ", "sh")
        lat = lat.transliterateSingleChar("ъ", "")
        lat = lat.transliterateSingleChar("ы", "i")
        lat = lat.transliterateSingleChar("ь", "")
        lat = lat.transliterateSingleChar("э", "e")
        lat = lat.transliterateSingleChar("ю", "yu")
        return lat.transliterateSingleChar("я", "ya")
    }
}
