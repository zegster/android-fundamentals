package edu.umsl.duc_ngo.shoppinglist.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ShoppingDao {
    /* Item */
    @Query("SELECT * FROM ShoppingItem WHERE listId = :listId")
    suspend fun getItems(listId: Long): List<ShoppingItem>

    @Insert
    suspend fun addItem(shoppingItem: ShoppingItem)

    @Query("UPDATE ShoppingItem SET title = :itemName, quantity = :itemQuantity, price = :itemPrice WHERE id = :itemId")
    suspend fun updateItem(itemId: Long, itemName: String, itemQuantity: Int, itemPrice: Double)

    @Query("DELETE FROM ShoppingItem WHERE id = :itemId")
    suspend fun removeItem(itemId: Long)


    /* List */
    @Query("SELECT * FROM ShoppingList WHERE id = :listId")
    suspend fun getList(listId: Long): ShoppingList

    @Query("SELECT * FROM ShoppingList")
    suspend fun getLists(): List<ShoppingList>

    @Insert
    suspend fun addList(shoppingList: ShoppingList)

    @Query("UPDATE ShoppingList SET title = :listName WHERE id = :listId")
    suspend fun updateList(listId: Long, listName: String)


    @Query("DELETE FROM ShoppingList WHERE id = :listId")
    suspend fun removeList(listId: Long)
}