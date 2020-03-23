package edu.umsl.duc_ngo.simonsays.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class PlayerDataDao {
    private val playerData = MutableLiveData<PlayerData>()

    init {
        playerData.postValue(PlayerData(0))
    }

    fun updatePlayer(newData: PlayerData) {
        playerData.value = newData
    }

    fun getPlayer() = playerData as LiveData<PlayerData>
}