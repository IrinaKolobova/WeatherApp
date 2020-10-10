package com.ivk.weatherapp

import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

internal const val WEATHER_DATA_QUERY = "WEATHER_DATA_QUERY"
internal const val WEATHER_DATA_TRANSFER = "WEATHER_DATA_TRANSFER"

private const val TAG = "BaseActivity"

open class BaseActivity : AppCompatActivity() {

    internal fun activateToolbar(enableHome: Boolean){
        Log.d(TAG, ".activateToolbar")

        val toolbar =  findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(enableHome)
    }

}