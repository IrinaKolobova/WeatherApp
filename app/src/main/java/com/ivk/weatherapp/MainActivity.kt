package com.ivk.weatherapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.*
import kotlin.collections.ArrayList

//TODO: clean up
//TODO: add settings
//TODO: add option to select city and units
//TODO: make detailed view opens when user click once

private const val TAG = "MainActivity"
private val weatherRVAdapter = WeatherRVAdapter(ArrayList())

class MainActivity : AppCompatActivity(), GetRawData.OnDownloadComplete,
    GetOpenWeatherJsonData.OnDataAvailable,
    RecyclerItemClickListener.OnRecyclerClickListener {

    private val OPEN_WEATHER_MAP_KEY: String = "b9331c3f8b9f662176fbd39baabf3f9a"
    private val OPEN_WEATHER_MAP_BASE_URL: String =
        "https://api.openweathermap.org/data/2.5/onecall"
    private val PERMISSION_ID = 1010

    private val units: String = "imperial"
    private var latitude: String = "0.0"
    private var longitude: String = "0.0"
    private var locationName: String = " "
    private lateinit var swipeLayout: SwipeRefreshLayout
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest


    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate starts")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        setRecyclerView()

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getLocation()

        Log.d(TAG, "onCreate: latitude = $latitude, LONGITUDE = $longitude")
        Log.d(TAG, "onCreate: location name = $locationName")

        swipeLayout = findViewById(R.id.swipeContainer)
        swipeLayout.setOnRefreshListener {
            //TODO: make refresh prettier
            //TODO: request new log&lat during refresh
            val intent = intent
            finish()
            startActivity(intent)
            //swipeLayout.isRefreshing = false

            //this.recreate()                 // refresh list contents somehow
            swipeLayout.isRefreshing = false
            /*// To keep animation for 4 seconds
            Handler().postDelayed(Runnable { // Stop animation (This will be after 3 seconds)
                swipeLayout.setRefreshing(true)
            }, 4000) // Delay in millis*/
        }
        Log.d(TAG, "onCreate finished")
    }

    private fun getLocation() {
        // check location permission
        Log.d(TAG, "getLastLocation: calling checkPermission()")
        if (checkPermission()) {
            Log.d(TAG, "getLastLocation: location permission is granted")
            // check if location service is enabled
            if (isLocationEnabled()) {
                Log.d(TAG, "getLastLocation: location service is enabled")
                // get the location
                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                    val location: Location? = task.result

                    if (location == null) {
                        // if location is null get the new user's location
                        Log.d(TAG, "getLastLocation: location is null, getting new user's location")
                        getNewLocation()
                        Log.d(
                            TAG,
                            "getLastLocation: before getNewLocation() latitude = $latitude, longitude = $longitude"
                        )
                        latitude = location?.latitude.toString()
                        longitude = location?.longitude.toString()
                        Log.d(
                            TAG,
                            "getLastLocation: after getNewLocation() latitude = $latitude, longitude = $longitude"
                        )
                        requestAPI()
                    } else {
                        // set latitude and longitude
                        Log.d(TAG, "getLastLocation: before setting data latitude = $latitude, longitude = $longitude")
                        latitude = location.latitude.toString()
                        longitude = location.longitude.toString()
                        locationName = getLocationName( location.latitude, location.longitude)
                        Log.d(TAG, "getLastLocation: after setting data latitude = $latitude, longitude = $longitude")
                        Log.d(TAG, "getLastLocation: after setting data location name = $locationName")
                        requestAPI()
                    }
                }
            } else {
                //ask user to enable location services
                Log.d(TAG, "getLastLocation: asking user to enable location services")
                //TODO: change to snackbar
                Toast.makeText(this, "Please enable your location", Toast.LENGTH_LONG).show()
            }
        } else {
            // ask for location permission
            Log.d(TAG, "getLastLocation: asking user for location permission")
            requestPermission()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getNewLocation() {
        Log.d(TAG, "getNewLocation: started")
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()
        )
        Log.d(TAG, "getNewLocation: finished")
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            Log.d(TAG, "locationCallback: onLocationResult started")
            val lastLocation = p0.lastLocation
            Log.d(TAG, "locationCallback: latitude = $latitude, longitude = $longitude")
            // set new location
            latitude = lastLocation.latitude.toString()
            longitude = lastLocation.longitude.toString()
            locationName = getLocationName(lastLocation.latitude, lastLocation.longitude)
            Log.d(TAG, "getLastLocation: new location were set \nlatitude = $latitude, longitude = $longitude")
            requestAPI()
        }
    }

    private fun checkPermission(): Boolean {
        Log.d(TAG, "checkPermission: checking location permission")
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        Log.d(TAG, "requestPermission: requesting location permission")
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "onRequestPermissionsResult: location permission granted")
                Log.d(TAG, "onRequestPermissionsResult: getting new location via getNewLocation()")
                getNewLocation()
            }
        }
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


    private fun getLocationName(lat: Double, long: Double): String {
        //var cityName:String = ""
        //var countryName = ""
        val geoCoder = Geocoder(this, Locale.getDefault())
        val address = geoCoder.getFromLocation(lat, long, 3)
        val cityName = address.get(0).locality
        val stateName = address.get(0).adminArea
        val countryName = address.get(0).countryName

        Log.d(TAG, "getCityName: cityName = $cityName,countryName = $countryName")
        toolbar.title = "$cityName, $stateName"
        return "$cityName, $stateName"
    }

}