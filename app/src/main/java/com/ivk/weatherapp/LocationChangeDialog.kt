package com.ivk.weatherapp

import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import kotlinx.android.synthetic.main.location_change_dialog.*
import java.util.*

private const val TAG = "LocationChangeDialog"

class LocationChangeDialog(var listener: LocationChangeDialogListener) : AppCompatDialogFragment() {

    interface LocationChangeDialogListener {
        fun applyLocationChange(latitude: Double, longitude: Double)
    }

    private var enteredLocation = " "
    private var latitude : Double = 0.0
    private var longitude : Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate: called")
        super.onCreate(savedInstanceState)
        //setStyle(STYLE_NORMAL, R.style.SettingsDialogStyle)
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView: called")
        return inflater.inflate(R.layout.location_change_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated: called")
        super.onViewCreated(view, savedInstanceState)

        dialog?.setTitle(R.string.location_change)

        Log.d(TAG, "onViewCreated: ")

       /* enterLocation.setOnKeyListener(View.OnKeyListener { _, keyCode, keyevent ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && keyevent.action == KeyEvent.ACTION_UP) {
                val enteredText = enterLocation.text.toString()
                getLatLon(enteredText)
                return@OnKeyListener true
            }
            false
        })*/

        location_okButton.setOnClickListener {
            readValues()
            listener.applyLocationChange(latitude, longitude)
            dismiss()
        }

        location_cancelButton.setOnClickListener { dismiss() }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewStateRestored: called")

        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState == null) {
            Log.d(TAG, "onViewStateRestored: ")
        }
    }

    private fun readValues() {
        Log.d(TAG, "readValues: called")
        Log.d(TAG, "readValues: location = $enteredLocation")

        enteredLocation = enterLocation.text.toString()
        getLatLon(enteredLocation)


        Log.d(TAG, "readValues: ended")
        Log.d(TAG, "readValues: new location = $enteredLocation")
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy: called")
        super.onDestroy()
    }

    private fun getLatLon(enteredText : String): String {
        val geoCoder = Geocoder(activity, Locale.getDefault())
        val address = geoCoder.getFromLocationName(enteredText, 3)
        latitude = address[0].latitude
        longitude = address[0].longitude
        Log.d(TAG, "getLatLon: latitude = $latitude, longitude = $longitude")

        return "$latitude, $longitude"
    }

    /*val geoCoder = Geocoder(this, Locale.getDefault())
       val address = geoCoder.getFromLocation(lat, long, 3)
       val cityName = address.get(0).locality
       val stateName = address.get(0).adminArea
       val countryName = address.get(0).countryName

       Log.d(TAG, "getCityName: cityName = $cityName,countryName = $countryName")
       toolbar.locationName.text = getString(R.string.location_name, cityName, stateName)
       return "$cityName, $stateName"*/

   /*private fun getLatLon(strAddress: String) {
        val geoCoder = Geocoder()
        val address : List<Address>
        val p1 : GeoPoint = null

        try {
            address = coder.getFromLocationName(strAddress,5)
            //if (address==null) { }
            val location=address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = (location.getLatitude().toDouble() * 1E6),
            (location.getLongitude() * 1E6)
        } catch (IOException ex) {

            ex.printStackTrace();
        }
    }

    private fun getAddress(latLng: LatLng): String {

        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses: List<Address>?
        val address: Address?
        var addressText = ""

        addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)

        if (addresses.isNotEmpty()) {
            address = addresses[0]
            addressText = address.getAddressLine(0)
        }else{
            addressText = "its not appear"
        }
        return addressText
    }*/


}