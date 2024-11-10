package com.hyperkani.reminder.di

import android.app.Application
import com.hyperkani.reminder.data.repository.ReminderRepository
import com.hyperkani.reminder.data.repository.ReminderRepositoryImpl
import com.hyperkani.reminder.data.room.ReminderDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideSleepRepository(
        reminderDao: ReminderDao,
        context:Application
    ): ReminderRepository = ReminderRepositoryImpl(reminderDao,context)
}