package com.capgemini.weatherapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capgemini.weatherapp.model.WeatherModel
import com.capgemini.weatherapp.service.WeatherAPIService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

private const val TAG = "MainViewModel"

class MainViewModel : ViewModel() {

    private val weatherApiService = WeatherAPIService()
    private val disposable = CompositeDisposable()

    private val _weather_data: MutableLiveData<WeatherModel> by lazy { MutableLiveData() }
    fun getWeatherData(): LiveData<WeatherModel> = _weather_data


    private val _all_weather_data: MutableLiveData<List<WeatherModel>> by lazy { MutableLiveData() }
    fun getAllWeatherData(): LiveData<List<WeatherModel>> = _all_weather_data


    private val _weather_error: MutableLiveData<String> by lazy { MutableLiveData() }
    fun getError(): LiveData<String> = _weather_error


    val weather_loading = MutableLiveData<Boolean>()


    fun getWeather() {

        weather_loading.value = true
        disposable.add(
            weatherApiService.getWeather()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<WeatherModel>>() {

                    override fun onSuccess(weatherResponse: List<WeatherModel>) {
                        _all_weather_data.postValue(weatherResponse)
                        weather_loading.value = false
                    }

                    override fun onError(e: Throwable) {
                        _weather_error.postValue("Error :" + e.message)
                        weather_loading.value = false
                    }

                })
        )

    }


    fun getCityWeather(cityName: String) {

        weather_loading.value = true
        disposable.add(
            weatherApiService.getDataService(cityName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<WeatherModel>() {

                    override fun onSuccess(weatherResponse: WeatherModel) {
                        _weather_data.postValue(weatherResponse)
                        weather_loading.value = false
                    }

                    override fun onError(e: Throwable) {
                        _weather_error.postValue("Error :" + e.message)
                        weather_loading.value = false
                    }

                })
        )

    }

}