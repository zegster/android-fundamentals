package edu.umsl.duc_ngo.multipurpose.ui.chronometer

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
import kotlinx.android.synthetic.main.chronometer_dialog_fragment.view.*
import java.util.concurrent.TimeUnit

class SettingChronometerDialogFragment : BaseDialogFragment() {
    companion object {
        fun newInstance() = SettingChronometerDialogFragment()
    }

    private lateinit var viewModel: ChronometerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.let {
            ViewModelProvider(it).get(ChronometerViewModel::class.java)
        }!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.chronometer_dialog_fragment, container, false)
    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        return activity?.let {
            /* Dialog Configuration */
            /* Inflate and set the layout for the dialog
            NOTE: Pass null as the parent view because its going in the dialog layout. IDE will complain, but it's fine. */
            val mDialogView = requireActivity().layoutInflater.inflate(R.layout.chronometer_dialog_fragment, null)
            val mBuilder = AlertDialog.Builder(it).setView(mDialogView)

            /* Dialog Input: time picker configuration */
            val timeNumber = Array(100) { n -> n.toString().padStart(2, '0') }
            mDialogView._chronometer_hours_picker.minValue = 0
            mDialogView._chronometer_hours_picker.maxValue = 99
            mDialogView._chronometer_hours_picker.displayedValues = timeNumber

            mDialogView._chronometer_minutes_picker.minValue = 0
            mDialogView._chronometer_minutes_picker.maxValue = 59
            mDialogView._chronometer_minutes_picker.displayedValues = timeNumber

            mDialogView._chronometer_seconds_picker.minValue = 0
            mDialogView._chronometer_seconds_picker.maxValue = 59
            mDialogView._chronometer_seconds_picker.displayedValues = timeNumber

            /* Dialog Cancellation */
            mDialogView._chronometer_cancel_button.setOnClickListener {
                dismiss()
            }

            /* Dialog Submission */
            mDialogView._chronometer_submit_button.setOnClickListener {
                val hours = mDialogView._chronometer_hours_picker.value.toLong()
                val minutes = mDialogView._chronometer_minutes_picker.value.toLong()
                val seconds = mDialogView._chronometer_seconds_picker.value.toLong()
                val totalSeconds = TimeUnit.HOURS.toSeconds(hours) + TimeUnit.MINUTES.toSeconds(minutes) + seconds
                viewModel.setTimerLength(totalSeconds)
                dismiss()
            }

            mBuilder.create()
        } ?: throw IllegalStateException("Activity cannot be null!")
    }
}