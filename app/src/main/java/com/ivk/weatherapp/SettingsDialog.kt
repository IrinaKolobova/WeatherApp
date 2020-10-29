package com.ivk.weatherapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import kotlinx.android.synthetic.main.settings_dialog.*

private const val TAG = "SettingsDialog"

const val SETTINGS_UNITS_IMPERIAL = "fahrenheit"
const val SETTINGS_UNITS_METRIC = "celsius"

@Suppress("DEPRECATION")
class SettingsDialog: AppCompatDialogFragment() {

    //default units are imperial
    private val defaultUnits = SETTINGS_UNITS_IMPERIAL
    private var units = defaultUnits

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
        return inflater.inflate(R.layout.settings_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated: called")
        super.onViewCreated(view, savedInstanceState)

        dialog?.setTitle(R.string.menumain_settings)

        switch_units.setOnCheckedChangeListener { buttonView, isChecked ->
            units = if (isChecked) {
                // The switch is enabled/checked: set imperial units
                SETTINGS_UNITS_IMPERIAL
            } else {
                // The switch is disabled: set metric units
                SETTINGS_UNITS_METRIC
            }
        }

        settings_okButton.setOnClickListener{
            saveValues()
            dismiss()
        }

        settings_cancelButton.setOnClickListener{ dismiss()}
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewStateRestored: called")

        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState == null) {
            readValues()
            //TODO: add code here
        }
    }

    private fun readValues() {
        with(getDefaultSharedPreferences(context)) {
            units = getString(SETTINGS_UNITS_IMPERIAL, defaultUnits).toString()
        }
        Log.d(TAG, "Retrieving units = $units")
    }

    private fun saveValues() {
        val newUnits = switch_units.text
        Log.d(TAG, "Saving units = $newUnits")

        with(getDefaultSharedPreferences(context).edit()) {
            if (newUnits != units) {
                putString(SETTINGS_UNITS_IMPERIAL, newUnits.toString())
            }
            apply()
        }
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy: called")
        super.onDestroy()
    }
}