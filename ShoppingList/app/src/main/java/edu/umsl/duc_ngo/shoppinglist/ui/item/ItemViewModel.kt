package edu.umsl.duc_ngo.shoppinglist.ui.item

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.umsl.duc_ngo.shoppinglist.data.ShoppingItem
import edu.umsl.duc_ngo.shoppinglist.data.ShoppingList

class ItemViewModel : ViewModel() {
    private var mCurrentList = MutableLiveData<ShoppingList>()
    private var mCurrentItem = MutableLiveData<ShoppingItem>()
    private var mItems = mutableListOf<ShoppingItem>()
    private var mItemsLiveData = MutableLiveData<List<ShoppingItem>>()

    init {
        mItemsLiveData.value = mItems
    }

    /* Shopping List */
    fun setCurrentList(list: ShoppingList) {
        mCurrentList.value = list
    }

    fun getCurrentList(): LiveData<ShoppingList> {
        return mCurrentList
    }

    /* Shopping Item */
    fun setCurrentItem(item: ShoppingItem) {
        mCurrentItem.value = item
    }

    fun getCurrentItem(): ShoppingItem {
        return mCurrentItem.value!!
    }

    fun setItems(items: List<ShoppingItem>) {
        mItems = items.toMutableList()
        mItemsLiveData.value = mItems
    }

    fun getItems(): LiveData<List<ShoppingItem>> {
        return mItemsLiveData
    }
}