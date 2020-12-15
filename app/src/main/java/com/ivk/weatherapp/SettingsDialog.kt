package com.ivk.weatherapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import kotlinx.android.synthetic.main.settings_dialog.*
import kotlinx.android.synthetic.main.weekday_list_items.*
import java.util.*

private const val TAG = "SettingsDialog"

const val SETTINGS_UNITS_IMPERIAL = "imperial"
const val SETTINGS_UNITS_METRIC = "metric"
var SETTINGS_UNITS = "imperial"

class SettingsDialog(var listener: SettingsDialogListener) : AppCompatDialogFragment() {

    interface SettingsDialogListener {
        fun applySettings(unitsFromSettings: String)
    }

    // get the region to set default units
    private val defaultUnits =
        when (Locale.getDefault().country) {
            "US", "GB", "MM", "LR" -> SETTINGS_UNITS_IMPERIAL
            else -> SETTINGS_UNITS_METRIC
        }
    private var units = SETTINGS_UNITS
    private var defaultSwitchStatus = units != defaultUnits
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

        switchListener()

        Log.d(TAG, "onViewCreated: units = $units")

        settings_okButton.setOnClickListener {
            saveValues()
            listener.applySettings(units)
            dismiss()
        }

        settings_cancelButton.setOnClickListener { dismiss() }
    }

    private fun switchListener() {
        switch_units.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // the switch is enabled/checked: set imperial units
                Log.d(TAG, "switchListener: switch isChecked (true) = $isChecked")
                units = SETTINGS_UNITS_METRIC
                switchStatus = true
                Log.d(TAG, "switchListener: setting new units (metric) = $units")
            } else {
                // the switch is disabled: set metric units
                Log.d(TAG, "switchListener: switch isChecked (false) = $isChecked")
                units = SETTINGS_UNITS_IMPERIAL
                switchStatus = false
                Log.d(TAG, "switchListener: setting new units (imperial) = $units")
            }
            Log.d(TAG, "switchListener: new units = $units")
        }
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

        switchListener()

        Log.d(TAG, "readValues: ended")
        Log.d(TAG, "readValues: new switchStatus = $switchStatus")
        Log.d(TAG, "readValues: new units = $units")
    }

    private fun saveValues() {
        Log.d(TAG, "saveValues: called")
        val newUnits = if (switch_units.isChecked) {
            "metric"
        } else {
            "imperial"
        }

        val newSwitchStatus = switch_units.isChecked
        Log.d(
            TAG,
            "saveValues: newSwitchStatus = $newSwitchStatus, switchStatus = ${switch_units.isChecked}"
        )
        Log.d(TAG, "saveValues: switch_units = ${switch_units.isChecked}")
        Log.d(TAG, "saveValues: newUnits = $newUnits")
        Log.d(TAG, "saveValues: units = $units")

        if (newUnits != units) {
            units = newUnits
            Log.d(TAG, "saveValues: units = $units")
            Log.d(TAG, "saveValues: switch_units = $switch_units")
        }

        SETTINGS_UNITS = units
        Log.d(TAG, "saveValues: ended")
        Log.d(TAG, "saveValues: new switchStatus = $switchStatus")
        Log.d(TAG, "saveValues: new units = $units")
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy: called")
        super.onDestroy()
    }
}