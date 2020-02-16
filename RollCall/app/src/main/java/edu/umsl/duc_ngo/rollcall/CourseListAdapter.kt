package edu.umsl.duc_ngo.rollcall

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import android.view.LayoutInflater
import kotlinx.android.synthetic.main.course_row_list.view.*

class CourseListAdapter(model: CourseModel): BaseAdapter() {
    private val mModel: CourseModel = model

    /* Responsible for how many rows in the list */
    override fun getCount(): Int {
        return mModel.getCourseSize()
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItem(position: Int): Any {
        return "Return ANY from getItem()"
    }

    /* Responsible for rendering out each row of the list view */
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        /* Debug
        val textView = TextView(ctx)
        textView.text = "This is a test, testing the row!" */

        val courseRowList: View

        //Check if convertView is null, if so then inflate a new row
        if(convertView == null) {
            //LayoutInflater inflate XML files and it gives us the entire view so that we can use it as the return value
            val layoutInflater = LayoutInflater.from(parent!!.context)
            courseRowList = layoutInflater.inflate(R.layout.course_row_list, parent, false)

            //Note: findViewById is very expensive
            //Using ViewHolder pattern for performance gains: https://developer.android.com/guide/topics/ui/layout/recyclerview
            /* Solution 1
            val courseNameTV = courseRowList.findViewById<TextView>(R.id._course_name_tv)
            val extendedNameTV = courseRowList.findViewById<TextView>(R.id._extended_name_tv)
            val courseNumberTV = courseRowList.findViewById<TextView>(R.id._course_number_tv)
            val heldDayTV = courseRowList.findViewById<TextView>(R.id._held_day_tv)
            val noStudentTV = courseRowList.findViewById<TextView>(R.id._no_student_tv)
            val viewHolder = ViewHolder(courseNameTV, extendedNameTV, courseNumberTV, heldDayTV, noStudentTV) */

            /* Solution 2
            //This require plugin of: kotlin-android-extensions
            val courseNameTV = courseRowList._course_name_tv
            val extendedNameTV = courseRowList._extended_name_tv
            val courseNumberTV = courseRowList._course_number_tv
            val heldDayTV = courseRowList._held_day_tv
            val noStudentTV = courseRowList._no_student_tv
            val viewHolder = ViewHolder(courseNameTV, extendedNameTV, courseNumberTV, heldDayTV, noStudentTV) */

            /* Solution 3 */
            //This require plugin of: kotlin-android-extensions
            val viewHolder = ViewHolder(courseRowList._course_name_tv, courseRowList._extended_name_tv, courseRowList._course_number_tv, courseRowList._held_day_tv, courseRowList._no_student_tv)
            courseRowList.tag = viewHolder
        } else {
            //If not null, that mean the row is convertView then set courseRowList as that view
            courseRowList = convertView
        }

        val viewHolder = courseRowList.tag as ViewHolder
        viewHolder.courseNameTV.text = mModel.getCourse(position).course_name
        viewHolder.extendedNameTV.text = mModel.getCourse(position).extended_name
        viewHolder.courseNumberTV.text = mModel.getCourse(position).course_number.toString()
        viewHolder.heldDayTV.text = mModel.getCourse(position).held_day
        viewHolder.noStudentTV.text = mModel.getCourse(position).no_students.toString()

        return courseRowList
    }

    private class ViewHolder(val courseNameTV: TextView, val extendedNameTV: TextView, val courseNumberTV: TextView,  val heldDayTV: TextView,  val noStudentTV: TextView)
}
