package edu.umsl.duc_ngo.multipurpose.ui.chronometer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import edu.umsl.duc_ngo.multipurpose.R
import edu.umsl.duc_ngo.multipurpose.service.chronometer.ChronometerService
import edu.umsl.duc_ngo.multipurpose.service.chronometer.ChronometerService.Companion.CHRONOMETER_SERVICE
import edu.umsl.duc_ngo.multipurpose.service.chronometer.ChronometerService.Companion.TIMER_REMAINING
import edu.umsl.duc_ngo.multipurpose.ui.BaseFragment
import kotlinx.android.synthetic.main.chronometer_fragment.*
import kotlin.math.floor

private const val TAG = "ChronometerFragment"

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
    private var broadcastReceiver: BroadcastReceiver = ChronometerReceiver()
    private var timer: CountDownTimer? = null
    private var timerState = ChronometerViewModel.TimerState.Stopped
    private var timerLength = 0L
    private var timerRemaining = 0L

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
        val animationDrawable: AnimationDrawable = _chronometer_background.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(4000)
        animationDrawable.setExitFadeDuration(4000)
        animationDrawable.start()

        _chronometer_return_button.setOnClickListener {
            activity?.onBackPressed()
        }

        _chronometer_setting_button.setOnClickListener {
            SettingChronometerDialogFragment.newInstance().show(parentFragmentManager, "SettingChronometerDialog")
        }

        _start_button.setOnClickListener {
            startTimer()
            timerState = ChronometerViewModel.TimerState.Running
            updateButton()
        }

        _pause_button.setOnClickListener {
            timer?.cancel()
            timerState = ChronometerViewModel.TimerState.Paused
            updateButton()
        }

        _stop_button.setOnClickListener {
            timer?.cancel()
            onTimerFinished()
        }

        viewModel.getTimerLength().observe(viewLifecycleOwner, Observer {
            timerLength = it
            onTimerFinished()
            initTimer()
        })
    }

    /* Called after onCreate() and will keep calling whenever activity come up on the screen */
    /* If register a receiver in onResume(), you unregister it in onPause() */
    /* If register a receiver in onCreate(), you unregister it in onDestroy() */
    override fun onResume() {
        activity?.registerReceiver(broadcastReceiver, IntentFilter(CHRONOMETER_SERVICE))
        activity?.stopService(Intent(activity, ChronometerService::class.java))
        initTimer()
        super.onResume()
    }

    /* Whenever user hide the application in the background, pause the timer and start the background timer */
    /* If register a receiver in onResume(), you unregister it in onPause() */
    /* If register a receiver in onCreate(), you unregister it in onDestroy() */
    override fun onPause() {
        super.onPause()
        if (timerState == ChronometerViewModel.TimerState.Running) {
            timer?.cancel()
            val serviceIntent = Intent(activity, ChronometerService::class.java)
            serviceIntent.putExtra(CHRONOMETER_SERVICE, timerRemaining)
            activity?.startService(serviceIntent)
        }

        viewModel.setTimerState(timerState)
        viewModel.setPreviousTimerLength(timerLength)
        viewModel.setTimerRemaining(timerRemaining)
        activity?.unregisterReceiver(broadcastReceiver)
    }

    /* Chronometer Function */
    private fun initTimer() {
        timerState = viewModel.getTimerState()

        if (timerState == ChronometerViewModel.TimerState.Stopped) {
            setNewTimerLength()
        } else {
            setPreviousTimerLength()
        }

        timerRemaining = if (timerState == ChronometerViewModel.TimerState.Running || timerState == ChronometerViewModel.TimerState.Paused) {
            viewModel.getTimerRemaining()
        } else {
            timerLength
        }

        if (timerState == ChronometerViewModel.TimerState.Running) {
            startTimer()
        }

        updateButton()
        updateCountdownUI()
    }

    private fun onTimerFinished() {
        timer?.cancel()
        timerState = ChronometerViewModel.TimerState.Stopped

        _chronometer_progress_bar.progress = 0
        setNewTimerLength()
        timerRemaining = timerLength

        viewModel.setTimerState(ChronometerViewModel.TimerState.Stopped)
        viewModel.setPreviousTimerLength(timerLength)
        viewModel.setTimerRemaining(timerLength)

        updateButton()
        updateCountdownUI()
    }

    private fun startTimer() {
        timerState = ChronometerViewModel.TimerState.Running
        timer = object : CountDownTimer(timerRemaining * 1000, 1000) {
            override fun onFinish() = onTimerFinished()
            override fun onTick(millisUntilFinished: Long) {
                timerRemaining = millisUntilFinished / 1000
                updateCountdownUI()
            }
        }.start()
    }

    private fun setNewTimerLength() {
        _chronometer_progress_bar.max = timerLength.toInt()
    }

    private fun setPreviousTimerLength() {
        timerLength = viewModel.getPreviousTimerLength()
        _chronometer_progress_bar.max = timerLength.toInt()
    }

    private fun updateCountdownUI() {
        val hoursLeft = floor((timerRemaining / 3600).toDouble()).toLong()
        val minutesLeft = floor(((timerRemaining - (hoursLeft * 3600)) / 60).toDouble()).toLong()
        val secondsLeft = timerRemaining - (hoursLeft * 3600) - (minutesLeft * 60)
        val hoursText = hoursLeft.toString().padStart(2, '0')
        val minutesText = minutesLeft.toString().padStart(2, '0')
        val secondsText = secondsLeft.toString().padStart(2, '0')
        val timeText = "$hoursText:$minutesText:$secondsText"

        _chornometer_time_text.text = timeText
        _chronometer_progress_bar.progress = (timerLength - timerRemaining).toInt()

        val progressPercent = timerRemaining.toDouble() / timerLength.toDouble()
        when {
            progressPercent <= 0.25 -> {
                _chronometer_progress_bar.currentDrawable?.setTint(Color.parseColor("#FF6464"))
            }
            progressPercent <= 0.5 -> {
                _chronometer_progress_bar.currentDrawable?.setTint(Color.parseColor("#FFEF64"))
            }
            else -> {
                _chronometer_progress_bar.currentDrawable?.setTint(Color.parseColor("#B7FF64"))
            }
        }
    }

    private fun updateButton() {
        when (timerState) {
            ChronometerViewModel.TimerState.Running -> {
                _start_button.visibility = View.GONE
                _pause_button.visibility = View.VISIBLE
                _stop_button.visibility = View.VISIBLE
                _chronometer_setting_button.isEnabled = false
                _chronometer_setting_button.background.setTint(Color.parseColor("#3CF1EBF1"))
            }
            ChronometerViewModel.TimerState.Paused -> {
                _start_button.visibility = View.VISIBLE
                _pause_button.visibility = View.GONE
                _stop_button.visibility = View.VISIBLE
                _chronometer_setting_button.isEnabled = false
                _chronometer_setting_button.background.setTint(Color.parseColor("#3CF1EBF1"))
            }
            ChronometerViewModel.TimerState.Stopped -> {
                _start_button.visibility = View.VISIBLE
                _pause_button.visibility = View.GONE
                _stop_button.visibility = View.GONE
                _chronometer_setting_button.isEnabled = true
                _chronometer_setting_button.background.setTint(Color.parseColor("#00BCD4"))
            }
        }
    }

    inner class ChronometerReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                onTimerFinished()
                viewModel.setTimerState(ChronometerViewModel.TimerState.Running)
                viewModel.setPreviousTimerLength(timerLength)
                viewModel.setTimerRemaining(it.getLongExtra(TIMER_REMAINING, 0L))
                initTimer()
            }
        }
    }
}