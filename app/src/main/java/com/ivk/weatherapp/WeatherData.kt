package com.ivk.weatherapp

class WeatherData (val latitude: String, val longitude: String, val date: String, val day: Int,
                   val currentTemp: String, val dayTemp: String, val nightTemp: String,
                   val description: String, val windSpeed: String, val sunrise: String,
                   val sunset: String, val icon: String){

    override fun toString(): String {
        return "WeatherData(latitude='$latitude', longitude='$longitude', date='$date', day=$day, " +
                "currentTemp='$currentTemp', dayTemp='$dayTemp', nightTemp='$nightTemp', " +
                "description='$description', windSpeed='$windSpeed', sunrise='$sunrise', " +
                "sunset='$sunset', icon='$icon')"
    }
}