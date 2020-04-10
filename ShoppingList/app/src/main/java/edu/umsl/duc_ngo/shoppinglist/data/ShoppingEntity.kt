package edu.umsl.duc_ngo.shoppinglist.data

import androidx.room.*


@Entity(tableName = "ShoppingItem")
data class ShoppingItem (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo val listId: Long,
    @ColumnInfo var title: String,
    @ColumnInfo var quantity: Int,
    @ColumnInfo var price: Double
)


@Entity(tableName = "ShoppingList")
data class ShoppingList (
    @PrimaryKey(autoGenerate = true) val id: Long  = 0,
    @ColumnInfo var title: String
)


data class ShoppingListWithItems (
    @Embedded val shoppingList: ShoppingList,
    @Relation(
        parentColumn = "id",
        entityColumn = "listId"
    )
    val items: List<ShoppingItem>
)