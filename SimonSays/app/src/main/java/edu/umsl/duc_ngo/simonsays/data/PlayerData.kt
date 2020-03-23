package edu.umsl.duc_ngo.simonsays.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// data class PlayerData (val score: Int = 0)

@Entity
data class PlayerData (
    val score: Int = 0
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
