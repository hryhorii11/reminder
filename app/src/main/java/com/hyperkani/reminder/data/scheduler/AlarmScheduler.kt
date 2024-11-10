package com.hyperkani.reminder.data.scheduler

import com.hyperkani.reminder.data.model.ReminderItem

interface AlarmScheduler {

    fun schedule(item:ReminderItem)
    fun cancel(item:ReminderItem)
}