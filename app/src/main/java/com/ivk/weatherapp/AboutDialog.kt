package com.ivk.weatherapp

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import kotlinx.android.synthetic.main.about_dialog.*

class AboutDialog : AppCompatDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true

    }

    fun setupHyperlink() {
        developer.setText(R.string.developer)
        developer.movementMethod = LinkMovementMethod.getInstance()

        forecast_credits.setText(R.string.forecast_credits)
        forecast_credits.movementMethod = LinkMovementMethod.getInstance()

        icons_credits.setText(R.string.icons_credits)
        icons_credits.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.about_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.setTitle(R.string.app_name)
        setupHyperlink()
        about_okButton.setOnClickListener { dismiss() }
    }
}
