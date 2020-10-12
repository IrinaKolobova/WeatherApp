package com.ivk.weatherapp

import android.os.Bundle
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.weekday_item.*
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "DetailedWeatherActivity"

class DetailedWeatherActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_weather)

        activateToolbar(true)

        val weatherData = intent.getSerializableExtra(WEATHER_DATA_TRANSFER) as WeatherData

        wi_date.text = SimpleDateFormat("EEE, MMM d", Locale.ENGLISH).format(Date(weatherData.date.toLong()*1000))
        wi_temperature.text = weatherData.dayTemp
        wi_description.text = weatherData.description
        wi_wind.text = weatherData.windSpeed
        wi_sunrise.text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(weatherData.sunrise.toLong()*1000))
        wi_sunset.text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(weatherData.sunset.toLong()*1000))

        Picasso.get().load(weatherData.icon)
            .error(R.drawable.placeholder)
            .placeholder(R.drawable.placeholder)
            .into(wi_thumbnail)


    }
}