package com.openweathermap.forecast.di.modules

import android.content.Context
import android.content.SharedPreferences
import com.openweathermap.forecast.utils.Constants
import com.openweathermap.forecast.utils.MyAppPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class PreferencesModule {

    @Singleton
    @Provides
    fun provideMyAppPreferences(preferences: SharedPreferences): MyAppPreferences {
        return MyAppPreferences(preferences)
    }

    @Singleton
    @Provides
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(
            Constants.APP_PREFERENCES,
            Context.MODE_PRIVATE
        )
    }
}