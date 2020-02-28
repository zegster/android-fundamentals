package edu.umsl.duc_ngo.rollcall

class StudentModel {
    private var students = arrayOf(
        arrayListOf(
            StudentData("Duc"),
            StudentData("John"),
            StudentData("Jared"),
            StudentData("Danny"),
            StudentData("Jackson")),

        arrayListOf(
            StudentData("Allison"),
            StudentData("David"),
            StudentData("Daniel"),
            StudentData("James"),
            StudentData("Brandon")),

        arrayListOf(
            StudentData("Austin"),
            StudentData("Ben"),
            StudentData("Jesse"),
            StudentData("Luke"),
            StudentData("Matt"))
    )

    fun getStudentRoster(id: Int): ArrayList<StudentData> {
        return students[id]
    }
}
