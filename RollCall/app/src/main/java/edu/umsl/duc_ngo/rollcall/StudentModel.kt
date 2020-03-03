package edu.umsl.duc_ngo.rollcall

class StudentModel {
    private var students = arrayOf(
        arrayListOf(
            StudentData("Duc"),
            StudentData("John"),
            StudentData("Jared"),
            StudentData("Danny"),
            StudentData("Jackson"),
            StudentData("Bek")),

        arrayListOf(
            StudentData("Duc"),
            StudentData("Allison"),
            StudentData("David"),
            StudentData("Daniel"),
            StudentData("James"),
            StudentData("Brandon")),

        arrayListOf(
            StudentData("Duc"),
            StudentData("Austin"),
            StudentData("Ben"),
            StudentData("Jesse"),
            StudentData("Luke"),
            StudentData("Matt"))
    )

    fun getStudentRoster(id: Int): ArrayList<StudentData> {
        return students[id]
    }

    fun getStudentRosterSize(id: Int): Int {
        return students[id].size
    }

    fun setStudent(rosterId: Int, studentId: Int, p: Boolean, l: Boolean, a: Boolean, u: Boolean) {
        students[rosterId][studentId].present = p
        students[rosterId][studentId].late = l
        students[rosterId][studentId].absence = a
        students[rosterId][studentId].unknown = u
    }
}
