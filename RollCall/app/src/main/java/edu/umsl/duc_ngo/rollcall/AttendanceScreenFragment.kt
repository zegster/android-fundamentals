package edu.umsl.duc_ngo.rollcall

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recyclerview_fragment.*
import kotlinx.android.synthetic.main.student_row.view.*

class AttendanceScreenFragment(private var list: ArrayList<StudentData>): Fragment() {
    companion object {
        private const val STUDENT_ATTENDANCE = "edu.umsl.duc_ngo.studentAttendance"
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

        //Adapter is a data source or a UI table view delegate to a list (which it helps rendering out the items inside of a list)
        _recyclerview_fg.layoutManager = LinearLayoutManager(activity)
        _recyclerview_fg.adapter = StudentListAdapter(list)
    }

    /* More Info: https://developer.android.com/reference/kotlin/androidx/recyclerview/widget/RecyclerView.Adapter */
    inner class StudentListAdapter(list: ArrayList<StudentData>): RecyclerView.Adapter<StudentViewHolder>() {
        private val studentList: ArrayList<StudentData> = list

        /* Return number of items to render */
        override fun getItemCount(): Int {
            return studentList.size
        }

        /* Called by RecyclerView to display the data at the specified position.
        NOTE: unlike android.widget.ListView, RecyclerView will not call this method again if the position of the
        item changes in the data set unless the item itself is invalidated or the new position cannot be determined. */
        override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
            holder.studentBind(studentList, position)
        }

        /* Called when RecyclerView needs a new ViewHolder of the given type to represent an item. */
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
            //Creating a view
            val layoutInflater = LayoutInflater.from(parent.context)
            val itemLayoutView = layoutInflater.inflate(R.layout.student_row, parent, false)
            return StudentViewHolder(itemLayoutView)
        }
    }

    inner class StudentViewHolder(private val customView: View): RecyclerView.ViewHolder(customView) {
        fun studentBind(list: ArrayList<StudentData>, position: Int) {
            //Pre-populate value
            customView._student_name_tv.text = list[position].student_name
            customView._student_attendance_rbtng.clearCheck()
            when {
                list[position].present -> {
                    customView._present_rbtn.isChecked = true
                    list[position].present = true
                    list[position].late = false
                    list[position].absence = false
                    list[position].unknown = false
                }
                list[position].late -> {
                    customView._late_rbtn.isChecked = true
                    list[position].present = false
                    list[position].late = true
                    list[position].absence = false
                    list[position].unknown = false
                }
                list[position].absence -> {
                    customView._absence_rbtn.isChecked = true
                    list[position].present = false
                    list[position].late = false
                    list[position].absence = true
                    list[position].unknown = false
                }
                list[position].unknown -> {
                    customView._unknown_rbtn.isChecked = true
                    list[position].present = false
                    list[position].late = false
                    list[position].absence = false
                    list[position].unknown = true
                }
            }

            //Present Radio Button
            customView._present_rbtn.setOnClickListener{
                customView._student_attendance_rbtng.clearCheck()
                customView._present_rbtn.isChecked = true
                list[position].present = true
                list[position].late = false
                list[position].absence = false
                list[position].unknown = false

                //Return updated list
                val returnIntent = AttendanceScreenActivity.newIntent(activity, position, list)
                activity?.setResult(Activity.RESULT_OK, returnIntent)
            }

            //Late Radio Button
            customView._late_rbtn.setOnClickListener{
                customView._student_attendance_rbtng.clearCheck()
                customView._late_rbtn.isChecked = true
                list[position].present = false
                list[position].late = true
                list[position].absence = false
                list[position].unknown = false

                //Return updated list
                val returnIntent = AttendanceScreenActivity.newIntent(activity, position, list)
                activity?.setResult(Activity.RESULT_OK, returnIntent)
            }

            //Absence Radio Button
            customView._absence_rbtn.setOnClickListener{
                customView._student_attendance_rbtng.clearCheck()
                customView._absence_rbtn.isChecked = true
                list[position].present = false
                list[position].late = false
                list[position].absence = true
                list[position].unknown = false

                //Return updated list
                val returnIntent = AttendanceScreenActivity.newIntent(activity, position, list)
                activity?.setResult(Activity.RESULT_OK, returnIntent)
            }

            //Unknown Radio Button
            customView._unknown_rbtn.setOnClickListener{
                customView._student_attendance_rbtng.clearCheck()
                customView._unknown_rbtn.isChecked = true
                list[position].present = false
                list[position].late = false
                list[position].absence = false
                list[position].unknown = true

                //Return updated list
                val returnIntent = AttendanceScreenActivity.newIntent(activity, position, list)
                activity?.setResult(Activity.RESULT_OK, returnIntent)
            }
        }
    }
}
