package edu.umsl.duc_ngo.shoppinglist.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.umsl.duc_ngo.shoppinglist.R
import edu.umsl.duc_ngo.shoppinglist.data.ShoppingDatabase
import edu.umsl.duc_ngo.shoppinglist.data.ShoppingList
import edu.umsl.duc_ngo.shoppinglist.ui.BaseFragment
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.shopping_row_layout.view.*
import kotlinx.coroutines.launch

private const val TAG = "MainFragment"
class MainFragment : BaseFragment() {
    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        _shoppinglist_recycleview.layoutManager = LinearLayoutManager(activity)
        _shoppinglist_recycleview.setHasFixedSize(true) //Improve the performance by not changing it size.

        /* Create new list */
        _create_list_button.setOnClickListener {
            context?.let {
                Toasty.info(it,"New List Created", Toast.LENGTH_SHORT, true).show();
            }

            launch {
                context?.let {
                    val test = ShoppingList(title = "Test")
                    ShoppingDatabase(it).getShoppingDao().addList(test)
                }
            }

            //Refresh shopping list data
            getShoppingList()
        }

        //Get shopping list data
        getShoppingList()
    }

    private fun getShoppingList() {
        launch {
            context?.let {
                val allShoppingListData = ShoppingDatabase(it).getShoppingDao().getLists()
                _shoppinglist_recycleview.adapter = ShoppingListAdapter(allShoppingListData)
            }
        }
    }

    inner class ShoppingListAdapter(private val shoppingList: List<ShoppingList>): RecyclerView.Adapter<ShoppingListAdapter.ShoppingListHolder>() {
        /* Called when RecyclerView needs a new ViewHolder of the given type to represent an item. */
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListHolder {
            return ShoppingListHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.shopping_row_layout, parent, false)
            )
        }

        /* Return number of items to render */
        override fun getItemCount() = shoppingList.size

        /* Called by RecyclerView to display the data at the specified position. */
        override fun onBindViewHolder(holder: ShoppingListHolder, position: Int) {
            holder.view._list_name_label.text = shoppingList[position].title

            holder.view._edit_list_button.setOnClickListener {
                launch {
                    context?.let {
                        ShoppingDatabase(it).getShoppingDao().updateList(shoppingList[position].id, "Edit")
                    }
                    getShoppingList()
                }
            }

            holder.view._delete_list_button.setOnClickListener {
                launch {
                    context?.let {
                        ShoppingDatabase(it).getShoppingDao().removeList(shoppingList[position].id)
                    }
                    getShoppingList()
                }
            }
        }

        /* Describes an item view and metadata about its place within the RecyclerView. */
        inner class ShoppingListHolder(val view: View): RecyclerView.ViewHolder(view)
    }
}