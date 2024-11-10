package com.hyperkani.reminder.data.model

import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.temporal.TemporalAdjusters

data class ReminderItem(
    val id:Long,
    val title:String,
    val time:LocalDateTime,
    val period: NotificationPeriod
){
    companion object{
        fun getReminderWithUpdateTime(item: ReminderItem): ReminderItem {
            val updatedTime = when (item.period) {
                NotificationPeriod.ONCE -> item.time
                NotificationPeriod.DAILY -> item.time.plusDays(1)
                NotificationPeriod.MON_TO_FRI -> {
                    when (item.time.dayOfWeek) {
                        DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY -> {
                            item.time.with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                        }
                        else -> {
                            item.time.plusDays(1)
                        }
                    }
                }
            }
            return item.copy(time = updatedTime)
        }

    }
}
