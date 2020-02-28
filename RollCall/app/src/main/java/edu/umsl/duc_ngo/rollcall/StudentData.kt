package edu.umsl.duc_ngo.rollcall

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StudentData (
    val student_name: String,
    var present: Boolean = false,
    var late: Boolean = false,
    var absence: Boolean = false,
    var unknown: Boolean = true
): Parcelable
