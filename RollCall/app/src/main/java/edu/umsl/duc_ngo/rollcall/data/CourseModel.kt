package edu.umsl.duc_ngo.rollcall.data

import androidx.lifecycle.ViewModel

class CourseModel: ViewModel() {
    private var courseList= listOf(
        CourseData(0, "Web Development with Advanced JavaScript", "M/W", "CMP SCI 4011", 4011, 6),
        CourseData(1, "Enterprise Web Development", "M/W", "CMP SCI 4012", 4012, 6),
        CourseData(2, "Android App Fundamentals", "T/TH", "CMP SCI 4020", 4020, 6)
    )

    fun getCourseSize(): Int {
        return courseList.size
    }

    fun getCourse(index: Int): CourseData {
        return courseList[index]
    }

    fun setCourse(index: Int, p: Int, l: Int, a: Int, u: Int) {
        courseList[index].no_present = p
        courseList[index].no_late = l
        courseList[index].no_absence = a
        courseList[index].no_unknown = u
    }
}
