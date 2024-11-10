package com.hyperkani.reminder.data.repository

import android.content.Context
import com.hyperkani.reminder.data.model.ReminderItem
import com.hyperkani.reminder.data.room.ReminderDao
import com.hyperkani.reminder.data.room.ReminderItemEntity
import com.hyperkani.reminder.data.scheduler.AlarmScheduler
import com.hyperkani.reminder.data.scheduler.AlarmSchedulerImpl
import javax.inject.Inject

class ReminderRepositoryImpl @Inject constructor(
    private val reminderDao: ReminderDao,
    private val context: Context
) : ReminderRepository {

    private val alarmScheduler: AlarmScheduler = AlarmSchedulerImpl(context)
    override fun addReminder(reminderItem: ReminderItem) {
        val id = reminderDao.addReminder(ReminderItemEntity.createEntity(reminderItem))

        alarmScheduler.schedule(reminderItem.copy(id = id))
    }

    override fun getReminders(): List<ReminderItem> {
        return reminderDao.getAllReminders().map { it.toModel() }
    }

    override fun deleteReminder(reminderItem: ReminderItem) {
        reminderDao.deleteReminder(ReminderItemEntity.createEntity(reminderItem))
    }

    override fun getReminderById(id: Long): ReminderItem {
        return reminderDao.getReminderById(id).toModel()
    }

    override fun cancelReminder(reminderItem: ReminderItem) {
        alarmScheduler.cancel(reminderItem)
    }


}