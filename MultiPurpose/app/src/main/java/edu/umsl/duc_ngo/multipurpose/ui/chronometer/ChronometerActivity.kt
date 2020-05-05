package edu.umsl.duc_ngo.multipurpose.ui.chronometer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import edu.umsl.duc_ngo.multipurpose.R

class ChronometerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base_activity)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().add(R.id._base_activity, ChronometerFragment.newInstance()).commit()
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 0) {
            super.onBackPressed()
        } else {
            supportFragmentManager.popBackStack()
        }
    }
}