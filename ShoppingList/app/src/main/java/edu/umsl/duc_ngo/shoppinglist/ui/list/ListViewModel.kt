package edu.umsl.duc_ngo.shoppinglist.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.umsl.duc_ngo.shoppinglist.data.ShoppingList

class ListViewModel : ViewModel() {
    private var mCurrentList = MutableLiveData<ShoppingList>()
    private var mLists = mutableListOf<ShoppingList>()
    private var mListsLiveData = MutableLiveData<List<ShoppingList>>()

    init {
        mListsLiveData.value = mLists
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
}