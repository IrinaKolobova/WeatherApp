package com.ivk.weatherapp

import android.net.Uri
import android.nfc.NdefRecord.createUri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.content_main.*

private const val TAG = "MainActivity"
private val weatherRVAdapter = WeatherRVAdapter(ArrayList())

class MainActivity : AppCompatActivity(), GetRawData.OnDownloadComplete,
    GetOpenWeatherJsonData.OnDataAvailable,
    RecyclerItemClickListener.OnRecyclerClickListener {

    val OPEN_WEATHER_MAP_KEY: String = "b9331c3f8b9f662176fbd39baabf3f9a"
    val OPEN_WEATHER_MAP_BASE_URL: String = "https://api.openweathermap.org/data/2.5/onecall"
    var latitude: String = "33.441792"
    var longitude: String = "-94.037689"
    var units = "imperial"

//    val testCity: String = "San Francisco"
//    var index: Int = 0
//    companion object {
//        lateinit var weatherData: WeatherData
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate starts")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.addOnItemTouchListener(RecyclerItemClickListener(this, recycler_view, this))
        recycler_view.adapter = weatherRVAdapter

        val url = createUri(OPEN_WEATHER_MAP_BASE_URL, latitude,
                                 longitude, units,
                                "hourly,minutely", OPEN_WEATHER_MAP_KEY)
        val getRawData = GetRawData(this)
        getRawData.execute(url)
    }

    override fun onItemClick(view: View, position: Int) {
        Log.d(TAG, "onItemClick: normal tap at position $position")
    }

    override fun onItemLongClick(view: View, position: Int) {
        Log.d(TAG, "onItemLongClick: long tap at position $position")
    }

    private fun createUri(baseURL: String, latitude: String,
                          longitude: String, units: String,
                          exclude: String, key: String) : String {
        return Uri.parse(baseURL).
                buildUpon().
                appendQueryParameter("lat", latitude).
                appendQueryParameter("lon", longitude).
                appendQueryParameter("units", units).
                appendQueryParameter("exclude", exclude).
                appendQueryParameter("appid", key).
                build().toString()
    }

    override fun onDownloadComplete(data: String, status: DownloadStatus) {
        if (status == DownloadStatus.OK) {
            val getOpenWeatherJsonData = GetOpenWeatherJsonData(this)
            getOpenWeatherJsonData.execute(data)
        } else {
            Log.d(TAG, "onDownloadComplete failed: status is $status, error message is $data")
        }
    }

    override fun onDataAvailable(data: List<WeatherData>) {
        Log.d(TAG, "onDataAvailable called, data is $data")
        weatherRVAdapter.loadNewData(data)
    }

    override fun onError(exception: Exception) {
        Log.e(TAG, "onError called with ${exception.message}")
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