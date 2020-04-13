package edu.umsl.duc_ngo.shoppinglist.ui.item

import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.umsl.duc_ngo.shoppinglist.R
import edu.umsl.duc_ngo.shoppinglist.data.ShoppingDatabase
import edu.umsl.duc_ngo.shoppinglist.data.ShoppingItem
import edu.umsl.duc_ngo.shoppinglist.ui.BaseFragment
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.item_fragment.*
import kotlinx.android.synthetic.main.item_row_layout.*
import kotlinx.android.synthetic.main.item_row_layout.view.*
import kotlinx.android.synthetic.main.item_row_layout.view._item_table_row
import kotlinx.coroutines.launch

private const val TAG = "Item"
class ItemFragment : BaseFragment() {
    companion object {
        fun newInstance() = ItemFragment()

        private const val LIST_ID = "edu.umsl.duc_ngo.list_id"
        lateinit var intent: Intent

        @JvmStatic
        fun newIntentInit(context: FragmentActivity?, listId: Long): Intent {
            val intent = Intent(context, ItemActivity::class.java)
            intent.putExtra(LIST_ID, listId)
            Companion.intent = intent
            return intent
        }
    }

    /* Global Attributes */
    private lateinit var viewModel: ItemViewModel
    private var listId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.let {
            ViewModelProvider(it).get(ItemViewModel::class.java)
        }!!

        listId = intent.getLongExtra(LIST_ID, -1)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.item_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        /* Configure Recycle View */
        _item_recycler_view.layoutManager = LinearLayoutManager(activity)
        _item_recycler_view.setHasFixedSize(true)
        _item_recycler_view.adapter = ItemAdapter(listOf(ShoppingItem(0,0,"Empty", 0, 0.0)))

        /* Get List Data */
        launch {
            context?.let {
                viewModel.setCurrentList(ShoppingDatabase(it).getShoppingDao().getList(listId))
            }
        }

        /* Get Items Data */
        launch {
            context?.let {
                viewModel.setItems(ShoppingDatabase(it).getShoppingDao().getItem(listId))
            }
        }

        /* Create a new item */
        _create_item_button.setOnClickListener {
            CreateItemDialogFragment.newInstance().show(parentFragmentManager, "CreateItemDialog")
        }

        /* Go back to previous activity */
        _return_button.setOnClickListener {
            activity?.onBackPressed()
        }

        /* Monitor the item header label */
        viewModel.getCurrentList().observe(viewLifecycleOwner, Observer {
            if(it.title.isNotEmpty()) {
                val titleHeader = "${it.title}'s Item"
                _all_item_label.text = titleHeader
            }
        })

        /* Monitor the current item data */
        viewModel.getItems().observe(viewLifecycleOwner, Observer {
            _item_recycler_view.adapter = ItemAdapter(it)
        })

        /* Monitor unmarked item left */
        viewModel.getItemsLeft().observe(viewLifecycleOwner, Observer {
            _total_items_label.text = when(it > 99) {
                true -> "99+"
                false -> it.toString()
            }
        })

        /* Monitor the total price */
        viewModel.getTotalPrice().observe(viewLifecycleOwner, Observer {
            val totalPrice = "$%.2f".format(it)
            _total_price_label.text = totalPrice
        })
    }

    inner class ItemAdapter(private val shoppingItem: List<ShoppingItem>): RecyclerView.Adapter<ItemAdapter.ItemHolder>() {
        /* Called when RecyclerView needs a new ViewHolder of the given type to represent an item. */
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
            return ItemHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_row_layout, parent, false)
            )
        }

        /* Return number of items to render */
        override fun getItemCount() = shoppingItem.size

        /* Called by RecyclerView to display the data at the specified position. */
        override fun onBindViewHolder(holder: ItemHolder, position: Int) {
            /* Item Label */
            holder.view._item_name_label.text = shoppingItem[position].title
            val itemQuantity = "Quantity: ${shoppingItem[position].quantity}"
            holder.view._quantity_label.text = itemQuantity
            val itemPrice = "Price: ${shoppingItem[position].price}"
            holder.view._price_label.text = itemPrice

            /* Item Edit Button */
            holder.view._edit_item_button.setOnClickListener {
                viewModel.setCurrentItem(shoppingItem[position])
                EditItemDialogFragment.newInstance().show(parentFragmentManager, "EditItemDialog")
            }

            /* Item Delete Button */
            holder.view._delete_item_button.setOnClickListener {
                launch {
                    context?.let {
                        ShoppingDatabase(it).getShoppingDao().removeItem(shoppingItem[position].id)
                        Toasty.info(it,"[${shoppingItem[position].title}] Deleted", Toast.LENGTH_SHORT, true).show()
                        viewModel.setItems(ShoppingDatabase(it).getShoppingDao().getItem(listId))
                    }
                }
                Log.d(TAG, "Delete Item ID: ${shoppingItem[position].id}")
            }

            /* Item Pre-apply */
            if(shoppingItem[position].isChecked) {
                holder.view._item_table_row.setBackgroundResource(R.drawable.custom_list_row_marked)
                holder.view._item_checkbox.isChecked = true
            }

            /* Item Checkbox and Body */
            val checkBoxListener = View.OnClickListener {
                launch {
                    context?.let {
                        val isChecked = !shoppingItem[position].isChecked
                        when(isChecked) {
                            true -> {
                                holder.view._item_table_row.setBackgroundResource(R.drawable.custom_list_row_marked)
                                holder.view._item_checkbox.isChecked = true
                            }
                            false ->  {
                                holder.view._item_table_row.setBackgroundResource(R.drawable.custom_list_row)
                                holder.view._item_checkbox.isChecked = false
                            }

                        }
                        ShoppingDatabase(it).getShoppingDao().markItem(shoppingItem[position].id, isChecked)
                        viewModel.setItems(ShoppingDatabase(it).getShoppingDao().getItem(shoppingItem[position].listId))
                    }
                }
            }
            holder.view._item_table_row.setOnClickListener(checkBoxListener)
            holder.view._item_checkbox.setOnClickListener(checkBoxListener)
        }

        /* Describes an item view and metadata about its place within the RecyclerView. */
        inner class ItemHolder(val view: View): RecyclerView.ViewHolder(view)
    }
}