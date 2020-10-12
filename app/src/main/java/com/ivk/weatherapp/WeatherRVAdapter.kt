package com.ivk.weatherapp

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "WeatherRVAdapter"
class WeatherDataViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var temperature : TextView = view.findViewById(R.id.wli_temperature)
    var date : TextView = view.findViewById(R.id.wli_date)
    var thumbnail : ImageView = view.findViewById(R.id.wli_thumbnail)
}
class WeatherRVAdapter(private var dailyWeatherDataList : List<WeatherData>) : RecyclerView.Adapter<WeatherDataViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherDataViewHolder {
        Log.d(TAG, "onCreateViewHolder: new view requested")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.weekday_list_items, parent, false)
        return WeatherDataViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: WeatherDataViewHolder, position: Int) {
        val dayWeatherItem = dailyWeatherDataList[position]

        Log.d(TAG, "onBindViewHolder ${dayWeatherItem.day} -> $position")

        //Picasso.with(holder.thumbnail.context).load(dayWeatherItem.icon)
        Picasso.get().load(dayWeatherItem.icon)
            .error(R.drawable.placeholder)
            .placeholder(R.drawable.placeholder)
            .into(holder.thumbnail)
        holder.temperature.text = dayWeatherItem.dayTemp
        holder.date.text = SimpleDateFormat("EEE, MMM d", Locale.ENGLISH).format(Date(dayWeatherItem.date.toLong()*1000))
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