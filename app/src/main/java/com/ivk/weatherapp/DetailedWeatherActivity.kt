package com.ivk.weatherapp

import android.os.Bundle
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.weekday_item.*

private const val TAG = "DetailedWeatherActivity"

class DetailedWeatherActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_weather)

        activateToolbar(true)

        val weatherData = intent.getSerializableExtra(WEATHER_DATA_TRANSFER) as WeatherData

        wi_date.text = weatherData.date
        wi_day.text = weatherData.day.toString()
        wi_temperature.text = weatherData.dayTemp
        wi_description.text = weatherData.description
        wi_wind.text = weatherData.windSpeed
        wi_sunrise.text = weatherData.sunrise
        wi_sunset.text = weatherData.sunset

        Picasso.get().load(weatherData.icon)
            .error(R.drawable.placeholder)
            .placeholder(R.drawable.placeholder)
            .into(wi_thumbnail)


    }
}