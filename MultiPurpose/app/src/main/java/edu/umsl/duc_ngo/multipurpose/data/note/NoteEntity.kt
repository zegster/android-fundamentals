package edu.umsl.duc_ngo.multipurpose.data.note

import androidx.room.*

@Entity(tableName = "NoteList")
data class NoteList(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo var title: String,
    @ColumnInfo var timestamp: String,
    @ColumnInfo var colorLabel: String
)

@Entity(
    tableName = "NoteItem",
    foreignKeys = [ForeignKey(
        entity = NoteList::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("listId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class NoteItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(index = true) val listId: Long,
    @ColumnInfo var title: String,
    @ColumnInfo var timestamp: String,
    @ColumnInfo var content: String
)
