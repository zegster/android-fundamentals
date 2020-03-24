package edu.umsl.duc_ngo.simonsays.ui.game

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {
    var currentScore = MutableLiveData<Int>()

    private var difficulty = MutableLiveData<Int>()
    private var isGeneratingSequence = MutableLiveData<Boolean>()
    private var currentIndex = MutableLiveData<Int>()
    private var sequenceList = mutableListOf<Int>()
    var simonSequence = MutableLiveData<List<Int>>()

    private var isGameOver = MutableLiveData<Boolean>()

    init {
        currentScore.value = 0
        difficulty.value = 0
        isGeneratingSequence.value = true
        currentIndex.value = 0
        simonSequence.value = sequenceList
        isGameOver.value = false
    }

    private fun incrementScore() {
        currentScore.value = currentScore.value?.plus(1)
    }

    private fun resetIndex() {
        currentIndex.value = 0
    }

    private fun incrementIndex() {
        currentIndex.value = currentIndex.value?.plus(1)
    }

    fun generateRandomSequence(difficulty: Int) {
        this.difficulty.value = difficulty
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

    fun gamePlay(color: Int): Boolean? {
        if(sequenceList[currentIndex.value!!] == color) {
            incrementScore()
            incrementIndex()
            if(currentIndex.value!! >= sequenceList.size) {
                resetIndex()
                isGeneratingSequence.value = true
                generateRandomSequence(difficulty.value!!)
            }
        }
        else {
            Log.e("Game", "sequence: $sequenceList")
            isGameOver.value = true
        }

        return isGameOver.value
    }
}
