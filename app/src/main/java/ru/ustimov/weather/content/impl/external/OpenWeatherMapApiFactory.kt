package ru.ustimov.weather.content.impl.external

import android.content.Context
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit

internal object OpenWeatherMapApiFactory {

    fun create(context: Context): OpenWeatherMapApi {
        val retrofit = Retrofit.Builder()
                //.baseUrl()
                //.callFactory(createCallFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
        return retrofit.create(OpenWeatherMapApi::class.java)
    }

}