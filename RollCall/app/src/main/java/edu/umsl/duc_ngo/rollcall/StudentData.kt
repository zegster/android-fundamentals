package edu.umsl.duc_ngo.rollcall

data class StudentData (
    val student_name: String,
    var present: Boolean,
    var late: Boolean,
    var unknown: Boolean
)
