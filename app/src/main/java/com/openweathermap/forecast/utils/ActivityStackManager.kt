package com.openweathermap.forecast.utils


import android.content.Context
import android.content.Intent
import com.openweathermap.forecast.ui.search.SearchActivity

class ActivityStackManager {

    companion object {

        val instance = ActivityStackManager()
    }

    /**
     * This function will start Patient Home activity
     * @param context it be context to start new Activity
     */
    fun startSearchActivity(context: Context) {
        val intent = Intent(context, SearchActivity::class.java)
        context.startActivity(intent)
    }

}
