package ru.skillbranch.devintensive.extensions

import java.text.SimpleDateFormat
import java.util.*

const val SECOND = 1000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR
const val YEAR = 365 * DAY

fun Date.format(pattern: String = "HH:mm:ss dd.MM.yy"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.add(value: Int, unit: TimeUnits = TimeUnits.SECOND): Date {
    this.time += when (unit) {
        TimeUnits.SECOND -> value * SECOND
        TimeUnits.MINUTE -> value * MINUTE
        TimeUnits.HOUR -> value * HOUR
        TimeUnits.DAY -> value * DAY
        TimeUnits.YEAR -> value * YEAR
    }
    return this
}

fun Date.humanizeDiff(date: Date = Date()): String {
    return when (val diff = date.time - time) {
        in Long.MIN_VALUE until -DAY * 360 -> "более чем через год"
        in -DAY * 360 until -HOUR * 26 -> "через ${TimeUnits.DAY.plural(-diff / DAY)}"
        in -HOUR * 26 until -HOUR * 22 -> "через день"
        in -HOUR * 22 until -MINUTE * 75 -> "через ${TimeUnits.HOUR.plural(-diff / HOUR)}"
        in -MINUTE * 75 until -MINUTE * 45 -> "через час"
        in -MINUTE * 45 until  -SECOND * 75 -> "через ${TimeUnits.MINUTE.plural(-diff / MINUTE)}"
        in -SECOND * 75 until -SECOND * 45 -> "через минуту"
        in -SECOND * 45 until -SECOND -> "через несколько секунд"
        in -SECOND until -1 -> "сейчас"
        in 0 until SECOND -> "только что"
        in SECOND until SECOND * 45 -> "несколько секунд назад"
        in SECOND * 45 until SECOND * 75 -> "минуту назад"
        in SECOND * 75 until MINUTE * 45 -> "${TimeUnits.MINUTE.plural(diff / MINUTE)} назад"
        in MINUTE * 45 until MINUTE * 75 -> "час назад"
        in MINUTE * 75 until HOUR * 22 -> "${TimeUnits.HOUR.plural(diff / HOUR)} назад"
        in HOUR * 22 until HOUR * 26 -> "день назад"
        in HOUR * 26 until DAY * 360 -> "${TimeUnits.DAY.plural(diff / DAY)} назад"
        else -> "более года назад"
    }
}

enum class TimeUnits {
    SECOND, MINUTE, HOUR, DAY, YEAR;

    fun plural(amount: Long): String {
        return when (this) {
            SECOND -> parseSeconds(amount)
            MINUTE -> parseMinutes(amount)
            HOUR -> parseHours(amount)
            DAY -> parseDays(amount)
            YEAR -> parseYears(amount)
        }
    }

    private fun parseSeconds(amount: Long): String {
        if (amount % 100 == 11L) {
            return "$amount секунд"
        }
        return "$amount " + when (amount % 10) {
            0L, in 5..9 -> "секунд"
            1L -> "секунду"
            in 2..4 -> "секунды"
            else -> "unknown"
        }
    }

    private fun parseMinutes(amount: Long): String {
        if (amount % 100 == 11L) {
            return "$amount минут"
        }
        return "$amount " + when (amount % 10) {
            0L, in 5..9 -> "минут"
            1L -> "минуту"
            in 2..4 -> "минуты"
            else -> "unknown"
        }
    }

    private fun parseHours(amount: Long): String {
        if (amount % 100 == 11L) {
            return "$amount часов"
        }
        return "$amount " + when (amount % 10) {
            0L, in 5..9 -> "часов"
            1L -> "час"
            in 2..4 -> "часа"
            else -> "unknown"
        }
    }

    private fun parseDays(amount: Long): String {
        if (amount % 100 == 11L) {
            return "$amount дней"
        }
        return "$amount " + when (amount % 10) {
            0L, in 5..9 -> "дней"
            1L -> "день"
            in 2..4 -> "дня"
            else -> "unknown"
        }
    }

    private fun parseYears(amount: Long): String {
        if (amount % 100 == 11L) {
            return "$amount лет"
        }
        return "$amount " + when (amount % 10) {
            0L, in 5..9 -> "лет"
            1L -> "год"
            in 2..4 -> "года"
            else -> "unknown"
        }
    }
}
