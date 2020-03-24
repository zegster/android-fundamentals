package edu.umsl.duc_ngo.simonsays.ui.game

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.umsl.duc_ngo.simonsays.R

class GameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_activity)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.container, GameFragment.newInstance())
                .commit()
        }
    }
}
