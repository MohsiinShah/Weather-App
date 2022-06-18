package com.openweathermap.forecast

import android.app.Application

import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {


//    private lateinit var appComponent: AppComponent
//
//    companion object {
//        lateinit var application: MyApplication
//            private set
//
//        val context: Context
//            get() = application.applicationContext
//    }
//
//    override fun onCreate() {
//        super.onCreate()
//        application = this
////        FirebaseApp.initializeApp(this)
////        basicClient = BasicChatClient(applicationContext)
//        appComponent = DaggerAppComponent.builder().application(this).build()
//        appComponent.inject(this)
//    }
//
//    override fun attachBaseContext(base: Context) {
//        super.attachBaseContext(base)
//        MultiDex.install(this)
//    }

}