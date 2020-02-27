package edu.umsl.duc_ngo.rollcall

import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.course_row.view.*

/* More Info: https://developer.android.com/reference/kotlin/androidx/recyclerview/widget/RecyclerView.Adapter */
class CourseAdapter(model: CourseModel): RecyclerView.Adapter<CourseViewHolder>() {
    private val cModel: CourseModel = model

    /* Return number of items */
    override fun getItemCount(): Int {
        return cModel.getCourseSize()
    }

    /* Called by RecyclerView to display the data at the specified position.
    NOTE: unlike android.widget.ListView, RecyclerView will not call this method again if the position of the
    item changes in the data set unless the item itself is invalidated or the new position cannot be determined. */
    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        holder.courseBind(cModel, position)
    }

    /* Called when RecyclerView needs a new ViewHolder of the given type to represent an item. */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        //Creating a view
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemLayoutView = layoutInflater.inflate(R.layout.course_row, parent, false)
        return CourseViewHolder(itemLayoutView)
    }
}

class CourseViewHolder(private val cView: View): RecyclerView.ViewHolder(cView) {
    lateinit var cModel: CourseModel
    private var cPos: Int = 0

    fun courseBind(model: CourseModel, pos: Int) {
        cView._course_name_tv.text = model.getCourse(pos).course_name
        cView._held_day_tv.text = model.getCourse(pos).held_day
        cView._extended_name_tv.text = model.getCourse(pos).extended_name
        cView._course_number_tv.text = model.getCourse(pos).course_number.toString()
        cView._no_student_tv.text = model.getCourse(pos).no_students.toString()

        val attendance = (model.getCourse(pos).attendance_rate / model.getCourse(pos).no_students) * 100.0F
        val late = (model.getCourse(pos).late_rate / model.getCourse(pos).no_students) * 100.0F
        val unknown = model.getCourse(pos)
//        cView._attendance_rate_tv.text = (attendance.toInt()).toString()
//        cView._late_rate_tv.text = (late.toInt()).toString()
//        cView._unknown_rate_tv.text = model.getCourse(pos).unknown_rate.toString()
        cModel = model
        cPos = pos
        cView._take_attendance_btn.setOnClickListener(listener)
    }

    private var listener = View.OnClickListener {
//        val intent = StudentAttendanceActivity.newIntent(activity,
//            studentArray,
//            localCourse, coursePos)
//        courseSelected = localCourse
//        // intent.putExtra("Student", studentArray)
//        startActivityForResult(intent, REQUEST_CODE_PRESENT)
    }
}
