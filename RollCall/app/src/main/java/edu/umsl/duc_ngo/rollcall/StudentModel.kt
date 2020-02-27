package edu.umsl.duc_ngo.rollcall

class StudentModel {
    private var students = listOf(
        listOf(
            StudentData("Duc", present = true, late = false, unknown = false),
            StudentData("John", present = true, late = false, unknown = false),
            StudentData("Jared", present = true, late = false, unknown = false),
            StudentData("Danny", present = true, late = false, unknown = false),
            StudentData("Jackson", present = true, late = false, unknown = false)),

        listOf(
            StudentData("Allison", present = true, late = false, unknown = false),
            StudentData("David", present = true, late = false, unknown = false),
            StudentData("Daniel", present = true, late = false, unknown = false),
            StudentData("James", present = true, late = false, unknown = false),
            StudentData("Brandon", present = true, late = false, unknown = false)),

        listOf(
            StudentData("Austin", present = true, late = false, unknown = false),
            StudentData("Ben", present = true, late = false, unknown = false),
            StudentData("Jesse", present = true, late = false, unknown = false),
            StudentData("Luke", present = true, late = false, unknown = false),
            StudentData("Matt", present = true, late = false, unknown = false))
    )

    fun getStudentSize(id: Int): Int {
        return students[id].size
    }

    fun getStudent(id: Int, index: Int): StudentData {
        return students[id][index]
    }
}
