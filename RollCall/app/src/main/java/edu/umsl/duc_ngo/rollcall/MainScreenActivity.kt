package edu.umsl.duc_ngo.rollcall

import android.os.Bundle
import android.util.Log
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class MainScreenActivity : AppCompatActivity() {
    private var model: CourseModel = CourseModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_screen)

        val listView = findViewById<ListView>(R.id._course_list)
        listView.adapter = CourseListAdapter(model)
    }
}