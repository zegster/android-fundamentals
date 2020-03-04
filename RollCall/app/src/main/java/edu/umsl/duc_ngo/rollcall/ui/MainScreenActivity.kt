package edu.umsl.duc_ngo.rollcall.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import edu.umsl.duc_ngo.rollcall.R
import edu.umsl.duc_ngo.rollcall.data.CourseModel
import edu.umsl.duc_ngo.rollcall.data.ModelHolder
import edu.umsl.duc_ngo.rollcall.data.StudentModel

class MainScreenActivity : AppCompatActivity() {
    private lateinit var courseModel: CourseModel
    private lateinit var studentModel: StudentModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_screen)

        //Initialize model
        courseModel = ModelHolder.instance.get(CourseModel::class) ?: CourseModel()
        ModelHolder.instance.set(courseModel)
        studentModel = ModelHolder.instance.get(StudentModel::class) ?: StudentModel()
        ModelHolder.instance.set(studentModel)

        val mainViewFragment = MainScreenFragment()
        if(savedInstanceState == null)
        {
            //Get the layout ID you want to inject, and pass in the fragment you want to inject into
            val transaction = this.supportFragmentManager.beginTransaction()
            transaction.add(R.id._main_screen_fgc, mainViewFragment)
            transaction.commit()
        }
    }
}
