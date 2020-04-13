package edu.umsl.duc_ngo.shoppinglist.ui.list

import android.os.Bundle
import android.util.Log
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
import edu.umsl.duc_ngo.shoppinglist.ui.item.ItemFragment
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.list_fragment.*
import kotlinx.android.synthetic.main.list_row_layout.view.*
import kotlinx.coroutines.launch

private const val TAG = "List"
class ListFragment : BaseFragment() {
    companion object {
        fun newInstance() = ListFragment()
    }

    /* Global Attributes */
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
        _list_recycler_view.layoutManager = LinearLayoutManager(activity)
        _list_recycler_view.setHasFixedSize(true)
        _list_recycler_view.adapter = ListAdapter(listOf(ShoppingList(0, "Empty")))
        getPersistenceData()

        /* Create a new list */
        _create_list_button.setOnClickListener {
            CreateListDialogFragment.newInstance().show(parentFragmentManager, "CreateListDialog")
        }

        /* Monitor the current list data */
        viewModel.getLists().observe(viewLifecycleOwner, Observer {
            _list_recycler_view.adapter = ListAdapter(it)
        })

        /* Monitor the current list data */
        viewModel.getItems().observe(viewLifecycleOwner, Observer {
            _list_recycler_view.adapter = ListAdapter(viewModel.getLists().value!!)
        })
    }

    override fun onResume() {
        super.onResume()
        getPersistenceData()
    }

    private fun getPersistenceData() {
        /* Get Lists Data */
        launch {
            context?.let {
                viewModel.setLists(ShoppingDatabase(it).getShoppingDao().getLists())
            }
        }

        /* Get Items Data */
        launch {
            context?.let {
                viewModel.setItems(ShoppingDatabase(it).getShoppingDao().getItems())
            }
        }
    }

    inner class ListAdapter(private val shoppingList: List<ShoppingList>): RecyclerView.Adapter<ListAdapter.ListHolder>() {
        /* Called when RecyclerView needs a new ViewHolder of the given type to represent an item. */
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListHolder {
            return ListHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.list_row_layout, parent, false)
            )
        }

        /* Return number of items to render */
        override fun getItemCount() = shoppingList.size

        /* Called by RecyclerView to display the data at the specified position. */
        override fun onBindViewHolder(holder: ListHolder, position: Int) {
            /* List Label */
            holder.view._list_name_label.text = shoppingList[position].title
            holder.view._list_total_item_label.text = viewModel.getTotalItem(shoppingList[position].id).toString()

            /* List Edit Button */
            holder.view._edit_list_button.setOnClickListener {
                viewModel.setCurrentList(shoppingList[position])
                EditListDialogFragment.newInstance().show(parentFragmentManager, "EditListDialog")
            }

            /* List Delete Button */
            holder.view._delete_list_button.setOnClickListener {
                launch {
                    context?.let {
                        ShoppingDatabase(it).getShoppingDao().removeList(shoppingList[position].id)
                        Toasty.info(it,"[${shoppingList[position].title}] Deleted", Toast.LENGTH_SHORT, true).show()
                        viewModel.setLists(ShoppingDatabase(it).getShoppingDao().getLists())
                    }
                }
                Log.d(TAG, "Delete List ID: ${shoppingList[position].id}")
            }

            /* List Body */
            holder.view._list_table_row.setOnClickListener {
                val intent = ItemFragment.newIntentInit(activity, shoppingList[position].id)
                startActivity(intent)
            }
        }

        /* Describes an item view and metadata about its place within the RecyclerView. */
        inner class ListHolder(val view: View): RecyclerView.ViewHolder(view)
    }
}