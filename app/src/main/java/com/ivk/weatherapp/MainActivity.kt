package com.ivk.weatherapp

import android.app.VoiceInteractor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.content_main.*
import org.json.JSONObject
import java.lang.Exception

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    val OPEN_WEATHER_MAP_URL: String = "https://api.openweathermap.org/data/2.5/onecall?lat=33.441792&lon=-94.037689&exclude=hourly,minutely&appid="
    val OPEN_WEATHER_MAP_KEY: String = "b9331c3f8b9f662176fbd39baabf3f9a"

    val testCity: String = "San Francisco"
    var index: Int = 0

    companion object {
        lateinit var weatherData: WeatherData
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate starts")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler_view.layoutManager = LinearLayoutManager(this)

        val url = "$OPEN_WEATHER_MAP_URL$OPEN_WEATHER_MAP_KEY"
        val getRawData = GetRawData()
        getRawData.execute("$url")
    }

    fun onDownloadComplete(data: String, status: DownloadStatus) {
        if (status == DownloadStatus.OK) {
            Log.d(TAG, "onDownloadComplete called successfully: data is $data")
        } else {
            Log.d(TAG, "onDownloadComplete failed: status is $status, error message is $data")
        }
    }


    /**
    // download weather data using Volley
    private fun downloadWeatherData(city:String) {
        val url = "$OPEN_WEATHER_MAP_URL&APPID=$OPEN_WEATHER_MAP_KEY"

        // request JSON
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null, Response.Listener<JSONObject> { response ->
                try {
                    // load
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.d("TAG", "downloadWeatherData: error $e")
                    Toast.makeText(this,"Error loading weather data!",Toast.LENGTH_LONG).show()
                }
            })
        val queue = Volley.newRequestQueue(applicationContext)
        queue.add(jsonObjectRequest)
    }**/
}