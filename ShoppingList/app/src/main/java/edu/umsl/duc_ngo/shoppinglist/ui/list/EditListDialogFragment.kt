package edu.umsl.duc_ngo.shoppinglist.ui.list

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
import edu.umsl.duc_ngo.shoppinglist.ui.BaseDialogFragment
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.list_dialog_fragment.view.*
import kotlinx.coroutines.launch

private const val TAG = "EditList"
class EditListDialogFragment : BaseDialogFragment() {
    companion object {
        fun newInstance() = EditListDialogFragment()
    }

    private lateinit var viewModel: ListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.let {
            ViewModelProvider(it).get(ListViewModel::class.java)
        }!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.list_dialog_fragment, container, false)
    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        return activity?.let {
            /* Dialog Configuration */
            /* Inflate and set the layout for the dialog
            NOTE: Pass null as the parent view because its going in the dialog layout. IDE will complain, but it's fine. */
            val mDialogView = requireActivity().layoutInflater.inflate(R.layout.list_dialog_fragment, null)
            val mBuilder = AlertDialog.Builder(it).setView(mDialogView)

            /* Current List Attributes */
            val currentList = viewModel.getCurrentList()

            /* Dialog Title */
            mDialogView._list_title_label.text = getString(R.string.list_dialog_edit_title)

            /* Dialog Input: list name */
            mDialogView._list_name_edit_text.setText(currentList.title)

            /* Dialog Cancellation */
            mDialogView._list_cancel_button.setOnClickListener {
                dismiss()
            }

            /* Dialog Submission */
            mDialogView._list_submit_button.setOnClickListener {
                launch {
                    context?.let { lContext ->
                        val editListName = when(mDialogView._list_name_edit_text.text.toString().isBlank()) {
                            true -> "Untitled List"
                            false -> mDialogView._list_name_edit_text.text.toString()
                        }
                        ShoppingDatabase(lContext).getShoppingDao().updateList(currentList.id, editListName)

                        Toasty.info(lContext,"List Updated", Toast.LENGTH_SHORT, true).show()
                        viewModel.setLists(ShoppingDatabase(lContext).getShoppingDao().getLists())
                        dismiss()
                    }
                }
            }

            mBuilder.create()
        } ?: throw IllegalStateException("Activity cannot be Null!")
    }
}