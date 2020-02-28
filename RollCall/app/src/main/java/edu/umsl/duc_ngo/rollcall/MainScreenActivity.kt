package edu.umsl.duc_ngo.rollcall

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainScreenActivity : AppCompatActivity() {
    private lateinit var courseModel: CourseModel
    lateinit var studentModel: StudentModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_screen)

        savedInstanceState?.let{
            courseModel = CourseModel()
            studentModel = StudentModel()
        }

        if(!::courseModel.isInitialized) {
            courseModel = CourseModel()
        }

        if(!::studentModel.isInitialized) {
            studentModel = StudentModel()
        }

        val mainViewFragment = MainScreenFragment(courseModel, studentModel)
        val transaction = this.supportFragmentManager.beginTransaction()

        transaction.add(R.id._main_screen_fgc, mainViewFragment)
        transaction.commit()
    }
}
