package edu.umsl.duc_ngo.shoppinglist.ui.item

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import edu.umsl.duc_ngo.shoppinglist.R
import edu.umsl.duc_ngo.shoppinglist.data.ShoppingDatabase
import edu.umsl.duc_ngo.shoppinglist.data.ShoppingItem
import edu.umsl.duc_ngo.shoppinglist.ui.BaseDialogFragment
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.item_dialog_fragment.view.*
import kotlinx.coroutines.launch

private const val TAG = "CreateItem"
class CreateItemDialogFragment : BaseDialogFragment() {
    companion object {
        fun newInstance() = CreateItemDialogFragment()
    }

    private lateinit var viewModel: ItemViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.let {
            ViewModelProvider(it).get(ItemViewModel::class.java)
        }!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.item_dialog_fragment, container, false)
    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        return activity?.let {
            /* Dialog Configuration */
            /* Inflate and set the layout for the dialog
            NOTE: Pass null as the parent view because its going in the dialog layout. IDE will complain, but it's fine. */
            val mDialogView = requireActivity().layoutInflater.inflate(R.layout.item_dialog_fragment, null)
            val mBuilder = AlertDialog.Builder(it).setView(mDialogView)

            /* Dialog Title */
            mDialogView._item_title_label.text = getString(R.string.item_dialog_edit_title)

            /* Dialog Cancellation */
            mDialogView._item_cancel_button.setOnClickListener {
                dismiss()
            }

            /* Dialog Submission */
            mDialogView._item_submit_button.setOnClickListener {
                launch {
                    context?.let { lContext ->
                        val newItemListId = viewModel.getCurrentList().value!!.id

                        val newItemName = when(mDialogView._item_name_edit_text.text.toString().isBlank()) {
                            true -> "Untitled Item"
                            false -> mDialogView._item_name_edit_text.text.toString()
                        }

                        val newItemQuantity = when(mDialogView._item_quantity_edit_text.text.toString().isBlank()) {
                            true -> 0
                            false -> mDialogView._item_quantity_edit_text.text.toString().toInt()
                        }

                        val newItemPrice = when(mDialogView._item_price_edit_text.text.toString().isBlank()) {
                            true -> 0.0
                            false -> mDialogView._item_price_edit_text.text.toString().toDouble()
                        }

                        ShoppingDatabase(lContext).getShoppingDao().addItem(
                            ShoppingItem(
                                listId = newItemListId,
                                title = newItemName,
                                quantity = newItemQuantity,
                                price = newItemPrice
                            )
                        )

                        Toasty.info(lContext,"New Item Created", Toast.LENGTH_SHORT, true).show()
                        viewModel.setItems(ShoppingDatabase(lContext).getShoppingDao().getItem(newItemListId))
                        dismiss()
                    }
                }
            }

            mBuilder.create()
        } ?: throw IllegalStateException("Activity cannot be Null!")
    }
}