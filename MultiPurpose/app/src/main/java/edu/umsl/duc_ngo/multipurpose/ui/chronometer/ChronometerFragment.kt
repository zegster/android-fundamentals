package edu.umsl.duc_ngo.multipurpose.ui.chronometer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import edu.umsl.duc_ngo.multipurpose.R
import edu.umsl.duc_ngo.multipurpose.service.chronometer.ChronometerService
import edu.umsl.duc_ngo.multipurpose.ui.BaseFragment
import kotlinx.android.synthetic.main.chronometer_fragment.*

class ChronometerFragment : BaseFragment() {
    companion object {
        fun newInstance() = ChronometerFragment()

        private lateinit var intent: Intent

        @JvmStatic
        fun newIntentInit(context: FragmentActivity?): Intent {
            val intent = Intent(context, ChronometerActivity::class.java)
            Companion.intent = intent
            return intent
        }
    }

    /* Global Attributes */
    private lateinit var viewModel: ChronometerViewModel
    private lateinit var broadcastReceiver: ChronometerReceiver
    private lateinit var timer: CountDownTimer
    private var timerState = ChronometerViewModel.TimerState.Stopped
    private var timerLengthSeconds = 0L  //The maximum time of the timer
    private var secondsRemaining = 0L    //The remaining second left on the timer

    /* Called only once and only call again when activity was destroyed or launched again */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.let {
            ViewModelProvider(it).get(ChronometerViewModel::class.java)
        }!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.chronometer_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        _chronometer_return_button.setOnClickListener {
            activity?.onBackPressed()
        }

        _start_button.setOnClickListener {
            startTimer()
            timerState = ChronometerViewModel.TimerState.Running
            updateButton()
        }

        _pause_button.setOnClickListener {
            timer.cancel()
            timerState = ChronometerViewModel.TimerState.Paused
            updateButton()
        }

        _stop_button.setOnClickListener {
            timer.cancel()
            onTimerFinished()
        }
    }

    /* Called after onCreate() and will keep calling whenever activity come up on the screen */
    override fun onResume() {
        super.onResume()
        val serviceIntent = Intent(activity, ChronometerService::class.java)
        activity?.stopService(serviceIntent)
        initTimer()
    }

    /* Whenever user hide the application in the background, pause the timer and start the background timer */
    override fun onPause() {
        super.onPause()
        if (timerState == ChronometerViewModel.TimerState.Running) {
            timer.cancel()
            val serviceIntent = Intent(activity, ChronometerService::class.java)
            serviceIntent.putExtra(ChronometerService.SECOND_REMAINING, secondsRemaining)
            activity?.startService(serviceIntent)
        } else if (timerState == ChronometerViewModel.TimerState.Paused) {
            //TODO:
        }

        viewModel.setTimerState(timerState)
        viewModel.setPreviousTimerLengthSeconds(timerLengthSeconds)
        viewModel.setSecondsRemaining(secondsRemaining)
    }

    /* Chronometer Function */
    private fun initTimer() {
        timerState = viewModel.getTimerState()

        if (timerState == ChronometerViewModel.TimerState.Stopped) {
            setNewTimerLength()
        } else {
            setPreviousTimerLength()
        }

        secondsRemaining = if (timerState == ChronometerViewModel.TimerState.Running || timerState == ChronometerViewModel.TimerState.Paused) {
            viewModel.getSecondsRemaining()
        } else {
            timerLengthSeconds
        }

        if (timerState == ChronometerViewModel.TimerState.Running) {
            startTimer()
        }

        updateButton()
        updateCountdownUI()
    }

    private fun onTimerFinished() {
        timerState = ChronometerViewModel.TimerState.Stopped
        setNewTimerLength()
        _chronometer_progress_bar.progress = 0
        viewModel.setSecondsRemaining(timerLengthSeconds)
        secondsRemaining = timerLengthSeconds

        updateButton()
        updateCountdownUI()
    }

    private fun startTimer() {
        timerState = ChronometerViewModel.TimerState.Running
        timer = object : CountDownTimer(secondsRemaining * 1000, 1000) {
            override fun onFinish() = onTimerFinished()
            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = millisUntilFinished / 1000
                updateCountdownUI()
            }
        }.start()
    }

    private fun setNewTimerLength() {
        timerLengthSeconds = (viewModel.getTimerLength() * 60L)
        _chronometer_progress_bar.max = timerLengthSeconds.toInt()
    }

    private fun setPreviousTimerLength() {
        timerLengthSeconds = viewModel.getPreviousTimerLengthSeconds()
        _chronometer_progress_bar.max = timerLengthSeconds.toInt()
    }

    private fun updateCountdownUI() {
        val minutesLeft = secondsRemaining / 60
        val secondsInMinuteLeft = secondsRemaining - minutesLeft * 60
        val timeText =
            "$minutesLeft:${if (secondsInMinuteLeft.toString().length == 2) {
                secondsInMinuteLeft.toString()
            } else {
                "0$secondsInMinuteLeft"
            }}"
        _chornometer_time_text.text = timeText
        _chronometer_progress_bar.progress = (timerLengthSeconds - secondsRemaining).toInt()
    }

    private fun updateButton() {
        when (timerState) {
            ChronometerViewModel.TimerState.Running -> {
                _start_button.visibility = View.GONE
                _pause_button.visibility = View.VISIBLE
                _stop_button.visibility = View.VISIBLE
            }
            ChronometerViewModel.TimerState.Paused -> {
                _start_button.visibility = View.VISIBLE
                _pause_button.visibility = View.GONE
                _stop_button.visibility = View.VISIBLE
            }
            ChronometerViewModel.TimerState.Stopped -> {
                _start_button.visibility = View.VISIBLE
                _pause_button.visibility = View.GONE
                _stop_button.visibility = View.GONE
            }
        }
    }

    /* Broadcast Receiver */
    class ChronometerReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                val sec = it.getLongExtra(ChronometerService.SECOND_REMAINING, 0L)
                println(sec)
            }
        }
    }
}