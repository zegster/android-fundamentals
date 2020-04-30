package edu.umsl.duc_ngo.multipurpose.data.note

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [NoteList::class, NoteItem::class],
    version = 1,
    exportSchema = false
)
abstract class NoteDatabase : RoomDatabase() {
    companion object {
        //Volatile: this instance is immediately available for all thread
        @Volatile
        private var instance: NoteDatabase? = null
        private val LOCK = Any()

        //Return an instance if not null. Else build our database
        //Once database is build, assign it to our instance
        //Invoke function will automatically call when Database() is call.
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        //This will build our database.
        //When we pass the context from a fragment, it will take the application context only.
        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext, NoteDatabase::class.java, "NoteDatabase"
        ).build()
    }

    abstract fun getNoteDao(): NoteDao
}