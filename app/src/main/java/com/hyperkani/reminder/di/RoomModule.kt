package com.hyperkani.reminder.di

import android.app.Application
import com.hyperkani.reminder.data.room.AppDatabase
import com.hyperkani.reminder.data.room.ReminderDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class RoomModule {
    @Provides
    @Singleton
    fun provideDataBase(app: Application): AppDatabase {
        return AppDatabase.getDataBase(app)
    }

    @Provides
    @Singleton
    fun provideReminderDao(db: AppDatabase): ReminderDao = db.getReminderDao()


}