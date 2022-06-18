package com.openweathermap.forecast.ui

import android.Manifest
import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.gms.location.*
import com.openweathermap.forecast.R
import com.openweathermap.forecast.databinding.ActivityMainBinding
import com.openweathermap.forecast.models.City
import com.openweathermap.forecast.models.Forecast
import com.openweathermap.forecast.persistance.Favourites
import com.openweathermap.forecast.utils.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class WeatherActivity : AppCompatActivity() {

    private val viewModel: WeatherViewModel by viewModels()

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var mLocationRequest: LocationRequest? = null
    private var mLocationCallback: LocationCallback? = null

    @Inject
    lateinit var sharedPrefs: MyAppPreferences

    lateinit var viewBinding: ActivityMainBinding

    lateinit var adapter: WeatherAdapter

    private var forecasts = mutableListOf<Forecast>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(location: LocationResult) {
                super.onLocationResult(location)
                if (location.lastLocation != null) {
                    val city = City(
                        lat = location.lastLocation!!.latitude,
                        lon = location.lastLocation!!.latitude
                    )
                    sharedPrefs.saveSelectedCity(Favourites(null, city = city))
                    viewModel.refresh()
                    fusedLocationClient.removeLocationUpdates(mLocationCallback!!)
                }
            }
        }

        setupUI()
        setupObservers()
        setupCurrentLocation()
        Utils.schedulePushNotifications(this)
    }

    private fun setupUI() {
        LocalBroadcastManager.getInstance(this).registerReceiver(
            favCitySelectedReceiver, IntentFilter(
                Constants.INTENTS.FAV_SELECTED
            )
        )

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        adapter = WeatherAdapter(this, forecasts)
        viewBinding.forecastRecyclerView.addItemDecoration(
            DividerItemDecoration(
                viewBinding.forecastRecyclerView.context,
                (viewBinding.forecastRecyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )
        viewBinding.forecastAdapter = adapter
    }

    private fun setupObservers() {

        viewModel.forecasts.observe(this@WeatherActivity) { it ->
            viewBinding.progressBar.isVisible = it is Resource.Loading

            val dailyData = it.data?.daily

            if (dailyData != null) {

                if (forecasts.isNotEmpty())
                    forecasts.clear()

                val current = it.data.current

                sharedPrefs.saveCurrentTemperature(current)

                viewBinding.timeZone.text = it.data.timezone
                viewBinding.tempToday.text = Utils.formatTemperature(current.temp)
                viewBinding.feelsLike.text = Utils.feelsLike(current.temp)
                viewBinding.todayDescription.text = current.weather[0].description

                Glide.with(this)
                    .load(Utils.getIconUrl(current.weather[0].icon))
                    .into(viewBinding.iconToday)

                for (i in dailyData.indices) {
                    val dailyObj = dailyData[i]
                    val forecast = Forecast(
                        dailyObj.weather[0].icon,
                        dailyObj.temp.max,
                        dailyObj.temp.min,
                        Utils.getDay(dailyObj.dt)
                    )
                    forecasts.add(forecast)
                }
                viewBinding.contentLayout.visibility = View.VISIBLE
            }
        }
    }

    private fun setupCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                Constants.REQUEST_PERMISSIONS_CODE
            )
            return
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_items, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
            R.id.action_settings -> {
                finish()
            }
            R.id.search -> {
                ActivityStackManager.instance.startSearchActivity(this)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val favCitySelectedReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            viewModel.refresh()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == Constants.REQUEST_PERMISSIONS_CODE) {
            if ((grantResults.isNotEmpty() &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED)
            ) {
                requestLocationUpdates()
            }
        }
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(favCitySelectedReceiver)
        super.onDestroy()
    }

    fun requestLocationUpdates() {
        mLocationRequest = LocationRequest()
        mLocationRequest!!.interval = 3000
        mLocationRequest!!.fastestInterval = 1000
        mLocationRequest!!.priority = Priority.PRIORITY_HIGH_ACCURACY

        try {
            fusedLocationClient.requestLocationUpdates(
                mLocationRequest!!,
                mLocationCallback!!, Looper.myLooper()
            )
        } catch (unlikely: SecurityException) {
            Log.d(
                TAG, "Lost location permission. Could not request updates. $unlikely"
            )
        }
    }
}