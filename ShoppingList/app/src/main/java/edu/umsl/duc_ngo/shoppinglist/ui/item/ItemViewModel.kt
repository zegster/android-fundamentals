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
    private var mItemsLeft = MutableLiveData<Long>()
    private var mTotalPrice = MutableLiveData<Double>()

    init {
        mItemsLiveData.value = mItems
        mItemsLeft.value = 0L
        mTotalPrice.value = 0.0
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
        calculateItemLeft()
        calculatePrice()
    }

    fun getItems(): LiveData<List<ShoppingItem>> {
        return mItemsLiveData
    }

    fun getItemsLeft(): LiveData<Long> {
        return mItemsLeft
    }

    fun getTotalPrice(): LiveData<Double> {
        return mTotalPrice
    }

    private fun calculateItemLeft() {
        var itemLeft = 0L
        for(i in mItems) {
            if(!i.isChecked) {
                itemLeft++
            }
        }
        mItemsLeft.value = itemLeft
    }

    private fun calculatePrice() {
        var total = 0.0
        for(i in mItems) {
            total += i.price * i.quantity.toDouble()
        }
        mTotalPrice.value = total
    }
}