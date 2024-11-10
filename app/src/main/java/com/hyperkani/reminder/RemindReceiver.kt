package com.hyperkani.reminder

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.hyperkani.reminder.data.model.NotificationPeriod
import com.hyperkani.reminder.data.model.ReminderItem
import com.hyperkani.reminder.data.repository.ReminderRepository
import com.hyperkani.reminder.presentation.MainActivity
import com.hyperkani.reminder.presentation.ReminderPopupActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import javax.inject.Inject

@AndroidEntryPoint
class RemindReceiver : BroadcastReceiver() {
    @Inject
    lateinit var repository: ReminderRepository

    override fun onReceive(context: Context?, intent: Intent?) {


        val message = intent?.getStringExtra("title") ?: return
        val time = intent.getLongExtra("time", 0L)
        val period = intent.getIntExtra("period", 0)
        val id = intent.getLongExtra("id", -1L)

        val alarmIntent = Intent(context, ReminderPopupActivity::class.java).apply {
            putExtra("title", message)
        }
        alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context?.startActivity(alarmIntent)

        val reminderItem = ReminderItem(
            id,
            message,
            Instant.ofEpochMilli(time).atZone(ZoneId.systemDefault()).toLocalDateTime(),
            NotificationPeriod.entries[period]
        )
        when (period) {
            NotificationPeriod.ONCE.ordinal -> {
                CoroutineScope(Dispatchers.IO).launch {
                    repository.deleteReminder(
                        reminderItem
                    )
                }
            }
            else -> {
                CoroutineScope(Dispatchers.IO).launch {
                    repository.addReminder(
                        ReminderItem.getReminderWithUpdateTime(
                            reminderItem
                        )
                    )
                }
            }
        }

        val channelId = "my_channel_id"
        val channelName = "My Channel"

        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Channel description"
        }
        val notificationManager =
            context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
        val startActivityIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context,
            0,
            startActivityIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val notification = context.let {
            NotificationCompat.Builder(it, channelId)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(message)
                .setContentText(context.getString(R.string.notification_text))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build()
        }

        if (context.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.POST_NOTIFICATIONS
                )
            } != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        NotificationManagerCompat.from(context).notify(1, notification)
    }
}
