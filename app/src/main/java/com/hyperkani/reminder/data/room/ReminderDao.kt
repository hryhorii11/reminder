package com.hyperkani.reminder.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface ReminderDao {


    @Upsert
    fun addReminder(reminderItemEntity: ReminderItemEntity):Long

    @Query("select * from reminders")
    fun getAllReminders(): List<ReminderItemEntity>

    @Delete
    fun deleteReminder(reminderItemEntity: ReminderItemEntity)

    @Query("select * from reminders where id=:id")
    fun getReminderById(id: Long):ReminderItemEntity
}