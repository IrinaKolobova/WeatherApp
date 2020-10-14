package com.ivk.weatherapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.*
import kotlin.collections.ArrayList


private const val TAG = "MainActivity"
private val weatherRVAdapter = WeatherRVAdapter(ArrayList())

class MainActivity : AppCompatActivity(), GetRawData.OnDownloadComplete,
    GetOpenWeatherJsonData.OnDataAvailable,
    RecyclerItemClickListener.OnRecyclerClickListener {

    private val OPEN_WEATHER_MAP_KEY: String = "b9331c3f8b9f662176fbd39baabf3f9a"
    private val OPEN_WEATHER_MAP_BASE_URL: String =
        "https://api.openweathermap.org/data/2.5/onecall"
    private val units: String = "imperial"
    private var latitude: String = "0.0"
    private var longitude: String = "0.0"
    private val requestPermissionCode = 1
    private var mLocation: Location? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var cityName: String = " "
    private var stateName: String = " "
    private var countryName: String = " "


    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate starts")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        setRecyclerView()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()
        Log.d(TAG, "onCreate: latitude = $latitude, LONGITUDE = $longitude")

        //getLocationName()
        Log.d(
            TAG,
            "onCreate: cityName = $cityName, stateName = $stateName, countryName = $countryName"
        )
        toolbar.title = cityName
    }

    private fun setRecyclerView() {
        recycler_view.adapter = weatherRVAdapter
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.addOnItemTouchListener(RecyclerItemClickListener(this, recycler_view, this))
        recycler_view.setHasFixedSize(true)
    }

    private fun requestAPI() {
        Log.d(TAG, "requestAPI: latitude = $latitude, longitude = $longitude")
        val openWeatherUrl = createOpenWeatherUri(
            OPEN_WEATHER_MAP_BASE_URL,
            latitude, longitude, units,
            "hourly,minutely", OPEN_WEATHER_MAP_KEY
        )
        val getRawData = GetRawData(this)
        getRawData.execute(openWeatherUrl)
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        Log.d(TAG, "getLastLocation: starts")
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d(TAG, "getLastLocation: calling requestPermission()1")
            requestPermission()
        } else {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    Log.d(TAG, "getLastLocation: callback")
                    mLocation = location
                    if (location != null) {
                        latitude = location.latitude.toString()
                        longitude = location.longitude.toString()

                        Log.d(TAG, "getLastLocation: latitude = $latitude, longitude = $longitude")
                        requestAPI()
                    } else {
                        Log.d(TAG, "getLastLocation: calling requestNewLocationData()1")
                        requestNewLocationData()
                    }
                }
            Log.d(TAG, "getLastLocation: callback completed")

        }
        Log.d(TAG, "getLastLocation: calling requestNewLocationData()2")
        requestNewLocationData()
        Log.d(TAG, "getLastLocation: completed")
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        Log.d(TAG, "requestNewLocationData: starts")
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermission()
        }
        Log.d(TAG, "requestNewLocationData: permission granted")
        val locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1

        //TODO: find a way to update latitude&longitude here
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        val client: SettingsClient = LocationServices.getSettingsClient(this)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.requestLocationUpdates(
            locationRequest, locationCallback,
            Looper.myLooper()
        )

        //latitude = fusedLocationClient.lastLocation.toString()
        Log.d(TAG, "requestNewLocationData: latitude = $latitude, longitude = $longitude")
        requestAPI()

        Log.d(TAG, "requestNewLocationData: finished")
    }


    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var lastLocation: Location = locationResult.lastLocation
            Log.d(TAG, "locationCallback: ${lastLocation.longitude}")
            toolbar.title =
                "You Last Location is : Long: ${lastLocation.longitude} , Lat: ${lastLocation.latitude}"
            cityName = getCityName(lastLocation.latitude, lastLocation.longitude)
        }
        /*if (locationResult.locations.isNotEmpty()) {
            var lastLocation: Location = locationResult.lastLocation
            Log.d("Debug:","your last last location: "+ lastLocation.longitude.toString())
            toolbar.title = "You Last Location is : Long: ${lastLocation.longitude} , Lat: ${lastLocation.latitude}"
            cityName = getCityName(lastLocation.latitude,lastLocation.longitude)
        }*/
        /*val location = locationResult.lastLocation
        Log.e("location", location.toString())

        latitude = locationResult.lastLocation.latitude.toString()
        longitude = locationResult.lastLocation.longitude.toString()
        Log.d(TAG, "onLocationResult: latitude = $latitude, longitude = $longitude")

        val addresses: List<Address>?
        val geoCoder = Geocoder(applicationContext, Locale.getDefault())
        addresses = geoCoder.getFromLocation(
            locationResult.lastLocation.latitude,
            locationResult.lastLocation.longitude,
            1
        )
        if (addresses != null && addresses.isNotEmpty()) {
            val address: String = addresses[0].getAddressLine(0)
            val city: String = addresses[0].locality
            val state: String = addresses[0].adminArea
            val country: String = addresses[0].countryName
            val postalCode: String = addresses[0].postalCode
            Log.e("location", "$address $city $state $postalCode $country")
        }*/
    }

    /*private fun onLocationResult(locationResult: LocationResult) {
        Log.d(TAG, "locationCallback starts")
        var lastLocation: Location = locationResult.lastLocation
        latitude = lastLocation.latitude.toString()
        longitude = lastLocation.longitude.toString()
        Log.d(TAG, "locationCallback completed: latitude = $latitude, longitude = $longitude, lastLocation = $lastLocation")
        //Toast.makeText(applicationContext, "latitude: $latitude \nlongitude: $longitude", Toast.LENGTH_LONG).show()
    }*/


    private fun requestPermission() {
        Log.d(TAG, "requestPermission: started")
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            requestPermissionCode
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == requestPermissionCode) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }

    override fun onItemClick(view: View, position: Int) {
        Log.d(TAG, "onItemClick: normal tap at position $position")
        //val weatherData = weatherRVAdapter.getWeatherData(position)
    }

    override fun onItemLongClick(view: View, position: Int) {
        Log.d(TAG, "onItemLongClick: long tap at position $position")
    }

    private fun createOpenWeatherUri(
        baseURL: String, latitude: String,
        longitude: String, units: String,
        exclude: String, key: String
    ): String {
        Log.d(TAG, "createOpenWeatherUri: latitude = $latitude, longitude = $longitude")
        return Uri.parse(baseURL).buildUpon().appendQueryParameter("lat", latitude)
            .appendQueryParameter("lon", longitude).appendQueryParameter("units", units)
            .appendQueryParameter("exclude", exclude).appendQueryParameter("appid", key).build()
            .toString()
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


    private fun getCityName(lat: Double, long: Double): String {
        //var cityName:String = ""
        //var countryName = ""
        var geoCoder = Geocoder(this, Locale.getDefault())
        var address = geoCoder.getFromLocation(lat, long, 3)

        cityName = address.get(0).locality
        countryName = address.get(0).countryName
        Log.d("Debug:", "Your City: " + cityName + " ; your Country " + countryName)
        return cityName
    }

}