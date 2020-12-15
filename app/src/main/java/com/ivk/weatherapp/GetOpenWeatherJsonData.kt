package com.ivk.weatherapp

import android.os.AsyncTask
import org.json.JSONException
import org.json.JSONObject

class GetOpenWeatherJsonData(private val listener: OnDataAvailable) :
    AsyncTask<String, Void, ArrayList<WeatherData>>() {
    interface OnDataAvailable {
        fun onDataAvailable(data: List<WeatherData>)
        fun onError(exception: Exception)
    }

    override fun doInBackground(vararg params: String): ArrayList<WeatherData> {
        val dailyWeatherList = ArrayList<WeatherData>()

        try {
            val jsonData = JSONObject(params[0])
            val latitude = jsonData.getString("lat")
            val longitude = jsonData.getString("lon")
            val currentTemp = jsonData.getJSONObject("current").getString("temp")

            val dailyArray = jsonData.getJSONArray("daily")
            for (i in 0 until 7) {
                val jsonWeatherData = dailyArray.getJSONObject(i)
                val date = jsonWeatherData.getString("dt")
                val day = i
                val windSpeed = jsonWeatherData.getString("wind_speed")
                val sunrise = jsonWeatherData.getString("sunrise")
                val sunset = jsonWeatherData.getString("sunset")

                val jsonTemp = jsonWeatherData.getJSONObject("temp")
                val dayTemp = jsonTemp.getString("day")
                val nightTemp = jsonTemp.getString("night")

                val jsonWeather = jsonWeatherData.getJSONArray("weather")
                val description = jsonWeather.getJSONObject(0).getString("description")
                val icon = jsonWeather.getJSONObject(0).getString("icon")

                val weatherObject = WeatherData(
                    latitude, longitude, date, day, currentTemp, dayTemp,
                    nightTemp, description, windSpeed, sunrise, sunset, icon
                )

                dailyWeatherList.add(weatherObject)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
            cancel(true)
            listener.onError(e)
        }

        return dailyWeatherList
    }

    override fun onPostExecute(result: ArrayList<WeatherData>) {
        super.onPostExecute(result)
        listener.onDataAvailable(result)
    }
}