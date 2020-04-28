package edu.umsl.duc_ngo.multipurpose.ui.note

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import edu.umsl.duc_ngo.multipurpose.R
import edu.umsl.duc_ngo.multipurpose.data.note.NoteDatabase
import edu.umsl.duc_ngo.multipurpose.data.note.NoteList
import edu.umsl.duc_ngo.multipurpose.ui.BaseDialogFragment
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.note_dialog_fragment.view.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

private const val TAG = "CreateNote"

class CreateNoteDialogFragment : BaseDialogFragment() {
    companion object {
        fun newInstance() = CreateNoteDialogFragment()
    }

    private lateinit var viewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.let {
            ViewModelProvider(it).get(NoteViewModel::class.java)
        }!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.note_dialog_fragment, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        return activity?.let {
            /* Dialog Configuration */
            /* Inflate and set the layout for the dialog
            NOTE: Pass null as the parent view because its going in the dialog layout. IDE will complain, but it's fine. */
            val mDialogView =
                requireActivity().layoutInflater.inflate(R.layout.note_dialog_fragment, null)
            val mBuilder = AlertDialog.Builder(it).setView(mDialogView)

            /* Setup colors label spinner */
            val colorLabels = arrayOf("Default", "Orange", "Yellow", "Green", "Blue")
            val spinnerAdapter =
                ArrayAdapter(context!!, android.R.layout.simple_spinner_item, colorLabels)
            mDialogView._note_colors_label_spinner.adapter = spinnerAdapter

            /* Dialog Cancellation */
            mDialogView._note_list_cancel_button.setOnClickListener {
                dismiss()
            }

            /* Dialog Submission */
            mDialogView._note_list_submit_button.setOnClickListener {
                launch {
                    context?.let { lContext ->
                        /* Get note title */
                        val newNoteTitle =
                            when (mDialogView._note_title_edit_text.text.toString().isBlank()) {
                                true -> "Untitled List"
                                false -> mDialogView._note_title_edit_text.text.toString()
                            }

                        /* Get current local date time */
                        val currentTime = LocalDateTime.now()
                        val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
                        val formatted = currentTime.format(formatter)

                        /* Get color label */
                        val newNoteColorLabel = mDialogView._note_colors_label_spinner.selectedItem.toString()

                        /* Write to database */
                        val newList = NoteList(title = newNoteTitle, timestamp = formatted, colorLabel = newNoteColorLabel)
                        NoteDatabase(lContext).getNoteDao().addList(newList)

                        Toasty.info(lContext, "New Note Created", Toast.LENGTH_SHORT, true).show()
                        viewModel.setLists(NoteDatabase(lContext).getNoteDao().getLists())
                        dismiss()
                    }
                }
            }

            mBuilder.create()
        } ?: throw IllegalStateException("Activity cannot be null!")
    }
}