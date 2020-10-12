package com.ivk.weatherapp

import android.util.Log
import java.io.IOException
import java.io.ObjectStreamException
import java.io.Serializable

class WeatherData (var latitude: String, var longitude: String, var date: String, var day: Int,
                   var currentTemp: String, var dayTemp: String, var nightTemp: String,
                   var description: String, var windSpeed: String, var sunrise: String,
                   var sunset: String, var icon: String) : Serializable{

    companion object{
        private const val serialVersionUID = 1L
    }

    override fun toString(): String {
        return "WeatherData(latitude='$latitude', longitude='$longitude', date='$date', day=$day, " +
                "currentTemp='$currentTemp', dayTemp='$dayTemp', nightTemp='$nightTemp', " +
                "description='$description', windSpeed='$windSpeed', sunrise='$sunrise', " +
                "sunset='$sunset', icon='$icon')"
    }

    @Throws(IOException::class)
    private fun writeObject(out: java.io.ObjectOutputStream) {
        out.writeUTF(latitude)
        out.writeUTF(longitude)
        out.writeUTF(date)
        out.writeUTF(currentTemp)
        out.writeUTF(dayTemp)
        out.writeUTF(nightTemp)
        out.writeUTF(description)
        out.writeUTF(windSpeed)
        out.writeUTF(sunrise)
        out.writeUTF(sunset)
        out.writeUTF(icon)
    }

    @Throws(IOException::class, ClassNotFoundException::class)
    private fun readObject(inStream: java.io.ObjectInputStream) {
        latitude = inStream.readUTF()
        longitude = inStream.readUTF()
        date = inStream.readUTF()
        //day = inStream.readUTF()
        currentTemp = inStream.readUTF()
        dayTemp = inStream.readUTF()
        nightTemp = inStream.readUTF()
        description = inStream.readUTF()
        windSpeed = inStream.readUTF()
        sunrise = inStream.readUTF()
        sunset = inStream.readUTF()
        icon = inStream.readUTF()
    }

    @Throws(ObjectStreamException::class)
    private fun readObjectNoData() {

    }
}