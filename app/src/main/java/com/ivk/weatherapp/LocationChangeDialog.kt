package com.ivk.weatherapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import kotlinx.android.synthetic.main.location_change_dialog.*

private const val TAG = "LocationChangeDialog"

class LocationChangeDialog(var listener: LocationChangeDialogListener) : AppCompatDialogFragment() {
    // TODO: add Geocoder to read location

        interface LocationChangeDialogListener {
            fun applyLocationChange(newLocation: String)
        }

        private var location = SETTINGS_UNITS

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

            location_okButton.setOnClickListener {
                readValues()
                listener.applyLocationChange(location)
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
            Log.d(TAG, "readValues: location = $")

            location = enterLocation.text.toString()

            Log.d(TAG, "readValues: ended")
            Log.d(TAG, "readValues: new location = $location")
        }

        override fun onDestroy() {
            Log.d(TAG, "onDestroy: called")
            super.onDestroy()
        }
}