package edu.umsl.duc_ngo.rollcall

class CourseModel {
    private var courses: ArrayList<CourseData> = arrayListOf(
        CourseData("Web Development with Advanced JavaScript","CMP SCI 4011", 4011, 21, "M/W", 0.0F, 0.0F, 0.0F),
        CourseData("Enterprise Web Development","CMP SCI 4012", 4012, 22, "M/W", 0.0F, 0.0F, 0.0F),
        CourseData("Android App Fundamentals","CMP SCI 4020", 4020, 23, "T/TH", 0.0F, 0.0F, 0.0F),
        CourseData("Program Translation Techniques","CMP SCI 4280", 4020, 24, "T/TH", 0.0F, 0.0F, 0.0F),
        CourseData("Artificial Intelligence","CMP SCI 4300", 4020, 25, "M/W", 0.0F, 0.0F, 0.0F),
        CourseData("Database Management Systems","CMP SCI 4610", 4020, 26, "T/TH", 0.0F, 0.0F, 0.0F)
    )

    fun getCourseSize(): Int {
        return courses.size
    }

    fun getCourse(index: Int): CourseData {
        return courses[index]
    }
}