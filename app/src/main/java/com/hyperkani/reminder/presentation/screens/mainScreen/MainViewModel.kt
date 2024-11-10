package com.hyperkani.reminder.presentation.screens.mainScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyperkani.reminder.data.model.ReminderItem
import com.hyperkani.reminder.data.repository.ReminderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: ReminderRepository
) : ViewModel() {

    private val _reminders = MutableStateFlow<List<ReminderItem>>(emptyList())
    val reminders: StateFlow<List<ReminderItem>> = _reminders


    fun setReminders() {
        viewModelScope.launch(Dispatchers.IO) {
            _reminders.value = repository.getReminders()
        }
    }

    fun setNewReminder(item: ReminderItem) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i("tag",item.toString())
            repository.addReminder(item)
            setReminders()
        }
    }

    fun deleteReminder(reminderItem: ReminderItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteReminder(reminderItem)
            setReminders()
        }
    }

}