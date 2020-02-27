package edu.umsl.duc_ngo.rollcall

class StudentModel {
    private var students = listOf(
        StudentData("Duc", present = true, late = false, unknown = false),
        StudentData("John", present = true, late = false, unknown = false),
        StudentData("Jared", present = true, late = false, unknown = false),
        StudentData("Danny", present = true, late = false, unknown = false),
        StudentData("Jackson", present = true, late = false, unknown = false)
    )

    fun getStudentSize(): Int {
        return students.size
    }

    fun getStudent(index: Int): StudentData {
        return students[index]
    }
}
