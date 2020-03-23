package edu.umsl.duc_ngo.simonsays.data

class Database private constructor() {
    companion object {
        @Volatile private var instance: Database? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: Database().also { instance = it }
        }
    }

    //Init DAO
    var playerDataDao = PlayerDataDao()
        private set
}