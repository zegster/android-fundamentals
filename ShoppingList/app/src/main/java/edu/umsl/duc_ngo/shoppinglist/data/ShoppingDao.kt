package edu.umsl.duc_ngo.shoppinglist.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface ShoppingDao {
    @Insert
    suspend fun addItem(shoppingItem: ShoppingItem)

    @Transaction
    @Query("SELECT * FROM ShoppingItem WHERE listId = :listId")
    suspend fun getItems(listId: Long): List<ShoppingItem>

    @Insert
    suspend fun addList(shoppingList: ShoppingList)

    @Query("SELECT * FROM ShoppingItem WHERE listId = :listId")
    suspend fun getList(listId: Long): List<ShoppingItem>

    @Query("SELECT * FROM ShoppingList")
    suspend fun getLists(): List<ShoppingList>

    @Query("UPDATE ShoppingList SET title = :listName WHERE id = :listId")
    suspend fun updateList(listId: Long, listName: String)

    @Query("DELETE FROM ShoppingList WHERE id = :listId")
    suspend fun removeList(listId: Long)
}