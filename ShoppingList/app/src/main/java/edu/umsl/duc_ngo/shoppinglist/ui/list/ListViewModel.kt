package edu.umsl.duc_ngo.shoppinglist.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.umsl.duc_ngo.shoppinglist.data.ShoppingItem
import edu.umsl.duc_ngo.shoppinglist.data.ShoppingList

class ListViewModel : ViewModel() {
    private var mCurrentList = MutableLiveData<ShoppingList>()
    private var mLists = mutableListOf<ShoppingList>()
    private var mListsLiveData = MutableLiveData<List<ShoppingList>>()
    private var mItems = mutableListOf<ShoppingItem>()
    private var mItemsLiveData = MutableLiveData<List<ShoppingItem>>()

    init {
        mListsLiveData.value = mLists
        mItemsLiveData.value = mItems
    }

    /* Shopping List */
    fun setCurrentList(list: ShoppingList) {
        mCurrentList.value = list
    }

    fun getCurrentList(): ShoppingList {
        return mCurrentList.value!!
    }

    fun setLists(list: List<ShoppingList>) {
        mLists = list.toMutableList()
        mListsLiveData.value = mLists
    }

    fun getLists(): LiveData<List<ShoppingList>> {
        return mListsLiveData
    }

    /* Shopping Item */
    fun setItems(items: List<ShoppingItem>) {
        mItems = items.toMutableList()
    }

    fun getItems(): LiveData<List<ShoppingItem>> {
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