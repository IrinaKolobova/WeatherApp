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
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.*
import kotlin.collections.ArrayList

//TODO: check if location is enabled on the phone
//TODO: make animated icons
//TODO: make location title run through toolbar like message in a bus

private val weatherRVAdapter = WeatherRVAdapter(ArrayList())

class MainActivity : AppCompatActivity(), GetRawData.OnDownloadComplete,
    GetOpenWeatherJsonData.OnDataAvailable,
    RecyclerItemClickListener.OnRecyclerClickListener,
    SettingsDialog.SettingsDialogListener,
    LocationChangeDialog.LocationChangeDialogListener {

    private val OPEN_WEATHER_MAP_KEY: String = "b9331c3f8b9f662176fbd39baabf3f9a"
    private val OPEN_WEATHER_MAP_BASE_URL: String =
        "https://api.openweathermap.org/data/2.5/onecall"
    private val PERMISSION_ID = 1010

    private var latitude: String = "0.0"
    private var longitude: String = "0.0"
    private var units = "imperial"
    private lateinit var locationName: String
    private lateinit var swipeLayout: SwipeRefreshLayout
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        units = getString(R.string.imperial)
        locationName = getString(R.string.location_unavailable)

        setRecyclerView()

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getLocation()

        swipeLayout = findViewById(R.id.swipeContainer)
        swipeLayout.setOnRefreshListener {
            getNewLocation()
            applySettings(units)
            swipeLayout.isRefreshing = false
        }
    }

    private fun getLocation() {
        // check location permission
        if (checkPermission()) {
            // check if location service is enabled
            if (isLocationEnabled()) {
                // get the location
                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                    val location: Location? = task.result

                    if (location == null) {
                        // if location is null get the new user's location
                        getNewLocation()
                        latitude = location?.latitude.toString()
                        longitude = location?.longitude.toString()
                        requestAPI()
                    } else {
                        // set latitude and longitude
                        latitude = location.latitude.toString()
                        longitude = location.longitude.toString()
                        locationName = getLocationName(location.latitude, location.longitude)
                        requestAPI()
                    }
                }
            } else {
                //ask user to enable location services
                //TODO: change to snackbar
                Toast.makeText(this, "Please enable your location", Toast.LENGTH_LONG).show()
            }
        } else {
            // ask for location permission
            requestPermission()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getNewLocation() {
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
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val newLocation = locationResult.locations[0]
            // set new location
            latitude = newLocation.latitude.toString()
            longitude = newLocation.longitude.toString()
            locationName = getLocationName(newLocation.latitude, newLocation.longitude)
            requestAPI()
        }
    }

    private fun checkPermission(): Boolean {
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
        val openWeatherUrl = createOpenWeatherUri(
            OPEN_WEATHER_MAP_BASE_URL,
            latitude, longitude, units,
            "hourly,minutely", OPEN_WEATHER_MAP_KEY
        )
        val getRawData = GetRawData(this)
        getRawData.execute(openWeatherUrl)
    }

    override fun onItemClick(view: View, position: Int) {
        weatherRVAdapter.getWeatherData(position)
    }

    override fun onItemLongTouch(view: View, position: Int) {
        weatherRVAdapter.getWeatherData(position)
    }

    private fun createOpenWeatherUri(
        baseURL: String, latitude: String,
        longitude: String, units: String,
        exclude: String, key: String
    ): String {
        return Uri.parse(baseURL).buildUpon().appendQueryParameter("lat", latitude)
            .appendQueryParameter("lon", longitude).appendQueryParameter("units", units)
            .appendQueryParameter("exclude", exclude).appendQueryParameter("appid", key).build()
            .toString()
    }

    override fun onDownloadComplete(data: String, status: DownloadStatus) {
        if (status == DownloadStatus.OK) {
            val getOpenWeatherJsonData = GetOpenWeatherJsonData(this)
            getOpenWeatherJsonData.execute(data)
        }
    }

    override fun onDataAvailable(data: List<WeatherData>) {
        weatherRVAdapter.loadNewData(data)
    }

    override fun onError(exception: Exception) {
    }


    private fun getLocationName(lat: Double, long: Double): String {
        val geoCoder = Geocoder(this, Locale.getDefault())
        val address = geoCoder.getFromLocation(lat, long, 3)
        val cityName = address.get(0).locality
        val stateName = address.get(0).adminArea

        toolbar.locationName.text = getString(R.string.location_name, cityName, stateName)
        return "$cityName, $stateName"
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.menu_settings -> {
                val dialog = SettingsDialog(this)
                dialog.show(supportFragmentManager, null)
                return true
            }
            R.id.menu_changeLocation -> {
                val dialog = LocationChangeDialog(this)
                dialog.show(supportFragmentManager, null)
                return true
            }
            R.id.menu_about -> {
                val dialog = AboutDialog()
                dialog.show(supportFragmentManager, null)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }

        return super.onOptionsItemSelected(item)
    }

    override fun applySettings(unitsFromSettings: String) {
        units = unitsFromSettings
        // TODO: Convert units without using API
        requestAPI()
    }

    fun changeLocation(view: View) {
        val dialog = LocationChangeDialog(this)
        dialog.show(supportFragmentManager, null)
    }

    override fun applyLocationChange(newLatitude: Double, newLongitude: Double) {
        latitude = newLatitude.toString()
        longitude = newLongitude.toString()
        requestAPI()
        getLocationName(newLatitude, newLongitude)
    }
}