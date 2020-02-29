package edu.umsl.duc_ngo.rollcall

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import es.dmoral.toasty.Toasty


class AttendanceScreenActivity: AppCompatActivity() {
    private lateinit var courseName: String
    private var courseId: Int = 0
    lateinit var studentList: ArrayList<StudentData>
    lateinit var mainViewFragment: AttendanceScreenFragment

    companion object {
        const val COURSE_NAME_INIT = "edu.umsl.duc_ngo.courseNameInit"
        const val COURSE_ID_INIT = "edu.umsl.duc_ngo.courseIdInit"
        const val STUDENT_LIST_INIT = "edu.umsl.duc_ngo.courseListInit"
        const val COURSE_ID_RESULT = "edu.umsl.duc_ngo.courseIdResult"
        const val STUDENT_LIST_RESULT = "edu.umsl.duc_ngo.courseListResult"

        @JvmStatic
        fun newIntentInit(context: FragmentActivity?, courseName: String, courseId: Int, students: ArrayList<StudentData>): Intent {
            val intent = Intent(context, AttendanceScreenActivity::class.java)
            intent.putExtra(COURSE_NAME_INIT, courseName)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.attendance_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId
        if(id == R.id._reset_tbbtn)
        {
            Toasty.info(this, "NOTICE: Attendance reset to default.", Toast.LENGTH_LONG, true).show()
            mainViewFragment.invokeResetData()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.attendance_screen)

        //Getting intent value
        courseName = intent.getStringExtra(COURSE_NAME_INIT)
        courseId = intent.getIntExtra(COURSE_ID_INIT, 0)
        studentList = intent.getParcelableArrayListExtra<StudentData>(STUDENT_LIST_INIT)
        title = courseName

        //Set up back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Injecting Fragment
        mainViewFragment = AttendanceScreenFragment(courseId, studentList)
        if(savedInstanceState == null) {
            val transaction = this.supportFragmentManager.beginTransaction()
            transaction.add(R.id._attendance_screen_fgc, mainViewFragment)
            transaction.commit()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        //setResult must be call first before onBackPressed()
        setResult(Activity.RESULT_OK, AttendanceScreenActivity.intent)
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        //setResult must be call first before onBackPressed()
        setResult(Activity.RESULT_OK, AttendanceScreenActivity.intent)
        super.onBackPressed()
    }
}
