package edu.umsl.duc_ngo.multipurpose.ui.note.list

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
import androidx.sqlite.db.SimpleSQLiteQuery
import edu.umsl.duc_ngo.multipurpose.R
import edu.umsl.duc_ngo.multipurpose.data.note.NoteDatabase
import edu.umsl.duc_ngo.multipurpose.ui.BaseDialogFragment
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.note_list_dialog_fragment.view.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class EditNoteListDialogFragment : BaseDialogFragment() {
    companion object {
        fun newInstance() = EditNoteListDialogFragment()
    }

    private lateinit var viewModel: NoteListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.let {
            ViewModelProvider(it).get(NoteListViewModel::class.java)
        }!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.note_list_dialog_fragment, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        return activity?.let {
            /* Dialog Configuration */
            /* Inflate and set the layout for the dialog
            NOTE: Pass null as the parent view because its going in the dialog layout. IDE will complain, but it's fine. */
            val mDialogView = requireActivity().layoutInflater.inflate(R.layout.note_list_dialog_fragment, null)
            val mBuilder = AlertDialog.Builder(it).setView(mDialogView)

            /* Current List Attributes */
            val currentList = viewModel.getCurrentList()

            /* Dialog Header */
            mDialogView._note_dialog_header.text = getString(R.string.note_list_dialog_edit_header)

            /* Dialog Input: title */
            mDialogView._note_title_edit_text.setText(currentList.title)

            /* Dialog Input: colorLabel (spinner) */
            val colorLabels = resources.getStringArray(R.array.note_list_dialog_colors_label)
            val spinnerAdapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_dropdown_item, colorLabels)
            mDialogView._note_colors_label_spinner.adapter = spinnerAdapter
            mDialogView._note_colors_label_spinner.setSelection(
                viewModel.getColorLabel(currentList.colorLabel, true).toInt()
            )

            /* Dialog Cancellation */
            mDialogView._note_dialog_cancel_button.setOnClickListener {
                dismiss()
            }

            /* Dialog Submission */
            mDialogView._note_dialog_submit_button.setOnClickListener {
                launch {
                    context?.let { lContext ->
                        /* Get title */
                        val editNoteTitle = when (mDialogView._note_title_edit_text.text.toString().isBlank()) {
                            true -> "Untitled List"
                            false -> mDialogView._note_title_edit_text.text.toString()
                        }

                        /* Get current local date time */
                        val currentTime = LocalDateTime.now()
                        val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
                        val formatted = currentTime.format(formatter)

                        /* Get color label */
                        val editNoteColorLabel = mDialogView._note_colors_label_spinner.selectedItem.toString()

                        /* Update to database */
                        NoteDatabase(lContext).getNoteDao().updateList(
                            id = currentList.id,
                            title = editNoteTitle,
                            timestamp = formatted,
                            colorLabel = editNoteColorLabel
                        )
                        Toasty.info(lContext, "Note Updated", Toast.LENGTH_SHORT, true).show()

                        /* Update listViewModel */
                        val category = viewModel.getCurrentCategory()
                        viewModel.setLists(
                            NoteDatabase(lContext).getNoteDao()
                                .getLists(SimpleSQLiteQuery("SELECT * FROM NoteList ORDER BY $category"))
                        )
                        dismiss()
                    }
                }
            }

            mBuilder.create()
        } ?: throw IllegalStateException("Activity cannot be null!")
    }
}