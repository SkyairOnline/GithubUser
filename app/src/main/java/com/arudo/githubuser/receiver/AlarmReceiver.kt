package com.arudo.githubuser.receiver

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.arudo.githubuser.MainActivity
import com.arudo.githubuser.R
import com.arudo.githubuser.SettingActivity
import java.util.*

class AlarmReceiver : BroadcastReceiver() {
    companion object {
        const val ID_REPEATING = 101
        const val CHANNEL_ID = "channel_id"
        const val CHANNEL_NAME = "channel_name"
        const val NOTIFICATION_REQUEST_CODE = 200
        const val HOURS = 9
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action.equals("android.intent.action.BOOT_COMPLETED")) {
            val checked = SettingActivity.NOTIFICATION_SWITCH_DATA.toBoolean()
            if (checked) {
                startNotification(context)
            }
        }
        showAlarmNotification(context)
    }

    private fun showAlarmNotification(context: Context) {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builderNotification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(context.getString(R.string.notificationTitle))
            .setContentText(context.getString(R.string.notificationContent))
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(alarmSound)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            channel.description =
                CHANNEL_NAME
            builderNotification.setChannelId(CHANNEL_ID)

            notificationManager.createNotificationChannel(channel)
        }
        val notification = builderNotification.build()
        notificationManager.notify(ID_REPEATING, notification)
    }

    fun startNotification(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(
                Calendar.HOUR_OF_DAY,
                HOURS
            )
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            ID_REPEATING, intent, 0
        )
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    fun cancelNotification(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            ID_REPEATING, intent, 0
        )
        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)
    }
}