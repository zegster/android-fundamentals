package edu.umsl.duc_ngo.rollcall.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.umsl.duc_ngo.rollcall.R
import edu.umsl.duc_ngo.rollcall.data.CourseModel
import edu.umsl.duc_ngo.rollcall.data.StudentData
import edu.umsl.duc_ngo.rollcall.data.StudentModel
import kotlinx.android.synthetic.main.course_row.view.*
import kotlinx.android.synthetic.main.recyclerview_fragment.*

class MainScreenFragment: Fragment() {
    private lateinit var courseModel: CourseModel
    private lateinit var studentModel: StudentModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        courseModel = ViewModelProvider(this).get(CourseModel::class.java)
        studentModel = ViewModelProvider(this).get(StudentModel::class.java)
        return inflater.inflate(R.layout.recyclerview_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Adapter is a data source or a UI table view delegate to a list (which it helps rendering out the items inside of a list)
        //Since it is already attach to the layout, it can be access directly
        _recyclerview_fg.layoutManager = LinearLayoutManager(activity)
        _recyclerview_fg.adapter = CourseListAdapter()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode != Activity.RESULT_OK) return
        if(data == null) return

        //Initialize variable
        val resultId = data.getIntExtra(AttendanceScreenFragment.COURSE_ID_RESULT, 0)
        val resultStudents = data.getParcelableArrayListExtra<StudentData>(AttendanceScreenFragment.STUDENT_LIST_RESULT)
        var totalPresent = 0
        var totalLate = 0
        var totalAbsence = 0
        var totalUnknown = 0

        //Getting total attendance
        for(list in resultStudents) {
            when {
                list.present -> totalPresent++
                list.late -> totalLate++
                list.absence -> totalAbsence++
                list.unknown -> totalUnknown++
            }
        }

        //Update attendance of the course model
        courseModel.setCourse(resultId, totalPresent, totalLate, totalAbsence, totalUnknown)

        //Update individual student attendance in the student model
        for(i in 0 until studentModel.getStudentRosterSize(resultId)) {
            studentModel.setStudent(resultId, i, resultStudents[i].present, resultStudents[i].late, resultStudents[i].absence ,resultStudents[i].unknown)
        }

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
        private lateinit var courseName: String
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
            customView._no_present_tv.text = p
            customView._no_late_tv.text = l
            customView._no_absence_tv.text = a
            customView._no_unknown_tv.text = u

            //Passing information to clickable row
            courseName = courseModel.getCourse(coursePosition).course_name
            courseId = coursePosition
            this.studentModel = studentModel
            customView.setOnClickListener(listener)
        }

        private var listener = View.OnClickListener {
            val intent = AttendanceScreenFragment.newIntentInit(activity, courseName, courseId, studentModel.getStudentRoster(courseId))
            startActivityForResult(intent, 1)
        }
    }
}
