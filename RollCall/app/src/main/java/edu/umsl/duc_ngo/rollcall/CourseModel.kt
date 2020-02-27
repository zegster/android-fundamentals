package edu.umsl.duc_ngo.rollcall

class CourseModel {
    private var courses = listOf(
        CourseData(1, "Web Development with Advanced JavaScript","CMP SCI 4011", 4011, 21, "M/W", 0.0F, 0.0F, 0.0F),
        CourseData(2, "Enterprise Web Development","CMP SCI 4012", 4012, 22, "M/W", 0.0F, 0.0F, 0.0F),
        CourseData(3, "Android App Fundamentals","CMP SCI 4020", 4020, 23, "T/TH", 0.0F, 0.0F, 0.0F),
        CourseData(4, "Program Translation Techniques","CMP SCI 4280", 4020, 24, "T/TH", 0.0F, 0.0F, 0.0F),
        CourseData(5, "Artificial Intelligence","CMP SCI 4300", 4020, 25, "M/W", 0.0F, 0.0F, 0.0F),
        CourseData(6, "Database Management Systems","CMP SCI 4610", 4020, 26, "T/TH", 0.0F, 0.0F, 0.0F)
    )

    fun getCourseSize(): Int {
        return courses.size
    }

    fun getCourse(index: Int): CourseData {
        return courses[index]
    }
}
