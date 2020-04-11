package edu.umsl.duc_ngo.shoppinglist.ui.list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.umsl.duc_ngo.shoppinglist.R

class ListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_activity)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id._list_activity, ListFragment.newInstance())
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
