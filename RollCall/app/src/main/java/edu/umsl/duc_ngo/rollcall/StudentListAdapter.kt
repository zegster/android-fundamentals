package edu.umsl.duc_ngo.rollcall

import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.student_row.view.*

/* More Info: https://developer.android.com/reference/kotlin/androidx/recyclerview/widget/RecyclerView.Adapter */
class StudentListAdapter(model: StudentModel): RecyclerView.Adapter<StudentViewHolder>() {
    private val studentModel: StudentModel = model

    /* Return number of items to render */
    override fun getItemCount(): Int {
        return studentModel.getStudentSize(0)
    }

    /* Called by RecyclerView to display the data at the specified position.
    NOTE: unlike android.widget.ListView, RecyclerView will not call this method again if the position of the
    item changes in the data set unless the item itself is invalidated or the new position cannot be determined. */
    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        holder.studentBind(studentModel, position)
    }

    /* Called when RecyclerView needs a new ViewHolder of the given type to represent an item. */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        //Creating a view
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemLayoutView = layoutInflater.inflate(R.layout.student_row, parent, false)
        return StudentViewHolder(itemLayoutView)
    }
}

class StudentViewHolder(private val customView: View): RecyclerView.ViewHolder(customView) {
    private lateinit var studentModel: StudentModel
    private var studentPosition: Int = 0

    fun studentBind(model: StudentModel, position: Int) {
        customView._student_name_tv.text = model.getStudent(0, position).student_name
        studentModel = model
        studentPosition = position
    }
}
