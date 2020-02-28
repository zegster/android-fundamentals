package edu.umsl.duc_ngo.rollcall

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

class CourseModel {
    private var courses = listOf(
        CourseData(0, "Web Development with Advanced JavaScript", "M/W", "CMP SCI 4011", 4011, 5),
        CourseData(1, "Enterprise Web Development", "M/W", "CMP SCI 4012", 4012, 5),
        CourseData(2, "Android App Fundamentals", "T/TH", "CMP SCI 4020", 4020, 5)
        )

    fun getCourseSize(): Int {
        return courses.size
    }

    fun getCourse(index: Int): CourseData {
        return courses[index]
    }

    fun setCourse(index: Int, p: Int, l: Int, a: Int, u: Int) {
        courses[index].no_present = p
        courses[index].no_late = l
        courses[index].no_absence = a
        courses[index].no_unknown = u
    }
}

@Parcelize
data class CourseData (
    var cid: Int,
    var course_name: String,
    var held_day: String,
    var extended_name: String,
    var course_number: Int,
    var no_students: Int,
    var no_present: Int = 0,
    var no_late: Int = 0,
    var no_absence: Int = 0,
    var no_unknown: Int = 5
): Parcelable
