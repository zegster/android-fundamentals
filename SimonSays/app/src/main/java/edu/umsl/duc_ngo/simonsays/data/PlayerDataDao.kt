package edu.umsl.duc_ngo.simonsays.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

//class PlayerDataDao {
//    private val playerData = MutableLiveData<PlayerData>()
//
//    init {
//        playerData.postValue(PlayerData(0))
//    }
//
//    fun updatePlayer(newData: PlayerData) {
//        playerData.value = newData
//    }
//
//    fun getPlayer() = playerData as LiveData<PlayerData>
//}

@Dao
interface PlayerDataDao {
    @Insert
    suspend fun addPlayer(playerData: PlayerData)

    @Query("SELECT * FROM PlayerData ORDER BY score")
    suspend fun getAllPlayers() : List<PlayerData>
}
