package com.dicoding.aplikasigithubuserfinal.alarm

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.dicoding.aplikasigithubuserfinal.MainActivity
import com.dicoding.aplikasigithubuserfinal.R
import java.util.*

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        private const val EXTRA_MESSAGE = "message"
        private const val TITLE = "Alarm"
        private const val ID_REPEATING = 101
    }

    override fun onReceive(context: Context, intent: Intent) {
        val message = intent.getStringExtra(EXTRA_MESSAGE)

        message?.let {
            showAlarmNotification(context, message)
        }
    }

    fun setRepeatingAlarm(context: Context, message: String){

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)

        intent.putExtra(EXTRA_MESSAGE, message)

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 9)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }
        Log.d("CALENDAR", calendar.time.toString())

        val pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING, intent, 0)
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)

        Toast.makeText(context, context.getString(R.string.remind_up), Toast.LENGTH_SHORT).show()
    }

    fun cancelAlarm(context: Context){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)

        val requestCode = ID_REPEATING
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)
        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)

        Toast.makeText(context, context.getString(R.string.remind_down), Toast.LENGTH_SHORT).show()
    }

    private fun showAlarmNotification(context: Context, message: String) {
        val channelId = "Channel_1"
        val channelName = "Reminder channel"

        val notifIntent = Intent(context, MainActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(context, ID_REPEATING, notifIntent, 0)

        val notificationManagerCompat = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_baseline_alarm_24)
                .setContentTitle(TITLE)
                .setContentText(message)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setContentIntent(pendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = channelName

            builder.setChannelId(channelId)
            notificationManagerCompat.createNotificationChannel(channel)
        }

        val notification = builder.build()
        notificationManagerCompat.notify(ID_REPEATING, notification)
    }


}