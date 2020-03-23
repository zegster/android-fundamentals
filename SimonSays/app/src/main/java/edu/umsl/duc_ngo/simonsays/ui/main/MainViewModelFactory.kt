package edu.umsl.duc_ngo.simonsays.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import edu.umsl.duc_ngo.simonsays.data.PlayerDataRepository

class MainViewModelFactory(private val playerDataRepository: PlayerDataRepository) : ViewModelProvider.NewInstanceFactory() {
    //Dependency injection is a way to modularize our code
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(playerDataRepository) as T
    }
}