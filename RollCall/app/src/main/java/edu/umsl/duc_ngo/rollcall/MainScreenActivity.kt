package edu.umsl.duc_ngo.rollcall

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.main_screen.*

class MainScreenActivity : AppCompatActivity() {
    private var model: CourseModel = CourseModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_screen)

        //LinearLayoutManager : implementation that provides similar functionality to ListView [DEPRECATED]
        //Adapter is a data source or a UI table view delegate to a list (which it helps rendering out the items inside of a list)
        _recycler_view_main.layoutManager = LinearLayoutManager(this)
        _recycler_view_main.adapter = CourseAdapter(model)
    }
}