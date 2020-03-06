package edu.umsl.duc_ngo.rollcall.data

import androidx.lifecycle.ViewModel

class StudentModel: ViewModel() {
    private var studentList = arrayOf(
        arrayListOf(
            StudentData("Duc"),
            StudentData("John"),
            StudentData("Jared"),
            StudentData("Danny"),
            StudentData("Jackson"),
            StudentData("Bek")
        ),

        arrayListOf(
            StudentData("Duc"),
            StudentData("Allison"),
            StudentData("David"),
            StudentData("Daniel"),
            StudentData("James"),
            StudentData("Brandon")
        ),

        arrayListOf(
            StudentData("Duc"),
            StudentData("Austin"),
            StudentData("Ben"),
            StudentData("Jesse"),
            StudentData("Luke"),
            StudentData("Matt")
        )
    )

    fun getStudentRoster(id: Int): ArrayList<StudentData> {
        return studentList[id]
    }

    fun getStudentRosterSize(id: Int): Int {
        return studentList[id].size
    }

    fun setStudent(rosterId: Int, studentId: Int, p: Boolean, l: Boolean, a: Boolean, u: Boolean) {
        studentList[rosterId][studentId].present = p
        studentList[rosterId][studentId].late = l
        studentList[rosterId][studentId].absence = a
        studentList[rosterId][studentId].unknown = u
    }
}
