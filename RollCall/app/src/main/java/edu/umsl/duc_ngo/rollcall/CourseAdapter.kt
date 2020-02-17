package edu.umsl.duc_ngo.rollcall

import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.course_row.view.*

/* More Info: https://developer.android.com/reference/kotlin/androidx/recyclerview/widget/RecyclerView.Adapter */
class CourseAdapter(model: CourseModel): RecyclerView.Adapter<CourseViewHolder>() {
    private val mModel: CourseModel = model

    /* Return number of items */
    override fun getItemCount(): Int {
        return mModel.getCourseSize()
    }

    /* Called by RecyclerView to display the data at the specified position.
    NOTE: unlike android.widget.ListView, RecyclerView will not call this method again if the position of the
    item changes in the data set unless the item itself is invalidated or the new position cannot be determined. */
    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        holder.view._course_name_tv.text = mModel.getCourse(position).course_name
        holder.view._held_day_tv.text = mModel.getCourse(position).held_day
        holder.view._extended_name_tv.text = mModel.getCourse(position).extended_name
        holder.view._course_number_tv.text = mModel.getCourse(position).course_number.toString()
        holder.view._no_student_tv.text = mModel.getCourse(position).no_students.toString()
    }

    /* Called when RecyclerView needs a new ViewHolder of the given type to represent an item. */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        //Creating a view
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.course_row, parent, false)
        return CourseViewHolder(cellForRow)
    }
}

class CourseViewHolder(val view: View): RecyclerView.ViewHolder(view) {

}