package com.hyperkani.reminder.presentation.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun LocalDateTime.toDateString(): String {
    val dateFormatter = DateTimeFormatter.ofPattern("dd:MM:yyyy")
    return this.format(dateFormatter)
}

fun LocalDateTime.toTimeString(): String {
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    return this.format(timeFormatter)
}

