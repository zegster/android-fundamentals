package edu.umsl.duc_ngo.multipurpose.ui.chronometer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ChronometerViewModel : ViewModel() {
    enum class TimerState {
        Running, Paused, Stopped
    }

    private var mTimerState = MutableLiveData<TimerState>()
    private var mTimerLengthSeconds = MutableLiveData<Long>()
    private var mSecondsRemaining = MutableLiveData<Long>()

    init {
        mTimerState.value = TimerState.Stopped
        mTimerLengthSeconds.value = 0L
        mSecondsRemaining.value = 0L
    }

    fun getTimerState(): TimerState {
        return mTimerState.value!!
    }

    fun setTimerState(state: TimerState) {
        mTimerState.value = state
    }

    fun getPreviousTimerLengthSeconds(): Long {
        return mTimerLengthSeconds.value!!
    }

    fun setPreviousTimerLengthSeconds(seconds: Long) {
        mTimerLengthSeconds.value = seconds
    }

    fun getTimerLength(): Int {
        return 1
    }

    fun getSecondsRemaining(): Long {
        return mSecondsRemaining.value!!
    }

    fun setSecondsRemaining(seconds: Long) {
        mSecondsRemaining.value = seconds
    }
}