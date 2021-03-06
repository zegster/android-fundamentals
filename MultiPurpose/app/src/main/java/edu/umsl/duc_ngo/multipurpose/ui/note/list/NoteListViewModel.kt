package edu.umsl.duc_ngo.multipurpose.ui.note.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.umsl.duc_ngo.multipurpose.data.note.NoteItem
import edu.umsl.duc_ngo.multipurpose.data.note.NoteList

class NoteListViewModel : ViewModel() {
    private var mCurrentCategory = MutableLiveData<String>()
    private var mCurrentList = MutableLiveData<NoteList>()
    private var mLists = mutableListOf<NoteList>()
    private var mListsLiveData = MutableLiveData<List<NoteList>>()
    private var mItems = mutableListOf<NoteItem>()
    private var mItemsLiveData = MutableLiveData<List<NoteItem>>()

    init {
        mCurrentCategory.value = "id"
        mListsLiveData.value = mLists
        mItemsLiveData.value = mItems
    }

    /* Utility Function */
    fun getColorLabel(colors: String, isPosition: Boolean = false): String {
        if (isPosition) {
            return when (colors) {
                "Orange" -> "1"
                "Red" -> "2"
                "Violet" -> "3"
                "Blue" -> "4"
                "Green" -> "5"
                "Yellow" -> "6"
                else -> "0"
            }
        } else {
            return when (colors) {
                "Orange" -> "#FFC8B2"
                "Red" -> "#FFC8C8"
                "Violet" -> "#D3C8FF"
                "Blue" -> "#C8E7FF"
                "Green" -> "#C6FFC8"
                "Yellow" -> "#FFFAC8"
                else -> "#FFFFFF"
            }
        }
    }

    /* Note List */
    fun setCurrentCategory(category: String) {
        mCurrentCategory.value = when (category) {
            "Default" -> "id"
            "Name - ASC" -> "title ASC"
            "Name - DESC" -> "title DESC"
            "Last Updated - ASC" -> "timestamp ASC"
            "Last Updated - DESC" -> "timestamp DESC"
            "Color - ASC" -> "colorLabel ASC"
            "Color - DESC" -> "colorLabel DESC"
            else -> "id"
        }
    }

    fun getCurrentCategory(): String {
        return mCurrentCategory.value!!
    }

    fun setCurrentList(list: NoteList) {
        mCurrentList.value = list
    }

    fun getCurrentList(): NoteList {
        return mCurrentList.value!!
    }

    fun setLists(list: List<NoteList>) {
        mLists = list.toMutableList()
        mListsLiveData.value = mLists
    }

    fun getLists(): LiveData<List<NoteList>> {
        return mListsLiveData
    }

    /* Note Item */
    fun setItems(items: List<NoteItem>) {
        mItems = items.toMutableList()
    }

    fun getItems(): LiveData<List<NoteItem>> {
        return mItemsLiveData
    }

    fun getTotalItem(listId: Long): Long {
        var total = 0L
        for (i in mItems) {
            if (i.listId == listId) {
                total++
            }
        }
        return total
    }
}