package edu.umsl.duc_ngo.multipurpose.ui.weather

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import edu.umsl.duc_ngo.multipurpose.R
import edu.umsl.duc_ngo.multipurpose.ui.BaseDialogFragment
import kotlinx.android.synthetic.main.weather_dialog_fragment.view.*
import java.util.*

class SettingWeatherDialogFragment : BaseDialogFragment() {
    companion object {
        fun newInstance() = SettingWeatherDialogFragment()
    }

    private lateinit var viewModel: WeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.let {
            ViewModelProvider(it).get(WeatherViewModel::class.java)
        }!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.chronometer_dialog_fragment, container, false)
    }


    @SuppressLint("InflateParams", "DefaultLocale")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        return activity?.let {
            /* Dialog Configuration */
            /* Inflate and set the layout for the dialog
            NOTE: Pass null as the parent view because its going in the dialog layout. IDE will complain, but it's fine. */
            val mDialogView = requireActivity().layoutInflater.inflate(R.layout.weather_dialog_fragment, null)
            val mBuilder = AlertDialog.Builder(it).setView(mDialogView)

            /* Dialog Input */
            mDialogView._weather_zipcode_edit_text.setText(WeatherPrefUtil.getZipCode(context!!).toString())
            mDialogView._weather_country_edit_text.setText(WeatherPrefUtil.getCountryCode(context!!))
            mDialogView._weather_use_location_switch.isChecked = WeatherPrefUtil.getUseLocation(context!!)

            /* Dialog Cancellation */
            mDialogView._weather_cancel_button.setOnClickListener {
                dismiss()
            }

            /* Dialog Submission */
            mDialogView._weather_submit_button.setOnClickListener {
                WeatherPrefUtil.setZipCode(context!!, mDialogView._weather_zipcode_edit_text.text.toString().toInt())
                WeatherPrefUtil.setCountryCode(context!!, mDialogView._weather_country_edit_text.text.toString())
                WeatherPrefUtil.setUseLocation(context!!, mDialogView._weather_use_location_switch.isChecked)
                viewModel.setIsFetch(true)
                dismiss()
            }

            mBuilder.create()
        } ?: throw IllegalStateException("Activity cannot be null!")
    }
}