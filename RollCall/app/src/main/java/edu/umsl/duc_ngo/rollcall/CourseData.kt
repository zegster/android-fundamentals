package edu.umsl.duc_ngo.rollcall

data class CourseData (
    var cid: Int,
    var course_name: String,
    var extended_name: String,
    var course_number: Int,
    var no_students: Int,
    var held_day: String,
    var present_rate: Float,
    var late_rate: Float,
    var unknown_rate: Float
)

