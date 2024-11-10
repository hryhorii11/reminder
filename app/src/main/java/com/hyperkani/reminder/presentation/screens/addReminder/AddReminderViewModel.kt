package com.hyperkani.reminder.presentation.screens.addReminder

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyperkani.reminder.data.model.NotificationPeriod
import com.hyperkani.reminder.data.model.ReminderItem
import com.hyperkani.reminder.data.repository.ReminderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class AddReminderViewModel @Inject constructor(
    private val reminderRepository: ReminderRepository
) : ViewModel() {


    private val _reminderItem = MutableStateFlow<ReminderItem>(
        ReminderItem(
            0L, "",
            LocalDateTime.now(),
            NotificationPeriod.ONCE
        )
    )
    val reminderItem: StateFlow<ReminderItem> = _reminderItem


    fun cancelReminder(reminderItem: ReminderItem) {
        viewModelScope.launch {
            reminderRepository.cancelReminder(reminderItem)
        }
    }

    fun setReminder(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            _reminderItem.value = reminderRepository.getReminderById(id)
            Log.i("tagR", reminderItem.value.toString())
        }
    }

    fun addReminder(reminderItem: ReminderItem) {
        viewModelScope.launch(Dispatchers.IO) {
            reminderRepository.addReminder(reminderItem)
        }
    }

}