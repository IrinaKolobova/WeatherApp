package com.ivk.weatherapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import kotlinx.android.synthetic.main.settings_dialog.*
import java.util.*


private const val TAG = "SettingsDialog"

const val SETTINGS_UNITS_IMPERIAL = "imperial"
const val SETTINGS_UNITS_METRIC = "metric"
const val SETTINGS_UNITS = "imperial" // set imperial as default
const val SWITCH_STATUS = false

@Suppress("DEPRECATION")
class SettingsDialog : AppCompatDialogFragment() {

    // get the region to set default units
    private val defaultUnits =
        when (Locale.getDefault().country) {
            "US", "GB", "MM", "LR" -> SETTINGS_UNITS_IMPERIAL
            else -> SETTINGS_UNITS_METRIC
        }
    private var units = defaultUnits
    private var defaultSwitchStatus = units == defaultUnits
    private var switchStatus = defaultSwitchStatus

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate: called")
        Log.d(TAG, "onCreate: units = $units")
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

        dialog?.setTitle(R.string.menu_settings)

        settings_okButton.setOnClickListener {
            saveValues()
            dismiss()
        }

        switch_units.setOnCheckedChangeListener { _, isChecked ->
            units = if (isChecked) {
                // the switch is enabled/checked: set imperial units
                SETTINGS_UNITS_IMPERIAL
            } else {
                // the switch is disabled: set metric units
                SETTINGS_UNITS_METRIC
            }
        }

        Log.d(TAG, "onViewCreated: units = $units")

        settings_cancelButton.setOnClickListener { dismiss() }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewStateRestored: called")

        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState == null) {
            readValues()
            Log.d(TAG, "onViewStateRestored: units = $units")

            // update switch view
            switch_units.isChecked = switchStatus
        }
    }

    private fun readValues() {
        Log.d(TAG, "readValues: called")
        Log.d(TAG, "readValues: switchStatus = $switchStatus")
        Log.d(TAG, "readValues: units = $units")

        with(getDefaultSharedPreferences(context)) {
            switchStatus = getBoolean(SWITCH_STATUS.toString(), defaultSwitchStatus)
            units = getString(SETTINGS_UNITS, defaultUnits).toString()
        }

        Log.d(TAG, "readValues: new switchStatus = $switchStatus")
        Log.d(TAG, "readValues: new units = $units")
    }

    private fun saveValues() {
        val newUnits = if (switch_units.isChecked) {
            switch_units.textOn.toString()
        } else {
            switch_units.textOff.toString()
        }
        val newSwitchStatus = switch_units.isChecked
        Log.d(
            TAG,
            "saveValues: newSwitchStatus = $newSwitchStatus, switchStatus = ${switch_units.isChecked}"
        )
        Log.d(TAG, "saveValues: switch_units = ${switch_units.isChecked}")

        with(getDefaultSharedPreferences(context).edit()) {
            if (newUnits != units) {
                putString(SETTINGS_UNITS, newUnits)
                putBoolean(SWITCH_STATUS.toString(), newSwitchStatus)
            }
            apply()
        }
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy: called")
        super.onDestroy()
    }
}