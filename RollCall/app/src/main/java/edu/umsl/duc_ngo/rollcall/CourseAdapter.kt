package edu.umsl.duc_ngo.rollcall

import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.course_row.view.*

/* More Info: https://developer.android.com/reference/kotlin/androidx/recyclerview/widget/RecyclerView.Adapter */
class CourseAdapter(model: CourseModel): RecyclerView.Adapter<CourseViewHolder>() {
    private val courseModel: CourseModel = model

    /* Return number of items to render */
    override fun getItemCount(): Int {
        return courseModel.getCourseSize()
    }

    /* Called by RecyclerView to display the data at the specified position.
    NOTE: unlike android.widget.ListView, RecyclerView will not call this method again if the position of the
    item changes in the data set unless the item itself is invalidated or the new position cannot be determined. */
    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        holder.courseBind(courseModel, position)
    }

    /* Called when RecyclerView needs a new ViewHolder of the given type to represent an item. */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        //Creating a view
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemLayoutView = layoutInflater.inflate(R.layout.course_row, parent, false)
        return CourseViewHolder(itemLayoutView)
    }
}

class CourseViewHolder(private val customView: View): RecyclerView.ViewHolder(customView) {
    private lateinit var courseModel: CourseModel
    private var coursePosition: Int = 0

    fun courseBind(model: CourseModel, position: Int) {
        customView._course_name_tv.text = model.getCourse(position).course_name
        customView._held_day_tv.text = model.getCourse(position).held_day
        customView._extended_name_tv.text = model.getCourse(position).extended_name
        customView._course_number_tv.text = model.getCourse(position).course_number.toString()
        customView._no_student_tv.text = model.getCourse(position).no_students.toString()

        val present = (model.getCourse(position).present_rate / model.getCourse(position).no_students) * 100.0F
        val late = (model.getCourse(position).late_rate / model.getCourse(position).no_students) * 100.0F
        val unknown = (model.getCourse(position).unknown_rate / model.getCourse(position).no_students) * 100.0F
        customView._present_rate_tv.text = (present.toInt()).toString()
        customView._late_rate_tv.text = (late.toInt()).toString()
        customView._unknown_rate_tv.text = (unknown.toInt()).toString()
        courseModel = model
        coursePosition = position
        customView._take_attendance_btn.setOnClickListener(listener)
    }

    private var listener = View.OnClickListener {
        val intent = Intent(customView.context, AttendanceScreenActivity::class.java)
        customView.context.startActivity(intent)
    }
}
