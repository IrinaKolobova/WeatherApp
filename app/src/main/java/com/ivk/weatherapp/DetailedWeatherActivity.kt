package com.ivk.weatherapp

import android.os.Bundle
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.weekday_details.*
import kotlinx.android.synthetic.main.weekday_details.description
import kotlinx.android.synthetic.main.weekday_details.sunrise_data
import kotlinx.android.synthetic.main.weekday_details.sunset_data
import kotlinx.android.synthetic.main.weekday_details.wind_data
import kotlinx.android.synthetic.main.weekday_list_items.*
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "DetailedWeatherActivity"

class DetailedWeatherActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_weather)

        activateToolbar(true)

        val weatherData = intent.getSerializableExtra(WEATHER_DATA_TRANSFER) as WeatherData

        date.text = SimpleDateFormat("EEE, MMM d", Locale.ENGLISH).format(Date(weatherData.date.toLong()*1000))
        temperature.text = weatherData.dayTemp
        description.text = weatherData.description
        wind_data.text = weatherData.windSpeed
        sunrise_data.text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(weatherData.sunrise.toLong()*1000))
        sunset_data.text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(weatherData.sunset.toLong()*1000))

        Picasso.get().load(weatherData.icon)
            .error(R.drawable.placeholder)
            .placeholder(R.drawable.placeholder)
            .into(thumbnail)


    }
}