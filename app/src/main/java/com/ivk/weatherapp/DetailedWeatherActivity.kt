package com.ivk.weatherapp

import android.os.Bundle

class DetailedWeatherActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_weather)
        activateToolbar(true)

    }
}