package ru.skillbranch.devintensive.extensions

import java.lang.IllegalArgumentException
import java.text.SimpleDateFormat
import java.util.*

const val SECOND = 1000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR

fun Date.format(pattern: String = "HH:mm:ss dd.MM.yy"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.add(value: Int, unit: TimeUnit = TimeUnit.SECOND): Date {
    this.time += when (unit) {
        TimeUnit.SECOND -> value * SECOND
        TimeUnit.MINUTE -> value * MINUTE
        TimeUnit.HOUR -> value * HOUR
        TimeUnit.DAY -> value * DAY
        else -> throw IllegalArgumentException("Invalid unit!")
    }
    return this
}

fun Date.humanizeDiff(date: Date = Date()): String {
    val currentTime = date.time

    return "TODO"
}

enum class TimeUnit {
    SECOND, MINUTE, HOUR, DAY
}
