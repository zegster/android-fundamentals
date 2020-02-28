package edu.umsl.duc_ngo.rollcall

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

/*
CourseData(3, "Program Translation Techniques","CMP SCI 4280", 4020, 5, "T/TH"),
CourseData(4, "Artificial Intelligence","CMP SCI 4300", 4020, 5, "M/W"),
CourseData(5, "Database Management Systems","CMP SCI 4610", 4020, 5, "T/TH")
 */