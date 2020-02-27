package edu.umsl.duc_ngo.rollcall

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class AttendanceScreenActivity: AppCompatActivity() {
    lateinit var studentModel: StudentModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.attendance_screen)

        //If it null, create a new instance. Model will never be null because we did lateinit
        studentModel = StudentModel()


        val mainViewFragment = AttendanceScreenFragment(studentModel)
        val transaction = this.supportFragmentManager.beginTransaction()

        transaction.add(R.id._attendance_screen_fgc, mainViewFragment)
        transaction.commit()
    }
}
