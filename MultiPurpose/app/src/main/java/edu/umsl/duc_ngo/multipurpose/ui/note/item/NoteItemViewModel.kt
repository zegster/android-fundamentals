package edu.umsl.duc_ngo.multipurpose.ui.note.item

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.umsl.duc_ngo.multipurpose.data.note.NoteItem
import edu.umsl.duc_ngo.multipurpose.data.note.NoteList

class NoteItemViewModel : ViewModel() {
    private var mCurrentList = MutableLiveData<NoteList>()
    private var mCurrentItem = MutableLiveData<NoteItem>()
    private var mItems = mutableListOf<NoteItem>()
    private var mItemsLiveData = MutableLiveData<List<NoteItem>>()
    private var mItemsLeft = MutableLiveData<Long>()
    private var mTotalPrice = MutableLiveData<Double>()

    init {
        mItemsLiveData.value = mItems
        mItemsLeft.value = 0L
        mTotalPrice.value = 0.0
    }

    /* Shopping List */
    fun setCurrentList(list: NoteList) {
        mCurrentList.value = list
    }

    fun getCurrentList(): LiveData<NoteList> {
        return mCurrentList
    }

    /* Shopping Item */
    fun setCurrentItem(item: NoteItem) {
        mCurrentItem.value = item
    }

    fun getCurrentItem(): NoteItem {
        return mCurrentItem.value!!
    }

    fun setItems(items: List<NoteItem>) {
        mItems = items.toMutableList()
        mItemsLiveData.value = mItems
    }

    fun getItems(): LiveData<List<NoteItem>> {
        return mItemsLiveData
    }
}