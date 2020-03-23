package edu.umsl.duc_ngo.simonsays.ui.main

import androidx.lifecycle.ViewModel
import edu.umsl.duc_ngo.simonsays.data.PlayerData
import edu.umsl.duc_ngo.simonsays.data.PlayerDataRepository

class MainViewModel(private val playerDataRepository: PlayerDataRepository) : ViewModel() {
    fun updatePlayer(playerData: PlayerData) = playerDataRepository.updatePlayer(playerData)
    fun getPlayer() = playerDataRepository.getPlayer()
}
