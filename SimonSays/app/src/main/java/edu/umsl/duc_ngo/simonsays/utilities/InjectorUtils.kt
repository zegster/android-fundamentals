package edu.umsl.duc_ngo.simonsays.utilities

import edu.umsl.duc_ngo.simonsays.data.Database
import edu.umsl.duc_ngo.simonsays.data.PlayerDataRepository
import edu.umsl.duc_ngo.simonsays.ui.main.MainViewModelFactory

object InjectorUtils {
    fun provideMainViewModelFactory(): MainViewModelFactory {
        val playerDataRepository = PlayerDataRepository.getInstance(Database.getInstance().playerDataDao)
        return MainViewModelFactory(playerDataRepository)
    }
}