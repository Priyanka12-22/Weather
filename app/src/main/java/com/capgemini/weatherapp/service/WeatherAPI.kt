package com.capgemini.weatherapp.service

import com.capgemini.weatherapp.BuildConfig
import com.capgemini.weatherapp.model.WeatherModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {

    @GET("data/2.5/weather")
    fun getData(
        @Query("q") cityName: String,
        @Query("units") units :String = "metric",
        @Query("appId") appId:String = BuildConfig.API_KEY
    ): Single<WeatherModel>

    @GET("data/2.5/weather")
    fun getWeather(


        @Query("units") units :String = "metric",
        @Query("appId") appId:String = BuildConfig.API_KEY
    ): Single<List<WeatherModel>>

}