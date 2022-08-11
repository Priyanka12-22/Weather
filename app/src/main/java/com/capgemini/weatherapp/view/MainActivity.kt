package com.capgemini.weatherapp.view

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capgemini.weatherapp.R
import com.capgemini.weatherapp.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var weatherAdapter: WeatherAdapter
    private lateinit var viewmodel: MainViewModel
    private lateinit var GET: SharedPreferences
    private lateinit var SET: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)
        recyclerview!!.layoutManager = layoutManager
        //val forecastDaysAdapter = mWeatherData?.let { WeatherAdapter(it) }
        weatherAdapter = WeatherAdapter()
        recyclerview!!.adapter =weatherAdapter


        btn_change_theme.setOnClickListener { chooseThemeDialog() }

        GET = getSharedPreferences(packageName, MODE_PRIVATE)
        SET = GET.edit()

        viewmodel = ViewModelProviders.of(this).get(MainViewModel::class.java)



        var cName = GET.getString("cityName", "london")?.lowercase(Locale.getDefault())
        edt_city_name.setText(cName)
        cName?.let { viewmodel.getCityWeather(it) }



   /*     var cName = GET.getString("cityName", "london")?.lowercase(Locale.getDefault())

        viewmodel.getWeather()
*/

        getLiveData()

        swipe_refresh_layout.setOnRefreshListener {
            ll_data.visibility = View.GONE
            tv_error.visibility = View.GONE
            pb_loading.visibility = View.GONE

            var cityName = GET.getString("cityName", cName)?.lowercase(Locale.getDefault())
            edt_city_name.setText(cityName)
            cityName?.let { viewmodel.getCityWeather(it) }
            swipe_refresh_layout.isRefreshing = false
        }

        img_search_city.setOnClickListener {
            val inputManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.SHOW_FORCED)
            val cityName = edt_city_name.text.toString()
            SET.putString("cityName", cityName)
            SET.apply()
            viewmodel.getCityWeather(cityName)
            getLiveData()
        }

    }

    private fun getLiveData() {



        viewmodel.getAllWeatherData().observe(this, {
            weatherAdapter.setData(it)


            it.forEach { data->
                if (data.name.equals("London")){
                    ll_data.visibility = View.VISIBLE
                    tv_city_code.text = data.sys.country.toString()
                    tv_city_name.text = data.name.toString()
                    tv_degree.text = data.main.temp.toString() + "°C"
                    tv_humidity.text = data.main.humidity.toString() + "%"
                    tv_wind_speed.text = data.wind.speed.toString()
                    tv_lat.text = data.coord.lat.toString()
                    tv_lon.text = data.coord.lon.toString()
                }

            }

        })

        viewmodel.getWeatherData().observe(this, { data ->
            data?.let {
                ll_data.visibility = View.VISIBLE
                tv_city_code.text = data.sys.country.toString()
                tv_city_name.text = data.name.toString()
                tv_degree.text = data.main.temp.toString() + "°C"
                tv_humidity.text = data.main.humidity.toString() + "%"
                tv_wind_speed.text = data.wind.speed.toString()
                tv_lat.text = data.coord.lat.toString()
                tv_lon.text = data.coord.lon.toString()
            }
        })

        viewmodel.getError().observe(this, { error ->
                tv_error.visibility = View.VISIBLE
                pb_loading.visibility = View.GONE
                ll_data.visibility = View.GONE
        })

        viewmodel.weather_loading.observe(this, Observer { loading ->
            loading?.let {
                if (loading) {
                    pb_loading.visibility = View.VISIBLE
                    tv_error.visibility = View.GONE
                    ll_data.visibility = View.GONE
                } else {
                    pb_loading.visibility = View.GONE
                }
            }
        })

    }
    private fun chooseThemeDialog() {

        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.choose_theme_text))
        val styles = arrayOf("Light","Dark","System default")
        val checkedItem = 0

        builder.setSingleChoiceItems(styles, checkedItem) { dialog, which ->

            when (which) {
                0 -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    delegate.applyDayNight()
                    dialog.dismiss()
                }
                1 -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    delegate.applyDayNight()

                    dialog.dismiss()
                }
                2 -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    delegate.applyDayNight()
                    dialog.dismiss()
                }

            }
        }

        val dialog = builder.create()
        dialog.show()
    }

}