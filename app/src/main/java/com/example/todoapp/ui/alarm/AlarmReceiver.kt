package com.example.todoapp.ui.alarm

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.todoapp.MainActivity
import com.example.todoapp.R

class AlarmReceiver : BroadcastReceiver() {
    companion object {
        const val ACTION_STOP_ALARM = "com.example.todoapp.STOP_ALARM"
        private var ringtone: Ringtone? = null
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("AlarmReceiver", "Received intent with action: ${intent.action}")
        when (intent.action) {
            ACTION_STOP_ALARM -> {
                Log.d("AlarmReceiver", "Stopping ringtone")
                ringtone?.stop()
                ringtone = null
                // Hủy notification
                val notificationManager = NotificationManagerCompat.from(context)
                notificationManager.cancel(1)
            }
            else -> {
                val notificationManager = NotificationManagerCompat.from(context)
                val channelId = "alarm_channel"

                // Kênh thông báo (Android 8+)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val channel = NotificationChannel(
                        channelId,
                        "Alarm Channel",
                        NotificationManager.IMPORTANCE_HIGH
                    ).apply {
                        enableVibration(true)
                        vibrationPattern = longArrayOf(0, 500, 1000, 500)
                        setSound(
                            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM),
                            AudioAttributes.Builder()
                                .setUsage(AudioAttributes.USAGE_ALARM)
                                .build()
                        )
                    }
                    notificationManager.createNotificationChannel(channel)
                }

                val alarmSound: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                    ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

                // Tạo PendingIntent để mở MainActivity khi nhấn thông báo
                val contentIntent = Intent(context, MainActivity::class.java).apply {
                    putExtra("isRinging", true) // Truyền trạng thái để hiển thị nút "Tắt chuông"
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
                val pendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    contentIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                // Tạo thông báo
                val notification = NotificationCompat.Builder(context, channelId)
                    .setContentTitle("⏰ Báo thức")
                    .setContentText("Hết giờ rồi!")
                    .setSmallIcon(R.drawable.ic_bell)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setSound(alarmSound)
                    .setAutoCancel(true)
                    .setVibrate(longArrayOf(0, 500, 1000, 500))
                    .setContentIntent(pendingIntent) // Gắn PendingIntent
                    .build()

                notificationManager.notify(1, notification)

                // Phát chuông
                ringtone?.stop() // Dừng chuông cũ nếu có
                ringtone = RingtoneManager.getRingtone(context, alarmSound)
                ringtone?.play()
                Log.d("AlarmReceiver", "Playing ringtone")
            }
        }
    }
}