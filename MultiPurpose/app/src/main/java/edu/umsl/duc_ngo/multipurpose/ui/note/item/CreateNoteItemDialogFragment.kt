package edu.umsl.duc_ngo.multipurpose.ui.note.item

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import edu.umsl.duc_ngo.multipurpose.R
import edu.umsl.duc_ngo.multipurpose.data.note.NoteDatabase
import edu.umsl.duc_ngo.multipurpose.data.note.NoteItem
import edu.umsl.duc_ngo.multipurpose.ui.BaseDialogFragment
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.note_item_dialog_fragment.view.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class CreateNoteItemDialogFragment : BaseDialogFragment() {
    companion object {
        fun newInstance() = CreateNoteItemDialogFragment()
    }

    private lateinit var viewModel: NoteItemViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.let {
            ViewModelProvider(it).get(NoteItemViewModel::class.java)
        }!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.note_item_dialog_fragment, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        return activity?.let {
            /* Dialog Configuration */
            /* Inflate and set the layout for the dialog
            NOTE: Pass null as the parent view because its going in the dialog layout. IDE will complain, but it's fine. */
            val mDialogView = requireActivity().layoutInflater.inflate(R.layout.note_item_dialog_fragment, null)
            val mBuilder = AlertDialog.Builder(it).setView(mDialogView)

            /* Dialog Title */
            mDialogView._note_dialog_header.text = getString(R.string.note_item_dialog_new_header)

            /* Dialog Cancellation */
            mDialogView._note_dialog_cancel_button.setOnClickListener {
                dismiss()
            }

            /* Dialog Submission */
            mDialogView._note_dialog_submit_button.setOnClickListener {
                launch {
                    context?.let { lContext ->
                        /* Get list id */
                        val newItemListId = viewModel.getCurrentList().value!!.id

                        /* Get title */
                        val newItemTitle = when(mDialogView._note_title_edit_text.text.toString().isBlank()) {
                            true -> "Untitled Item"
                            false -> mDialogView._note_title_edit_text.text.toString()
                        }

                        /* Get current local date time */
                        val currentTime = LocalDateTime.now()
                        val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
                        val formatted = currentTime.format(formatter)

                        /* Write to database */
                        val newItem = NoteItem(listId = newItemListId,title = newItemTitle, timestamp = formatted, content = "")
                        NoteDatabase(lContext).getNoteDao().addItem(newItem)
                        Toasty.info(lContext,"New Page Created", Toast.LENGTH_SHORT, true).show()

                        /* Update itemViewModel */
                        viewModel.setItems(NoteDatabase(lContext).getNoteDao().getItem(newItemListId))
                        dismiss()
                    }
                }
            }

            mBuilder.create()
        } ?: throw IllegalStateException("Activity cannot be null!")
    }
}