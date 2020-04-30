package edu.umsl.duc_ngo.multipurpose.ui.note.item

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.umsl.duc_ngo.multipurpose.R
import edu.umsl.duc_ngo.multipurpose.data.note.NoteDatabase
import edu.umsl.duc_ngo.multipurpose.data.note.NoteItem
import edu.umsl.duc_ngo.multipurpose.ui.BaseFragment
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.note_item_fragment.*
import kotlinx.android.synthetic.main.note_item_row_layout.view.*
import kotlinx.coroutines.launch

class NoteItemFragment : BaseFragment() {
    companion object {
        fun newInstance() = NoteItemFragment()

        private const val LIST_ID = "edu.umsl.duc_ngo.list_id"
        lateinit var intent: Intent

        @JvmStatic
        fun newIntentInit(context: FragmentActivity?, listId: Long): Intent {
            val intent = Intent(context, NoteItemActivity::class.java)
            intent.putExtra(LIST_ID, listId)
            Companion.intent = intent
            return intent
        }
    }

    /* Global Attributes */
    private lateinit var viewModel: NoteItemViewModel
    private var listId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.let {
            ViewModelProvider(it).get(NoteItemViewModel::class.java)
        }!!

        listId = intent.getLongExtra(LIST_ID, -1)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.note_item_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        /* Configure Recycle View */
        _note_recycler_view.layoutManager = LinearLayoutManager(activity)
        _note_recycler_view.setHasFixedSize(true)
        _note_recycler_view.adapter = ItemAdapter(listOf(NoteItem(0, 0, "Empty", "", "")))

        /* Get List Data */
        launch {
            context?.let {
                viewModel.setCurrentList(NoteDatabase(it).getNoteDao().getList(listId))
            }
        }

        /* Get Items Data */
        launch {
            context?.let {
                viewModel.setItems(NoteDatabase(it).getNoteDao().getItem(listId))
            }
        }

        /* Go back to previous activity */
        _note_return_button.setOnClickListener {
            activity?.onBackPressed()
        }

        /* Create a new item */
        _note_create_button.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id._base_activity, CreateNoteItemFragment.newInstance())
                .addToBackStack(null).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit()
        }

        /* Monitor the item header label */
        viewModel.getCurrentList().observe(viewLifecycleOwner, Observer {
            if (it.title.isNotEmpty()) {
                _note_label.text = it.title
            }
        })

        /* Monitor the current item data */
        viewModel.getItems().observe(viewLifecycleOwner, Observer {
            _note_recycler_view.adapter = ItemAdapter(it)
        })
    }

    inner class ItemAdapter(private val noteItem: List<NoteItem>) : RecyclerView.Adapter<ItemAdapter.ItemHolder>() {
        /* Called when RecyclerView needs a new ViewHolder of the given type to represent an item. */
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
            return ItemHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.note_item_row_layout, parent, false)
            )
        }

        /* Return number of items to render */
        override fun getItemCount() = noteItem.size

        /* Called by RecyclerView to display the data at the specified position. */
        override fun onBindViewHolder(holder: ItemHolder, position: Int) {
            /* Label */
            holder.view._note_title_text.text = noteItem[position].title
            holder.view._note_timestamp_text.text = noteItem[position].timestamp

            /* Predefine Listener */
            val editListener = View.OnClickListener {
                viewModel.setCurrentItem(noteItem[position])
                parentFragmentManager.beginTransaction().replace(R.id._base_activity, EditNoteItemFragment.newInstance())
                    .addToBackStack(null).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit()
            }

            /* Edit Button */
            holder.view._note_edit_button.setOnClickListener(editListener)

            /* Delete Button */
            holder.view._note_delete_button.setOnClickListener {
                launch {
                    context?.let {
                        NoteDatabase(it).getNoteDao().removeItem(noteItem[position].id)
                        Toasty.info(it, "[${noteItem[position].title}] Deleted", Toast.LENGTH_SHORT, true).show()
                        viewModel.setItems(NoteDatabase(it).getNoteDao().getItem(listId))
                    }
                }
            }

            /* Body */
            holder.view._note_table_row.setOnClickListener(editListener)
        }

        /* Describes an item view and metadata about its place within the RecyclerView. */
        inner class ItemHolder(val view: View) : RecyclerView.ViewHolder(view)
    }
}