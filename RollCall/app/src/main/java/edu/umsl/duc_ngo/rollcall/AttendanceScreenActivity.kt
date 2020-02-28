package edu.umsl.duc_ngo.rollcall

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity

class AttendanceScreenActivity: AppCompatActivity() {
    private var courseId: Int = 0
    lateinit var studentList: ArrayList<StudentData>

    companion object {
        const val COURSE_ID = "edu.umsl.duc_ngo.courseId"
        const val STUDENT_LIST = "edu.umsl.duc_ngo.courseList"

        @JvmStatic
        fun newIntent(context: FragmentActivity?, courseId: Int, students: ArrayList<StudentData>): Intent {
            val intent = Intent(context, AttendanceScreenActivity::class.java)
            intent.putExtra(COURSE_ID, courseId)
            intent.putExtra(STUDENT_LIST, students)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.attendance_screen)

        courseId = intent.getIntExtra(COURSE_ID, 0)
        studentList = intent.getParcelableArrayListExtra<StudentData>(STUDENT_LIST)

        val mainViewFragment = AttendanceScreenFragment(studentList)
        val transaction = this.supportFragmentManager.beginTransaction()

        transaction.add(R.id._attendance_screen_fgc, mainViewFragment)
        transaction.commit()
    }
}
