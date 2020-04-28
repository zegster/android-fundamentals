package edu.umsl.duc_ngo.multipurpose.ui.note

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.umsl.duc_ngo.multipurpose.data.note.NoteItem
import edu.umsl.duc_ngo.multipurpose.data.note.NoteList

class NoteViewModel : ViewModel() {
    private var mCurrentList = MutableLiveData<NoteList>()
    private var mLists = mutableListOf<NoteList>()
    private var mListsLiveData = MutableLiveData<List<NoteList>>()
    private var mItems = mutableListOf<NoteItem>()
    private var mItemsLiveData = MutableLiveData<List<NoteItem>>()

    init {
        mListsLiveData.value = mLists
        mItemsLiveData.value = mItems
    }

    /* Note List */
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
        for(i in mItems) {
            if(i.listId == listId) {
                total++
            }
        }
        return total
    }
}