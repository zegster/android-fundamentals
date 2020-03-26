package edu.umsl.duc_ngo.simonsays.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PlayerData (
    val score: Int = 0,
    val difficulty: Int = 0,
    val datePlay: String = ""
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
