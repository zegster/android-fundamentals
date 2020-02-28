package edu.umsl.duc_ngo.rollcall

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity

class AttendanceScreenActivity: AppCompatActivity() {
    private var courseId: Int = 0
    lateinit var studentList: ArrayList<StudentData>

    companion object {
        const val COURSE_ID_INIT = "edu.umsl.duc_ngo.courseIdInit"
        const val STUDENT_LIST_INIT = "edu.umsl.duc_ngo.courseListInit"
        const val COURSE_ID_RESULT = "edu.umsl.duc_ngo.courseIdResult"
        const val STUDENT_LIST_RESULT = "edu.umsl.duc_ngo.courseListResult"

        @JvmStatic
        fun newIntentInit(context: FragmentActivity?, courseId: Int, students: ArrayList<StudentData>): Intent {
            val intent = Intent(context, AttendanceScreenActivity::class.java)
            intent.putExtra(COURSE_ID_INIT, courseId)
            intent.putExtra(STUDENT_LIST_INIT, students)
            return intent
        }

        lateinit var intent: Intent
        @JvmStatic
        fun newIntentResult(context: FragmentActivity?, courseId: Int, students: ArrayList<StudentData>) {
            intent = Intent(context, AttendanceScreenActivity::class.java)
            intent.putExtra(COURSE_ID_RESULT, courseId)
            intent.putExtra(STUDENT_LIST_RESULT, students)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.attendance_screen)

        courseId = intent.getIntExtra(COURSE_ID_INIT, 0)
        studentList = intent.getParcelableArrayListExtra<StudentData>(STUDENT_LIST_INIT)

        val mainViewFragment = AttendanceScreenFragment(courseId, studentList)
        if(savedInstanceState == null) {
            val transaction = this.supportFragmentManager.beginTransaction()
            transaction.add(R.id._attendance_screen_fgc, mainViewFragment)
            transaction.commit()
        }
    }

    override fun onBackPressed() {
        //setResult must be call first before onBackPressed()
        setResult(Activity.RESULT_OK, AttendanceScreenActivity.intent)
        super.onBackPressed()
    }
}
