package edu.umsl.duc_ngo.rollcall.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import edu.umsl.duc_ngo.rollcall.R

class MainScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_screen)

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
