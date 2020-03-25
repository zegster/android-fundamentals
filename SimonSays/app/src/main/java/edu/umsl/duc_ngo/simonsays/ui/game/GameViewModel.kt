package edu.umsl.duc_ngo.simonsays.ui.game

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {
    private var currentScore = MutableLiveData<Int>()

    private var difficulty = MutableLiveData<Int>()
    private var isGeneratingSequence = MutableLiveData<Boolean>()
    private var currentIndex = MutableLiveData<Int>()
    private var sequenceList = mutableListOf<Int>()
    private var simonSequence = MutableLiveData<List<Int>>()

    private var isGameStarted = MutableLiveData<Boolean>()
    private var isFinishUpdate = MutableLiveData<Boolean>()
    private var isGameOver = MutableLiveData<Boolean>()
    private var isScoreRegister = MutableLiveData<Boolean>()

    private var timer: CountDownTimer? = null
    private var timeLeft = MutableLiveData<Long>()

    init {
        currentScore.value = 0

        difficulty.value = 0
        isGeneratingSequence.value = true
        currentIndex.value = 0
        simonSequence.value = sequenceList

        isGameStarted.value = false
        isFinishUpdate.value = false
        isGameOver.value = false
        isScoreRegister.value = false
    }

    /* Score */
    private fun incrementScore() {
        currentScore.value = currentScore.value?.plus(1)
    }

    fun getScore(): LiveData<Int> {
        return currentScore
    }

    /* Simon Sequence */
    private fun resetIndex() {
        currentIndex.value = 0
    }

    private fun incrementIndex() {
        currentIndex.value = currentIndex.value?.plus(1)
    }

    fun getSequence(): LiveData<List<Int>> {
        return simonSequence
    }

    fun initSequence(difficulty: Int) {
        if(isGameStarted.value == false) {
            this.difficulty.value = difficulty
            generateRandomSequence(difficulty)
            isGameStarted.value = true
            timeLeft.value = ((9000 - (3000 * difficulty.toLong())) / 1000) % 60
        }
    }

    private fun generateRandomSequence(difficulty: Int) {
        if(isGeneratingSequence.value == true) {
            when(difficulty) {
                0 -> {
                    val random = (0..3).random()
                    sequenceList.add(random)
                }

                1 -> {
                    for(i in 0..2) {
                        val random = (0..3).random()
                        sequenceList.add(random)
                    }
                }

                2 -> {
                    for(i in 0..4) {
                        val random = (0..3).random()
                        sequenceList.add(random)
                    }
                }

                else -> { /* Empty */ }
            }
            simonSequence.value = sequenceList
            isGeneratingSequence.value = false
            isFinishUpdate.value = true
        }
    }

    private fun nextSequence() {
        resetIndex()
        stopTime()
        isFinishUpdate.value = false
        isGeneratingSequence.value = true
        generateRandomSequence(difficulty.value!!)
    }

    /* Game */
    fun getTime(): LiveData<Long> {
        return timeLeft
    }

    fun startTime() {
        timer = object : CountDownTimer(10000 - (3000 * difficulty.value?.toLong()!!), 1000) {
            override fun onFinish() {
                isGameOver.value = true
            }
            override fun onTick(millisUntilFinished: Long) {
                timeLeft.value = (millisUntilFinished / 1000) % 60
            }
        }
        timer?.start()
    }

    fun stopTime() {
        timer?.cancel()
    }

    fun resetTime() {
        timer?.cancel()
        timer = object : CountDownTimer(10000 - (3000 * difficulty.value!!.toLong()), 1000) {
            override fun onFinish() {
                isGameOver.value = true
            }
            override fun onTick(millisUntilFinished: Long) {
                timeLeft.value = (millisUntilFinished / 1000)
            }
        }
        timer?.start()
    }

    fun isFinishUpdate(): LiveData<Boolean> {
        return isFinishUpdate
    }

    fun checkSequence(color: Int) {
        if(sequenceList[currentIndex.value!!] == color) {
            incrementScore()
            incrementIndex()
            if(currentIndex.value!! >= sequenceList.size) {
                nextSequence()
            }
        }
        else {
            isGameOver.value = true
        }
    }

    fun lastSequence(): String {
        val colors = arrayOf("RED", "YELLOW", "GREEN", "BLUE")
        return colors[sequenceList[currentIndex.value!!]]
    }

    fun isGameOver(): LiveData<Boolean> {
        return isGameOver
    }

    fun gameFinish() {
        isScoreRegister.value = true
    }

    fun isScoreRegister(): LiveData<Boolean> {
        return isScoreRegister
    }
}
