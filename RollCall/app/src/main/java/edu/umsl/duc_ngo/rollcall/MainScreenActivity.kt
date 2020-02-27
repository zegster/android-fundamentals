package edu.umsl.duc_ngo.rollcall

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainScreenActivity : AppCompatActivity() {
    lateinit var courseModel: CourseModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_screen)

        //If it null, create a new instance. Model will never be null because we did lateinit
        //Note: this is an Elvis Operator
        //courseModel = ModelHolder.instance.get(CourseModel::class) ?: CourseModel()
        courseModel = CourseModel()

        val mainViewFragment = MainScreenFragment(courseModel)
        val transaction = this.supportFragmentManager.beginTransaction()

        transaction.add(R.id._fragment_container, mainViewFragment)
        transaction.commit()

        //LinearLayoutManager : implementation that provides similar functionality to ListView [DEPRECATED]

//        _recycler_view_main.layoutManager = LinearLayoutManager(this)
//        _recycler_view_main.adapter = CourseAdapter(model)
    }
}