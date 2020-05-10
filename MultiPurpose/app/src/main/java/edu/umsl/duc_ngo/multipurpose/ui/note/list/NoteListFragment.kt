package edu.umsl.duc_ngo.multipurpose.ui.note.list

import android.content.Intent
import android.graphics.Color.parseColor
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.sqlite.db.SimpleSQLiteQuery
import edu.umsl.duc_ngo.multipurpose.R
import edu.umsl.duc_ngo.multipurpose.data.note.NoteDatabase
import edu.umsl.duc_ngo.multipurpose.data.note.NoteList
import edu.umsl.duc_ngo.multipurpose.ui.BaseFragment
import edu.umsl.duc_ngo.multipurpose.ui.note.item.NoteItemFragment
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.note_list_fragment.*
import kotlinx.android.synthetic.main.note_list_row_layout.view.*
import kotlinx.coroutines.launch

class NoteListFragment : BaseFragment() {
    companion object {
        fun newInstance() = NoteListFragment()

        private lateinit var intent: Intent

        @JvmStatic
        fun newIntentInit(context: FragmentActivity?): Intent {
            val intent = Intent(context, NoteListActivity::class.java)
            Companion.intent = intent
            return intent
        }
    }

    /* Global Attributes */
    private lateinit var viewModel: NoteListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.let {
            ViewModelProvider(it).get(NoteListViewModel::class.java)
        }!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.note_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        /* Configure Recycle View */
        _note_recycler_view.layoutManager = LinearLayoutManager(activity)
        _note_recycler_view.setHasFixedSize(true)
        _note_recycler_view.adapter = ListAdapter(listOf(NoteList(0, "", "", "")))
        val categories = resources.getStringArray(R.array.note_list_categories)
        val spinnerAdapter = ArrayAdapter(context!!, R.layout.note_list_categories_spinner, categories)
        _note_categories_spinner.adapter = spinnerAdapter
        getPersistenceData()

        /* Go back to previous activity */
        _note_return_button.setOnClickListener {
            activity?.onBackPressed()
        }

        /* Sort by spinner */
        _note_categories_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                /* Get Lists Data */
                launch {
                    context?.let {
                        viewModel.setCurrentCategory(_note_categories_spinner.selectedItem.toString())
                        val category = viewModel.getCurrentCategory()
                        viewModel.setLists(
                            NoteDatabase(it).getNoteDao()
                                .getLists(SimpleSQLiteQuery("SELECT * FROM NoteList ORDER BY $category"))
                        )
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        /* Create a new list */
        _note_create_button.setOnClickListener {
            CreateNoteListDialogFragment.newInstance().show(parentFragmentManager, "CreateNoteDialog")
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
                val category = viewModel.getCurrentCategory()
                viewModel.setLists(
                    NoteDatabase(it).getNoteDao().getLists(SimpleSQLiteQuery("SELECT * FROM NoteList ORDER BY $category"))
                )
            }
        }

        /* Get Items Data */
        launch {
            context?.let {
                viewModel.setItems(NoteDatabase(it).getNoteDao().getItems())
            }
        }
    }

    inner class ListAdapter(private val noteList: List<NoteList>) : RecyclerView.Adapter<ListAdapter.ListHolder>() {
        /* Called when RecyclerView needs a new ViewHolder of the given type to represent an item. */
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListHolder {
            return ListHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.note_list_row_layout, parent, false)
            )
        }

        /* Return number of items to render */
        override fun getItemCount() = noteList.size

        /* Called by RecyclerView to display the data at the specified position. */
        override fun onBindViewHolder(holder: ListHolder, position: Int) {
            /* Label */
            holder.view._note_title_text.text = noteList[position].title
            holder.view._note_timestamp_text.text = noteList[position].timestamp
            holder.view._note_total_item_label.text = viewModel.getTotalItem(noteList[position].id).toString()

            /* Edit Button */
            holder.view._note_edit_button.setOnClickListener {
                viewModel.setCurrentList(noteList[position])
                EditNoteListDialogFragment.newInstance().show(parentFragmentManager, "EditNoteDialog")
            }

            /* Delete Button */
            holder.view._note_delete_button.setOnClickListener {
                launch {
                    context?.let {
                        NoteDatabase(it).getNoteDao().removeList(noteList[position].id)
                        Toasty.info(it, "[${noteList[position].title}] Deleted", Toast.LENGTH_SHORT, true).show()

                        val category = viewModel.getCurrentCategory()
                        viewModel.setLists(
                            NoteDatabase(it).getNoteDao()
                                .getLists(SimpleSQLiteQuery("SELECT * FROM NoteList ORDER BY $category"))
                        )
                    }
                }
            }

            /* Body: Setting up color tint and listener */
            val colorLabel = viewModel.getColorLabel(noteList[position].colorLabel)
            holder.view._note_table_row.background.setTint(parseColor(colorLabel))
            holder.view._note_table_row.setOnClickListener {
                val intent = NoteItemFragment.newIntentInit(activity, noteList[position].id)
                startActivity(intent)
            }
        }

        /* Describes an item view and metadata about its place within the RecyclerView. */
        inner class ListHolder(val view: View) : RecyclerView.ViewHolder(view)
    }
}