package edu.umsl.duc_ngo.rollcall.ui

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import edu.umsl.duc_ngo.rollcall.R
import edu.umsl.duc_ngo.rollcall.data.CourseModel
import edu.umsl.duc_ngo.rollcall.data.ModelHolder
import es.dmoral.toasty.Toasty

class AttendanceScreenActivity: AppCompatActivity() {
    private lateinit var courseModel: CourseModel
    lateinit var mainViewFragment: AttendanceScreenFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.attendance_screen)

        //Set up back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        courseModel = ModelHolder.instance.get(CourseModel::class)!!
        val courseId = AttendanceScreenFragment.intent.getIntExtra(AttendanceScreenFragment.COURSE_ID_INIT, 0)
        title = courseModel.getCourse(courseId).course_name


        mainViewFragment = AttendanceScreenFragment()
        if(savedInstanceState == null) {
            val transaction = this.supportFragmentManager.beginTransaction()
            transaction.add(R.id._attendance_screen_fgc, mainViewFragment)
            transaction.commit()
        }
    }

    //Creating menu (three dot on upper right) icon
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.attendance_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //Indicate what the menu item should do
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId
        if(id == R.id._reset_tbbtn)
        {
            Toasty.info(this, "NOTICE: Attendance reset to default.", Toast.LENGTH_LONG, true).show()
            mainViewFragment.invokeResetData()
        }
        return super.onOptionsItemSelected(item)
    }

    //Toolbar back button
    override fun onSupportNavigateUp(): Boolean {
        //setResult must be call first before onBackPressed()
        setResult(Activity.RESULT_OK, AttendanceScreenFragment.intent)
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    //Android back button (bottom toolbar)
    override fun onBackPressed() {
        //setResult must be call first before onBackPressed()
        setResult(Activity.RESULT_OK, AttendanceScreenFragment.intent)

        Log.i("World", intent.toString())
        super.onBackPressed()
    }
}
