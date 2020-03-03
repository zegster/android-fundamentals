package edu.umsl.duc_ngo.rollcall.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.umsl.duc_ngo.rollcall.R
import edu.umsl.duc_ngo.rollcall.data.StudentData
import kotlinx.android.synthetic.main.recyclerview_fragment.*
import kotlinx.android.synthetic.main.student_row.view.*

class AttendanceScreenFragment: Fragment() {
    private var courseId: Int = 0
    private lateinit var studentList: ArrayList<StudentData>


    companion object {
        const val COURSE_NAME_INIT = "edu.umsl.duc_ngo.courseNameInit"
        const val COURSE_ID_INIT = "edu.umsl.duc_ngo.courseIdInit"
        const val STUDENT_LIST_INIT = "edu.umsl.duc_ngo.courseListInit"
        const val COURSE_ID_RESULT = "edu.umsl.duc_ngo.courseIdResult"
        const val STUDENT_LIST_RESULT = "edu.umsl.duc_ngo.courseListResult"

        lateinit var intent: Intent

        @JvmStatic
        fun newIntentInit(context: FragmentActivity?, courseName: String, courseId: Int, students: ArrayList<StudentData>): Intent {
            val intent = Intent(context, AttendanceScreenActivity::class.java)
            Companion.intent = intent
            intent.putExtra(COURSE_NAME_INIT, courseName)
            intent.putExtra(COURSE_ID_INIT, courseId)
            intent.putExtra(STUDENT_LIST_INIT, students)
            return intent
        }

        @JvmStatic
        fun newIntentResult(context: FragmentActivity?, courseId: Int, students: ArrayList<StudentData>) {
            intent = Intent(context, AttendanceScreenActivity::class.java)
            intent.putExtra(COURSE_ID_RESULT, courseId)
            intent.putExtra(STUDENT_LIST_RESULT, students)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.recyclerview_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Getting intent value
        courseId = intent.getIntExtra(COURSE_ID_INIT, 0)
        studentList = intent.getParcelableArrayListExtra<StudentData>(STUDENT_LIST_INIT)

        //Adapter is a data source or a UI table view delegate to a list (which it helps rendering out the items inside of a list)
        _recyclerview_fg.layoutManager = LinearLayoutManager(activity)
        _recyclerview_fg.adapter = StudentListAdapter()
    }

    /* More Info: https://developer.android.com/reference/kotlin/androidx/recyclerview/widget/RecyclerView.Adapter */
    inner class StudentListAdapter: RecyclerView.Adapter<StudentViewHolder>() {
        /* Return number of items to render */
        override fun getItemCount(): Int {
            return studentList.size
        }

        /* Called by RecyclerView to display the data at the specified position.
        NOTE: unlike android.widget.ListView, RecyclerView will not call this method again if the position of the
        item changes in the data set unless the item itself is invalidated or the new position cannot be determined. */
        override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
            holder.studentBind(position)
        }

        /* Called when RecyclerView needs a new ViewHolder of the given type to represent an item. */
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
            //Creating a view
            val layoutInflater = LayoutInflater.from(parent.context)
            val itemLayoutView = layoutInflater.inflate(R.layout.student_row, parent, false)
            return StudentViewHolder(itemLayoutView)
        }
    }

    //Reset the attendance
    fun invokeResetData() {
        for(list in studentList) {
            list.present = false
            list.late = false
            list.absence = false
            list.unknown = true
        }

        _recyclerview_fg.layoutManager = LinearLayoutManager(activity)
        _recyclerview_fg.adapter = StudentListAdapter()
    }

    inner class StudentViewHolder(private val customView: View): RecyclerView.ViewHolder(customView) {
        fun studentBind(position: Int) {
            //Pre-populate value
            customView._student_name_tv.text = studentList[position].student_name
            customView._student_attendance_rbtng.clearCheck()
            when {
                studentList[position].present -> {
                    customView._present_rbtn.isChecked = true
                    studentList[position].present = true
                    studentList[position].late = false
                    studentList[position].absence = false
                    studentList[position].unknown = false
                }
                studentList[position].late -> {
                    customView._late_rbtn.isChecked = true
                    studentList[position].present = false
                    studentList[position].late = true
                    studentList[position].absence = false
                    studentList[position].unknown = false
                }
                studentList[position].absence -> {
                    customView._absence_rbtn.isChecked = true
                    studentList[position].present = false
                    studentList[position].late = false
                    studentList[position].absence = true
                    studentList[position].unknown = false
                }
                studentList[position].unknown -> {
                    customView._unknown_rbtn.isChecked = true
                    studentList[position].present = false
                    studentList[position].late = false
                    studentList[position].absence = false
                    studentList[position].unknown = true
                }
            }

            //Make sure the return intent is not null
            newIntentResult(activity, courseId, studentList)

            //Present Radio Button
            customView._present_rbtn.setOnClickListener{
                customView._student_attendance_rbtng.clearCheck()
                customView._present_rbtn.isChecked = true
                studentList[position].present = true
                studentList[position].late = false
                studentList[position].absence = false
                studentList[position].unknown = false

                //Return updated list
                newIntentResult(activity, courseId, studentList)
            }

            //Late Radio Button
            customView._late_rbtn.setOnClickListener{
                customView._student_attendance_rbtng.clearCheck()
                customView._late_rbtn.isChecked = true
                studentList[position].present = false
                studentList[position].late = true
                studentList[position].absence = false
                studentList[position].unknown = false

                //Return updated list
                newIntentResult(activity, courseId, studentList)
            }

            //Absence Radio Button
            customView._absence_rbtn.setOnClickListener{
                customView._student_attendance_rbtng.clearCheck()
                customView._absence_rbtn.isChecked = true
                studentList[position].present = false
                studentList[position].late = false
                studentList[position].absence = true
                studentList[position].unknown = false

                //Return updated list
                newIntentResult(activity, courseId, studentList)
            }

            //Unknown Radio Button
            customView._unknown_rbtn.setOnClickListener{
                customView._student_attendance_rbtng.clearCheck()
                customView._unknown_rbtn.isChecked = true
                studentList[position].present = false
                studentList[position].late = false
                studentList[position].absence = false
                studentList[position].unknown = true

                //Return updated list
                newIntentResult(activity, courseId, studentList)
            }
        }
    }
}
