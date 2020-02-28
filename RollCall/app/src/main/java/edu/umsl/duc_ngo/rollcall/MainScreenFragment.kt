package edu.umsl.duc_ngo.rollcall

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.course_row.view.*
import kotlinx.android.synthetic.main.recyclerview_fragment.*

class MainScreenFragment(private var courseModel: CourseModel, private var studentModel: StudentModel): Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.recyclerview_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Adapter is a data source or a UI table view delegate to a list (which it helps rendering out the items inside of a list)
        _recyclerview_fg.layoutManager = LinearLayoutManager(activity)
        _recyclerview_fg.adapter = CourseListAdapter()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode != Activity.RESULT_OK) return
        if(data == null) return

        val resultPosition = data.getIntExtra(AttendanceScreenActivity.COURSE_ID, 0)
        val resultList = data.getParcelableArrayListExtra<StudentData>(AttendanceScreenActivity.STUDENT_LIST)
        var totalPresent = 0
        var totalLate = 0
        var totalAbsence = 0
        var totalUnknown = 0

        for(l in resultList) {
            when {
                l.present -> totalPresent++
                l.late -> totalLate++
                l.absence -> totalAbsence ++
                l.unknown -> totalUnknown ++
            }
        }

        courseModel.setCourse(resultPosition, totalPresent, totalLate, totalAbsence, totalUnknown)
        _recyclerview_fg.layoutManager = LinearLayoutManager(activity)
        _recyclerview_fg.adapter = CourseListAdapter()
    }


    inner class CourseListAdapter: RecyclerView.Adapter<CourseListHolder>() {
        /* Return number of items to render */
        override fun getItemCount(): Int {
            return courseModel.getCourseSize()
        }

        /* Called by RecyclerView to display the data at the specified position. */
        override fun onBindViewHolder(holder: CourseListHolder, position: Int) {
            holder.courseBind(courseModel, position, studentModel)
        }

        /* Called when RecyclerView needs a new ViewHolder of the given type to represent an item. */
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseListHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val itemLayoutView = layoutInflater.inflate(R.layout.course_row, parent, false)
            return CourseListHolder(itemLayoutView)
        }
    }


    inner class CourseListHolder(private val customView: View): RecyclerView.ViewHolder(customView) {
        private var courseId: Int = 0
        private lateinit var studentModel: StudentModel

        fun courseBind(courseModel: CourseModel, coursePosition: Int, studentModel: StudentModel) {
            //Populating static information
            val currentCourse = courseModel.getCourse(coursePosition)
            customView._course_name_tv.text = currentCourse.course_name
            customView._held_day_tv.text = currentCourse.held_day
            customView._extended_name_tv.text = currentCourse.extended_name
            customView._course_number_tv.text = currentCourse.course_number.toString()

            //Calculating attendance
            val present = (currentCourse.no_present.toFloat() / currentCourse.no_students.toFloat()) * 100.0F
            val late = (currentCourse.no_late.toFloat() /currentCourse.no_students.toFloat()) * 100.0F
            val absence = (currentCourse.no_absence.toFloat() / currentCourse.no_students.toFloat()) * 100.0F
            val unknown = (currentCourse.no_unknown.toFloat() / currentCourse.no_students.toFloat()) * 100.0F

            //Updating course row label
            val ns = "No. Student: " + currentCourse.no_students.toString()
            val p = "Present: " + present.toInt().toString() + "%"
            val l = "Late: " + late.toInt().toString() + "%"
            val a = "Absence: " + absence.toInt().toString() + "%"
            val u = "Unknown: " + unknown.toInt().toString() + "%"

            customView._no_student_tv.text = ns
            customView._present_rate_tv.text = p
            customView._late_rate_tv.text = l
            customView._unknown_rate_tv.text = u

            //Passing information to clickable row
            courseId = coursePosition
            this.studentModel = studentModel
            customView.setOnClickListener(listener)
        }

        private var listener = View.OnClickListener {
            val intent = AttendanceScreenActivity.newIntent(activity, courseId, studentModel.getStudentRoster(courseId))
            startActivityForResult(intent, 1)
        }
    }
}
