package com.openweathermap.forecast.utils

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.graphics.Color
import android.graphics.Color.blue
import android.graphics.Typeface
import android.location.Address
import android.location.Geocoder
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.openweathermap.forecast.R
import com.openweathermap.forecast.models.City
import com.openweathermap.forecast.notifications.AlarmReceiver
import com.skydoves.powermenu.MenuAnimation
import com.skydoves.powermenu.OnMenuItemClickListener
import com.skydoves.powermenu.PowerMenu
import com.skydoves.powermenu.PowerMenuItem
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


internal object Utils {

    fun getIconUrl(iconUrl: String): String {
        return "http://openweathermap.org/img/wn/$iconUrl@2x.png"
    }

    fun formatTemperature(temp: Double): String {
        return "$temp °"
    }

    fun feelsLike(temp: Double): String {
        return "Feels like:  $temp"
    }

    fun getDay(dt: Long): String {
        val date = Date(dt * 1000L)
        val format = SimpleDateFormat("EEEE")
        format.timeZone = TimeZone.getTimeZone("Etc/UTC")
        return format.format(date)
    }

    fun getLocationFromAddress(context: Context, cityName: String?): City? {
        val coder = Geocoder(context)
        try {
            val adresses = coder.getFromLocationName(cityName, 50) as ArrayList<Address>
            var city = City()
            for (add in adresses) {
                city = City(cityName, add.latitude, add.longitude)
                break
            }
            return city
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    fun hideKeyboard(context: Context, view: View) {
        val imm: InputMethodManager? =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }

     fun sendBroadcast(context: Context) {
        val intent = Intent(Constants.INTENTS.FAV_SELECTED)
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
    }

    fun schedulePushNotifications(context: Context) {
        val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
         val alarmPendingIntent by lazy {
            val intent = Intent(context, AlarmReceiver::class.java)
            PendingIntent.getBroadcast(context, 0, intent, 0)
        }

        val calendar = GregorianCalendar.getInstance().apply {

            if (get(Calendar.HOUR_OF_DAY) >= Constants.HOURS_TO_PUSH_NOTIFICATION) {
                add(Calendar.DAY_OF_MONTH, 1)
            }

            set(Calendar.HOUR_OF_DAY, Constants.HOURS_TO_PUSH_NOTIFICATION)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            alarmPendingIntent
        )
    }

    /**
     * this method is used to get channel id for chat notification
     */
    fun getPushNotificationChannelId(context: Context): String {
        var chanelId = "PUSH_NOTIFICATION"
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "bidsquad_chat_notification_channel_id"
        val channelName = "bidsquad_chat_notification_channel"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val notificationChannel = NotificationChannel(channelId, channelName, importance)
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.RED
        notificationChannel.enableVibration(true)
        notificationChannel.vibrationPattern =
            longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
        notificationManager.createNotificationChannel(notificationChannel)
        chanelId = notificationChannel.id
        return chanelId
    }

    fun getPowerMenu(context: Context, onMenuItemClickListener: OnMenuItemClickListener<PowerMenuItem>): PowerMenu{

        val powerMenu = PowerMenu.Builder(context)
            .addItem(PowerMenuItem("Celsius", true)) // add an item.
            .addItem(PowerMenuItem("Fahrenheit", false)) // aad an item list.
            .setAnimation(MenuAnimation.SHOWUP_TOP_RIGHT)
            .setMenuRadius(10f)
            .setMenuShadow(10f)
            .setTextColor(ContextCompat.getColor(context, R.color.white))
            .setTextGravity(Gravity.CENTER)
            .setTextTypeface(Typeface.create("sans-serif-medium", Typeface.BOLD))
            .setSelectedTextColor(Color.WHITE)
            .setMenuColor(ContextCompat.getColor(context, R.color.black))
            .setSelectedMenuColor(ContextCompat.getColor(context, android.R.color.holo_green_light))
            .setOnMenuItemClickListener(onMenuItemClickListener)
            .build()

        return powerMenu
    }
}