package com.hyperkani.reminder.data.repository

import com.hyperkani.reminder.data.model.ReminderItem


interface ReminderRepository {

    fun addReminder(reminderItem: ReminderItem)

    fun getReminders(): List<ReminderItem>

    fun deleteReminder(reminderItem: ReminderItem)

    fun getReminderById(id:Long):ReminderItem

    fun cancelReminder(reminderItem: ReminderItem)
}