package edu.umsl.duc_ngo.simonsays.ui.scoreboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.umsl.duc_ngo.simonsays.R

class ScoreboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scoreboard_activity)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id._scoreboard_fragment, ScoreboardFragment.newInstance())
                .commitNow()
        }
    }
}
