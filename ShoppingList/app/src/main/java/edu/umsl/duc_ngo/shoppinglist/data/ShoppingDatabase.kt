package edu.umsl.duc_ngo.shoppinglist.data

import android.content.Context
import androidx.room.*
import androidx.room.Database

@Database(
    entities = [ShoppingItem::class, ShoppingList::class],
    version = 1,
    exportSchema = false
)
abstract class ShoppingDatabase: RoomDatabase()  {
    companion object {
        //Volatile: this instance is immediately available for all thread
        @Volatile private var instance : ShoppingDatabase? = null
        private val LOCK = Any()

        //Return an instance if not null. Else build our database
        //Once database is build, assign it to our instance
        //Invoke function will automatically call when SimonDatabase() is call.
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        //This will build our database.
        //When we pass the context from a fragment, it will take the application context only.
        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext, ShoppingDatabase::class.java, "ShoppingDatabase"
        ).build()
    }

    abstract fun getShoppingDao() : ShoppingDao
}