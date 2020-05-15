package edu.umsl.duc_ngo.multipurpose.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import edu.umsl.duc_ngo.multipurpose.R
import edu.umsl.duc_ngo.multipurpose.ui.BaseFragment
import edu.umsl.duc_ngo.multipurpose.ui.chronometer.ChronometerFragment
import edu.umsl.duc_ngo.multipurpose.ui.note.list.NoteListFragment
import edu.umsl.duc_ngo.multipurpose.ui.weather.WeatherFragment
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : BaseFragment() {
    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        /* Starting the note application */
        _note_app_button.setOnClickListener {
            val intent = NoteListFragment.newIntentInit(activity)
            startActivity(intent)
        }

        /* Starting the weather application */
        _weather_app_button.setOnClickListener {
            val intent = WeatherFragment.newIntentInit(activity)
            startActivity(intent)
        }

        /* Starting the chronometer application */
        _chronometer_app_button.setOnClickListener {
            val intent = ChronometerFragment.newIntentInit(activity)
            startActivity(intent)
        }

        /* Starting the counter application */
        _counter_app_button.setOnClickListener {
            Toasty.info(context!!, "Not Available. Work In Progress", Toast.LENGTH_SHORT, true).show()
        }

        /* Starting the alarm application */
        _alarm_app_button.setOnClickListener {
            Toasty.info(context!!, "Not Available. Work In Progress", Toast.LENGTH_SHORT, true).show()
        }

        /* Starting the reminder application */
        _reminder_app_button.setOnClickListener {
            Toasty.info(context!!, "Not Available. Work In Progress", Toast.LENGTH_SHORT, true).show()
        }
    }
}