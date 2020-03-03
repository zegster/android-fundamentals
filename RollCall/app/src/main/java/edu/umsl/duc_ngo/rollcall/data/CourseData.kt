package edu.umsl.duc_ngo.rollcall.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

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
    var no_unknown: Int = 6
): Parcelable
