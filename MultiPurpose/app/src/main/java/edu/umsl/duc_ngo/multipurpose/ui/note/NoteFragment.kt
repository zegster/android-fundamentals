package edu.umsl.duc_ngo.multipurpose.ui.note

import android.content.Intent
import android.graphics.Color.parseColor
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.umsl.duc_ngo.multipurpose.R
import edu.umsl.duc_ngo.multipurpose.data.note.NoteDatabase
import edu.umsl.duc_ngo.multipurpose.data.note.NoteList
import edu.umsl.duc_ngo.multipurpose.ui.BaseFragment
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.note_fragment.*
import kotlinx.android.synthetic.main.note_row_layout.view.*
import kotlinx.coroutines.launch

private const val TAG = "Note"
class NoteFragment : BaseFragment() {
    companion object {
        fun newInstance() = NoteFragment()

        private lateinit var intent: Intent

        @JvmStatic
        fun newIntentInit(context: FragmentActivity?): Intent {
            val intent = Intent(context, NoteActivity::class.java)
            Companion.intent = intent
            return intent
        }
    }

    /* Global Attributes */
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
        return inflater.inflate(R.layout.note_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        /* Configure Recycle View */
        _note_recycler_view.layoutManager = LinearLayoutManager(activity)
        _note_recycler_view.setHasFixedSize(true)
        _note_recycler_view.adapter = ListAdapter(listOf(NoteList(0, "", "", "")))
        getPersistenceData()

        /* Create a new list */
        _create_note_button.setOnClickListener {
            CreateNoteDialogFragment.newInstance().show(parentFragmentManager, "CreateNoteDialog")
        }

        /* Monitor the current list data */
        viewModel.getLists().observe(viewLifecycleOwner, Observer {
            _note_recycler_view.adapter = ListAdapter(it)
        })

        /* Monitor the current list data */
        viewModel.getItems().observe(viewLifecycleOwner, Observer {
            _note_recycler_view.adapter = ListAdapter(viewModel.getLists().value!!)
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
                viewModel.setLists(NoteDatabase(it).getNoteDao().getLists())
            }
        }

        /* Get Items Data */
        launch {
            context?.let {
                viewModel.setItems(NoteDatabase(it).getNoteDao().getItems())
            }
        }
    }

    inner class ListAdapter(private val noteList: List<NoteList>): RecyclerView.Adapter<ListAdapter.ListHolder>() {
        /* Called when RecyclerView needs a new ViewHolder of the given type to represent an item. */
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListHolder {
            return ListHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.note_row_layout, parent, false)
            )
        }

        /* Return number of items to render */
        override fun getItemCount() = noteList.size

        /* Called by RecyclerView to display the data at the specified position. */
        override fun onBindViewHolder(holder: ListHolder, position: Int) {
            /* List Label */
            holder.view._note_title_text.text = noteList[position].title
            holder.view._note_total_item_label.text = viewModel.getTotalItem(noteList[position].id).toString()

            /* List Edit Button */
            holder.view._edit_note_button.setOnClickListener {
                viewModel.setCurrentList(noteList[position])
//                EditListDialogFragment.newInstance().show(parentFragmentManager, "EditListDialog")
            }

            /* List Delete Button */
            holder.view._delete_note_button.setOnClickListener {
                launch {
                    context?.let {
                        NoteDatabase(it).getNoteDao().removeList(noteList[position].id)
                        Toasty.info(it,"[${noteList[position].title}] Deleted", Toast.LENGTH_SHORT, true).show()
                        viewModel.setLists(NoteDatabase(it).getNoteDao().getLists())
                    }
                }
            }

            /* List Body */
            val drawable : Drawable = ContextCompat.getDrawable(context!!, R.drawable.custom_note_row)!!
            DrawableCompat.setTint(drawable, parseColor("#FF0000"))
            holder.view._note_table_row.background = drawable

            holder.view._note_table_row.setOnClickListener {
//                val intent = ItemFragment.newIntentInit(activity, shoppingList[position].id)
//                startActivity(intent)
            }
        }

        /* Describes an item view and metadata about its place within the RecyclerView. */
        inner class ListHolder(val view: View): RecyclerView.ViewHolder(view)
    }
}
