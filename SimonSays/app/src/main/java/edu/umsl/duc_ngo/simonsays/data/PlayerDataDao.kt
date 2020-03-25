package edu.umsl.duc_ngo.simonsays.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PlayerDataDao {
    @Insert
    suspend fun addPlayer(playerData: PlayerData)

    @Query("SELECT * FROM PlayerData ORDER BY score DESC")
    suspend fun getAllPlayers() : List<PlayerData>
}
