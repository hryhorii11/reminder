package com.hyperkani.reminder.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hyperkani.reminder.data.model.NotificationPeriod
import com.hyperkani.reminder.data.model.ReminderItem
import java.time.LocalDateTime

@Entity(
    tableName = "reminders"
)
data class ReminderItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val title: String,
    val time: LocalDateTime,
    val period: NotificationPeriod
) {

    fun toModel() = ReminderItem(
        id,
        title,
        time,
        period
    )

    companion object {
        fun createEntity(reminderItem: ReminderItem) = ReminderItemEntity(
            reminderItem.id,
            reminderItem.title,
            reminderItem.time,
            reminderItem.period
        )
    }
}
