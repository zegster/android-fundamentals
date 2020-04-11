package edu.umsl.duc_ngo.shoppinglist.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.umsl.duc_ngo.shoppinglist.R
import edu.umsl.duc_ngo.shoppinglist.data.ShoppingDatabase
import edu.umsl.duc_ngo.shoppinglist.data.ShoppingList
import edu.umsl.duc_ngo.shoppinglist.ui.BaseFragment
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.list_fragment.*
import kotlinx.android.synthetic.main.list_row_layout.view.*
import kotlinx.coroutines.launch

private const val TAG = "Main"
class ListFragment : BaseFragment() {
    companion object {
        fun newInstance() = ListFragment()
    }

    private lateinit var viewModel: ListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.let {
            ViewModelProvider(it).get(ListViewModel::class.java)
        }!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        /* Configure Recycle View */
        _shoppinglist_recycleview.layoutManager = LinearLayoutManager(activity)
        _shoppinglist_recycleview.setHasFixedSize(true)
        _shoppinglist_recycleview.adapter = ShoppingListAdapter(listOf(ShoppingList(0, "Empty")))

        /* Get List Data */
        launch {
            context?.let {
                viewModel.setLists(ShoppingDatabase(it).getShoppingDao().getLists())
            }
        }

        /* Create a new list */
        _create_list_button.setOnClickListener {
            CreateListDialogFragment.newInstance().show(parentFragmentManager, "CreateListDialog")
        }

        /* Monitor the current list data */
        viewModel.getLists().observe(viewLifecycleOwner, Observer {
            _shoppinglist_recycleview.adapter = ShoppingListAdapter(it)
        })
    }

    inner class ShoppingListAdapter(private val shoppingList: List<ShoppingList>): RecyclerView.Adapter<ShoppingListAdapter.ShoppingListHolder>() {
        /* Called when RecyclerView needs a new ViewHolder of the given type to represent an item. */
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListHolder {
            return ShoppingListHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.list_row_layout, parent, false)
            )
        }

        /* Return number of items to render */
        override fun getItemCount() = shoppingList.size

        /* Called by RecyclerView to display the data at the specified position. */
        override fun onBindViewHolder(holder: ShoppingListHolder, position: Int) {
            holder.view._list_name_label.text = shoppingList[position].title

            holder.view._edit_list_button.setOnClickListener {
                viewModel.setCurrentList(shoppingList[position])
                EditListDialogFragment.newInstance().show(parentFragmentManager, "EditListDialog")
            }

            holder.view._delete_list_button.setOnClickListener {
                launch {
                    context?.let {
                        ShoppingDatabase(it).getShoppingDao().removeList(shoppingList[position].id)
                        Toasty.info(it,"Entry #${shoppingList[position].id} Deleted", Toast.LENGTH_SHORT, true).show()
                        viewModel.setLists(ShoppingDatabase(it).getShoppingDao().getLists())
                    }
                }
            }

            holder.view._list_tablerow.setOnClickListener {
                context?.let {
                    Toasty.info(it,"Testing " + shoppingList[position].id, Toast.LENGTH_SHORT, true).show()
                }
            }
        }

        /* Describes an item view and metadata about its place within the RecyclerView. */
        inner class ShoppingListHolder(val view: View): RecyclerView.ViewHolder(view)
    }
}