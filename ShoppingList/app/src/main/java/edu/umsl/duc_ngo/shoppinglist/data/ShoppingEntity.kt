package edu.umsl.duc_ngo.shoppinglist.data

import androidx.room.*

@Entity(tableName = "ShoppingList")
data class ShoppingList (
    @PrimaryKey(autoGenerate = true) val id: Long  = 0,
    @ColumnInfo var title: String
)

@Entity(
    tableName = "ShoppingItem",
    foreignKeys = [ForeignKey(entity = ShoppingList::class, parentColumns = arrayOf("id"), childColumns = arrayOf("listId"), onDelete = ForeignKey.CASCADE )]
)
data class ShoppingItem (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo val listId: Long,
    @ColumnInfo var title: String,
    @ColumnInfo var quantity: Int,
    @ColumnInfo var price: Double
)
