package edu.umsl.duc_ngo.multipurpose.ui.note.item

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
import edu.umsl.duc_ngo.multipurpose.ui.BaseFragment
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.note_item_editor_fragment.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class CreateNoteItemFragment : BaseFragment() {
    companion object {
        fun newInstance() = CreateNoteItemFragment()
    }

    private lateinit var viewModel: NoteItemViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.let {
            ViewModelProvider(it).get(NoteItemViewModel::class.java)
        }!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.note_item_editor_fragment, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        /* Editor Title */
        _note_editor_header.text = getString(R.string.note_item_new_header)

        /* Editor Cancellation */
        _note_editor_return_button.setOnClickListener {
            activity?.onBackPressed()
        }

        /* Dialog Submission */
        _note_editor_submit_button.setOnClickListener {
            launch {
                context?.let { lContext ->
                    /* Get list id */
                    val newItemListId = viewModel.getCurrentList().value!!.id

                    /* Get title */
                    val newItemTitle = when (_note_title_edit_text.text.toString().isBlank()) {
                        true -> "Untitled Item"
                        false -> _note_title_edit_text.text.toString()
                    }

                    /* Get content */
                    val newItemContent = _note_content_edit_text.text.toString()

                    /* Get current local date time */
                    val currentTime = LocalDateTime.now()
                    val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
                    val formatted = currentTime.format(formatter)

                    /* Write to database */
                    val newItem = NoteItem(
                        listId = newItemListId,
                        title = newItemTitle,
                        timestamp = formatted,
                        content = newItemContent
                    )
                    NoteDatabase(lContext).getNoteDao().addItem(newItem)
                    Toasty.info(lContext, "New Page Created", Toast.LENGTH_SHORT, true).show()

                    /* Update itemViewModel */
                    viewModel.setItems(NoteDatabase(lContext).getNoteDao().getItem(newItemListId))
                    activity?.onBackPressed()
                }
            }
        }
    }
}