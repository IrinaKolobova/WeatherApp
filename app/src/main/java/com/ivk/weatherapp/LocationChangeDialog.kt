package com.ivk.weatherapp

import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import kotlinx.android.synthetic.main.location_change_dialog.*
import java.util.*


class LocationChangeDialog(var listener: LocationChangeDialogListener) : AppCompatDialogFragment() {

    interface LocationChangeDialogListener {
        fun applyLocationChange(newLatitude: Double, newLongitude: Double)
    }

    private var enteredLocation = " "
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setStyle(STYLE_NORMAL, R.style.SettingsDialogStyle)
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.location_change_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.setTitle(R.string.location_change)

        location_okButton.setOnClickListener {
            readValues()
            listener.applyLocationChange(latitude, longitude)
            dismiss()
        }

        location_cancelButton.setOnClickListener { dismiss() }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
    }

    private fun readValues() {
        enteredLocation = enterLocation.text.toString()
        getLatLon(enteredLocation)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun getLatLon(enteredText: String): String {
        val geoCoder = Geocoder(activity, Locale.getDefault())
        val address = geoCoder.getFromLocationName(enteredText, 3)
        latitude = address[0].latitude
        longitude = address[0].longitude

        return "$latitude, $longitude"
    }

}