package edu.umsl.duc_ngo.shoppinglist.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.umsl.duc_ngo.shoppinglist.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id._main_activity, MainFragment.newInstance())
                .commit()
        }
    }

    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount == 0) {
            super.onBackPressed()
        }
        else {
            supportFragmentManager.popBackStack()
        }
    }
}
