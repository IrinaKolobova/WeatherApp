package com.ivk.weatherapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import kotlinx.android.synthetic.main.settings_dialog.*
import java.util.*


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
        super.onCreate(savedInstanceState)
        //setStyle(STYLE_NORMAL, R.style.SettingsDialogStyle)
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.settings_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.setTitle(R.string.menu_settings)

        switchListener()

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
                units = SETTINGS_UNITS_METRIC
                switchStatus = true
            } else {
                // the switch is disabled: set metric units
                units = SETTINGS_UNITS_IMPERIAL
                switchStatus = false
            }
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState == null) {
            readValues()
            // update switch view
            switch_units.isChecked = switchStatus
        }
    }

    private fun readValues() {
        switchListener()
    }

    private fun saveValues() {
        val newUnits = if (switch_units.isChecked) {
            "metric"
        } else {
            "imperial"
        }

        if (newUnits != units) {
            units = newUnits
        }

        SETTINGS_UNITS = units
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}