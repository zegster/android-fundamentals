package edu.umsl.duc_ngo.simonsays.data

//class PlayerDataRepository private constructor(private val playerDataDao: PlayerDataDao){
//    companion object {
//        @Volatile private var instance: PlayerDataRepository? = null
//
//        fun getInstance(playerDataDao: PlayerDataDao) = instance ?: synchronized(this) {
//            instance ?: PlayerDataRepository(playerDataDao).also { instance = it }
//        }
//    }
//
//    fun updatePlayer(playerData: PlayerData) {
//        playerDataDao.updatePlayer(playerData)
//    }
//
//    fun getPlayer() = playerDataDao.getPlayer()
//}