package edu.umsl.duc_ngo.multipurpose.data.note

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NoteDao {
    /* ~~~ List ~~~ */
    @Query("SELECT * FROM NoteList WHERE id = :id")
    suspend fun getList(id: Long): NoteList

    @Query("SELECT * FROM NoteList")
    suspend fun getLists(): List<NoteList>

    @Insert
    suspend fun addList(NoteList: NoteList)

    @Query("UPDATE NoteList SET title = :title, timestamp = :timestamp, colorLabel = :colorLabel WHERE id = :id")
    suspend fun updateList(id: Long, title: String, timestamp: String, colorLabel: String)

    @Query("DELETE FROM NoteList WHERE id = :id")
    suspend fun removeList(id: Long)

    /* ~~~ Item ~~~ */
    @Query("SELECT * FROM NoteItem WHERE listId = :listId")
    suspend fun getItem(listId: Long): List<NoteItem>

    @Query("SELECT * FROM NoteItem")
    suspend fun getItems(): List<NoteItem>

    @Insert
    suspend fun addItem(NoteItem: NoteItem)

    @Query("UPDATE NoteItem SET title = :title, timestamp = :timestamp, content = :content WHERE id = :id")
    suspend fun updateItem(id: Long, title: String, timestamp: String, content: String)

    @Query("DELETE FROM NoteItem WHERE id = :id")
    suspend fun removeItem(id: Long)
}
