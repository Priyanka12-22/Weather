package com.capgemini.weatherapp.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capgemini.weatherapp.R
import com.capgemini.weatherapp.model.WeatherModel

class WeatherAdapter :
    RecyclerView.Adapter<WeatherAdapter.ForecastHolder?>() {
    var mWeatherDataList: List<WeatherModel> = ArrayList()

    class ForecastHolder(view: View) : RecyclerView.ViewHolder(view) {
        var dateOnDate: TextView
        var weatherOnDate: TextView
        var tempOnDate: TextView
        var windSpeedOnDate: TextView

        init {
            dateOnDate = view.findViewById(R.id.tv_city_name) as TextView
            windSpeedOnDate = view.findViewById(R.id.tv_humidity) as TextView
            tempOnDate = view.findViewById(R.id.tv_wind_speed) as TextView
            weatherOnDate = view.findViewById(R.id.tv_lat) as TextView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastHolder {
        val view: View = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.card_view_design, parent, false)
        return ForecastHolder(view)
    }

    override fun onBindViewHolder(holder: ForecastHolder, position: Int) {

        //FILLING THE CARDS IN RECYCLERVIEW WITH INFORMATION
        holder.dateOnDate.setText(mWeatherDataList?.get(position)?.coord?.lat.toString())
        holder.tempOnDate.setText(mWeatherDataList?.get(position)?.main?.humidity.toString())
        holder.windSpeedOnDate.setText(mWeatherDataList?.get(position)?.clouds?.all.toString())

    }


    override fun getItemCount(): Int {
        return mWeatherDataList.size
    }


    fun setData(data: List<WeatherModel>) {
        mWeatherDataList = data
        notifyDataSetChanged()

    }


}