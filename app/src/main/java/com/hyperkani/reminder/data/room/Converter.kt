package com.hyperkani.reminder.data.room

import androidx.room.TypeConverter
import com.hyperkani.reminder.data.model.NotificationPeriod
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class Converter {

    @TypeConverter
    fun localDateTimeToLong(dateTime: LocalDateTime): Long {
        return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

  @TypeConverter
    fun longToLocalDateTime(timestamp: Long): LocalDateTime {
        return Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime()
    }
    @TypeConverter
    fun fromNotificationPeriod(notificationPeriod: NotificationPeriod): Int {
        return notificationPeriod.ordinal
    }

    @TypeConverter
    fun toNotificationPeriod(id: Int): NotificationPeriod {
        return NotificationPeriod.entries[id]
    }
}