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
import edu.umsl.duc_ngo.multipurpose.ui.BaseFragment
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.note_item_editor_fragment.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class EditNoteItemFragment : BaseFragment() {
    companion object {
        fun newInstance() = EditNoteItemFragment()
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

        /* Current Item Attributes */
        val currentItem = viewModel.getCurrentItem()

        /* Editor Header */
        _note_editor_header.text = getString(R.string.note_item_edit_header)

        /* Editor Input: title, content */
        _note_title_edit_text.setText(currentItem.title)
        _note_content_edit_text.setText(currentItem.content)

        /* Editor Cancellation */
        _note_editor_return_button.setOnClickListener {
            activity?.onBackPressed()
        }

        /* Dialog Submission */
        _note_editor_submit_button.setOnClickListener {
            launch {
                context?.let { lContext ->
                    /* Get title */
                    val editItemTitle = when (_note_title_edit_text.text.toString().isBlank()) {
                        true -> "Untitled Item"
                        false -> _note_title_edit_text.text.toString()
                    }

                    /* Get content */
                    val editItemContent = _note_content_edit_text.text.toString()

                    /* Get current local date time */
                    val currentTime = LocalDateTime.now()
                    val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
                    val formatted = currentTime.format(formatter)

                    /* Update to database */
                    NoteDatabase(lContext).getNoteDao().updateItem(
                        id = currentItem.id,
                        title = editItemTitle,
                        timestamp = formatted,
                        content = editItemContent
                    )
                    Toasty.info(lContext, "Page Updated", Toast.LENGTH_SHORT, true).show()

                    /* Update itemViewModel */
                    viewModel.setItems(NoteDatabase(lContext).getNoteDao().getItem(currentItem.listId))
                    activity?.onBackPressed()
                }
            }
        }
    }
}