package edu.umsl.duc_ngo.shoppinglist.ui.item

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import edu.umsl.duc_ngo.shoppinglist.R

class ItemActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_activity)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id._item_activity, ItemFragment.newInstance())
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