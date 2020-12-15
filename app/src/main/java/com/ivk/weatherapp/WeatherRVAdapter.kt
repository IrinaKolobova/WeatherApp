package com.ivk.weatherapp

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*


private const val TAG = "WeatherRVAdapter"

class WeatherDataViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var date : TextView = view.findViewById(R.id.date)
    var temperature : TextView = view.findViewById(R.id.temperature)
    var temperatureIcon: ImageView = view.findViewById(R.id.temp_unit)
    var thumbnail : ImageView = view.findViewById(R.id.thumbnail)
    var description : TextView = view.findViewById(R.id.description)
    var wind : TextView = view.findViewById(R.id.wind_data)
    var windUnits: TextView = view.findViewById(R.id.speed_units)
    var sunrise : TextView = view.findViewById(R.id.sunrise_data)
    var sunset : TextView = view.findViewById(R.id.sunset_data)
    var weatherLayout : ConstraintLayout = view.findViewById(R.id.weather_item_layout)
    var expandableLayout : ConstraintLayout = view.findViewById(R.id.expandableView)

}
class WeatherRVAdapter(private var dailyWeatherDataList: List<WeatherData>) : RecyclerView.Adapter<WeatherDataViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherDataViewHolder {
        Log.d(TAG, "onCreateViewHolder: new view requested")
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.weekday_list_items,
            parent,
            false
        )
        return WeatherDataViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: WeatherDataViewHolder, position: Int) {
        val dayWeatherItem = dailyWeatherDataList[position]
        Log.d(TAG, "onBindViewHolder ${dayWeatherItem.day} -> $position")

        holder.temperature.text = dayWeatherItem.dayTemp
        holder.date.text = SimpleDateFormat("EEE, MMM d", Locale.ENGLISH).format(Date(dayWeatherItem.date.toLong() * 1000))
        holder.description.text = dayWeatherItem.description.capitalize(Locale.getDefault())
        holder.wind.text = dayWeatherItem.windSpeed


        val isExtendable : Boolean = dailyWeatherDataList[position].expandable
        holder.expandableLayout.visibility = if(isExtendable) View.VISIBLE else View.GONE

        holder.weatherLayout.setOnClickListener {
            val weatherData = dailyWeatherDataList[position]
            weatherData.expandable = !weatherData.expandable
            notifyItemChanged(position)
        }

        val units = SETTINGS_UNITS

        if(units == "metric"){
            holder.windUnits.setText(R.string.meters_per_second)
            holder.temperatureIcon.setImageResource(R.drawable.ic_celsius)
            holder.sunrise.text = SimpleDateFormat("HH:mm").format(Date(dayWeatherItem.sunrise.toLong()*1000))
            holder.sunset.text = SimpleDateFormat("HH:mm").format(Date(dayWeatherItem.sunset.toLong()*1000))
        } else {
            holder.windUnits.setText(R.string.miles_per_hour)
            holder.temperatureIcon.setImageResource(R.drawable.ic_fahrenheit)
            holder.sunrise.text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(dayWeatherItem.sunrise.toLong()*1000))
            holder.sunset.text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(dayWeatherItem.sunset.toLong()*1000))
        }

        val iconFromAPI = when(dayWeatherItem.icon) {
            "01d" -> R.drawable.ic_01d
            "02d" -> R.drawable.ic_02d
            "03d" -> R.drawable.ic_03
            "04d" -> R.drawable.ic_04
            "09d" -> R.drawable.ic_09
            "10d" -> R.drawable.ic_10
            "11d" -> R.drawable.ic_11
            "13d" -> R.drawable.ic_13
            "50d" -> R.drawable.ic_50
            else -> R.drawable.ic_na
        }

       /*val background = when(dayWeatherItem.icon) {
            "01d" -> R.drawable.list_item_01d
            "02d" -> R.drawable.list_item_02d
            "03d" -> R.drawable.list_item_03
            "04d" -> R.drawable.list_item_04
            "09d" -> R.drawable.list_item_09
            "10d" -> R.drawable.list_item_10
            "11d" -> R.drawable.list_item_11
            "13d" -> R.drawable.list_item_13
            "50d" -> R.drawable.list_item_50
            else -> R.color.transparent
        }*/
        holder.thumbnail.setImageResource(iconFromAPI)
        //holder.weatherLayout.setBackgroundResource(background)

        //Picasso.with(holder.thumbnail.context).load(dayWeatherItem.icon)
        /*Picasso.get().load(dayWeatherItem.icon)
            .error(R.drawable.placeholder)
            .placeholder(R.drawable.placeholder)
            .into(holder.thumbnail)*/

        //holder.bind(dayWeatherItem)
    }

    override fun getItemCount(): Int {
        return if (dailyWeatherDataList.isNotEmpty()) dailyWeatherDataList.size else 0
    }

    fun loadNewData(newWeatherData: List<WeatherData>) {
        dailyWeatherDataList = newWeatherData
        notifyDataSetChanged()
    }

    fun getWeatherData(position: Int): WeatherData? {
        return if (dailyWeatherDataList.isNotEmpty()) dailyWeatherDataList[position] else null
    }


}