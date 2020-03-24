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
    private var isUpdating = MutableLiveData<Boolean>()
    private var isGameOver = MutableLiveData<Boolean>()

    private lateinit var timer: CountDownTimer
    private var timeLeft = MutableLiveData<Long>()

    init {
        currentScore.value = 0
        difficulty.value = 0
        isGeneratingSequence.value = true
        currentIndex.value = 0
        simonSequence.value = sequenceList
        isGameStarted.value = false
        isUpdating.value = true
        isGameOver.value = false
    }

    /* Score */
    private fun incrementScore() {
        currentScore.value = currentScore.value?.plus(1)
    }

    fun getScore(): LiveData<Int> {
        return currentScore
    }

    /* Sequence */
    private fun resetIndex() {
        currentIndex.value = 0
    }

    private fun incrementIndex() {
        currentIndex.value = currentIndex.value?.plus(1)
    }

    fun getSequence(): LiveData<List<Int>> {
        return simonSequence
    }

    private fun generateRandomSequence(difficulty: Int) {
        if(isGeneratingSequence.value == true) {
            when(difficulty) {
                0 -> {
                    val random = (0..3).random()
                    sequenceList.add(random)
                    simonSequence.value = sequenceList
                    isGeneratingSequence.value = false
                }

                1 -> {
                    for(i in 0..2) {
                        val random = (0..3).random()
                        sequenceList.add(random)
                        simonSequence.value = sequenceList
                    }
                    isGeneratingSequence.value = false
                }

                2 -> {
                    for(i in 0..4) {
                        val random = (0..3).random()
                        sequenceList.add(random)
                        simonSequence.value = sequenceList
                    }
                    isGeneratingSequence.value = false
                }

                else -> {
                    val random = (0..3).random()
                    sequenceList.add(random)
                    simonSequence.value = sequenceList
                    isGeneratingSequence.value = false
                }
            }
        }
    }

    fun initSequence(difficulty: Int) {
        if(isGameStarted.value == false) {
            this.difficulty.value = difficulty
            generateRandomSequence(this.difficulty.value!!)
            isGameStarted.value = true
            timer = object : CountDownTimer(10000 - (3000 * difficulty.toLong()), 1000) {
                override fun onFinish() {
                    isGameOver.value = true
                }
                override fun onTick(millisUntilFinished: Long) {
                    timeLeft.value = (millisUntilFinished / 1000) % 60
                }
            }
            timeLeft.value = ((10000 - (3000 * difficulty.toLong())) / 1000) % 60
        }
    }

    private fun nextSequence() {
        isUpdating.value = true
        resetIndex()
        isGeneratingSequence.value = true
        generateRandomSequence(difficulty.value!!)
    }

    /* Game */
    fun getTime(): MutableLiveData<Long> {
        return timeLeft
    }

    fun startTime() {
        timer.start()
    }

    fun resetTime() {
        timer.cancel()
        timer = object : CountDownTimer(10000 - (3000 * difficulty.value!!.toLong()), 1000) {
            override fun onFinish() {
                isGameOver.value = true
            }
            override fun onTick(millisUntilFinished: Long) {
                timeLeft.value = (millisUntilFinished / 1000)
            }
        }
        timer.start()
    }

    fun isUpdating(): Boolean? {
        val state = isUpdating.value
        isUpdating.value = false
        return state
    }

    fun isGameOver(): LiveData<Boolean> {
        return isGameOver
    }

    fun checkSequence(color: Int): Boolean? {
        if(sequenceList[currentIndex.value!!] == color) {
            incrementScore()
            incrementIndex()
            if(currentIndex.value!! >= sequenceList.size) {
                timer.cancel()
                nextSequence()
            }
        }
        else {
            isGameOver.value = true
        }

        return isGameOver.value
    }

    fun lastSequence(): String {
        val colors = arrayOf("RED", "YELLOW", "GREEN", "BLUE")
        return colors[sequenceList[currentIndex.value!!]]
    }
}
