package com.openweathermap.forecast.notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.annotation.CallSuper
import androidx.core.app.NotificationCompat
import com.openweathermap.forecast.R
import com.openweathermap.forecast.ui.WeatherActivity
import com.openweathermap.forecast.utils.MyAppPreferences
import com.openweathermap.forecast.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

abstract class HiltBroadcastReceiver : BroadcastReceiver() {
    @CallSuper
    override fun onReceive(context: Context, intent: Intent) {}
}

@AndroidEntryPoint
class AlarmReceiver :  HiltBroadcastReceiver() {

    @Inject
    lateinit var sharedprefs: MyAppPreferences


    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        showPushNotification(context) // implement showing notification in this function
    }

    private fun showPushNotification(context: Context){

        val current = sharedprefs.getCurrentTemperature()

        val mNotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent()
        intent.setClass(context,WeatherActivity::class.java)
        intent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)

        val pIntent = PendingIntent.getActivity(context, 0, intent, 0)

        val builder =
            NotificationCompat.Builder(context, Utils.getPushNotificationChannelId(context))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Weather Forecast")
                .setContentText("Today's temperatue is ${current?.temp}. Feels like ${current?.feels_like}")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pIntent)
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText("")
                )

        builder.setSmallIcon(R.mipmap.ic_launcher)
        builder.setAutoCancel(true)
        builder.setTicker("")
        builder.setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
        builder.setLights(Color.YELLOW, 3000, 3000)
        mNotificationManager.notify(0, builder.build())
    }
}