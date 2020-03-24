package edu.umsl.duc_ngo.simonsays.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import edu.umsl.duc_ngo.simonsays.R
import edu.umsl.duc_ngo.simonsays.ui.BaseFragment
import edu.umsl.duc_ngo.simonsays.ui.game.DifficultyFragment
import edu.umsl.duc_ngo.simonsays.ui.scoreboard.ScoreboardFragment
import kotlinx.android.synthetic.main.main_fragment.*

private const val TAG = "MainFragment"
class MainFragment : BaseFragment() {
    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        /* Starting the GameActivity */
        _start_game_btn.setOnClickListener {
            if (savedInstanceState == null) {
                parentFragmentManager.beginTransaction()
                    .replace(R.id._main_activity, DifficultyFragment.newInstance())
                    .addToBackStack(null)
                    .commit()
            }
        }

        /* View scoreboard screen */
        _high_score_btn.setOnClickListener {
            val intent = ScoreboardFragment.newIntentInit(activity)
            startActivity(intent)
        }
    }


    /* You can't access the database in the main thread, it will crash if you do so.
        Whenever we want to do database operation, we want to do it asynchronously.
        Room doesn't allow us to perform the database operation in main thread because it can take a long time.
        We can use Kotlin Coroutines to reduce/minimize the async lengthy process/code (old references below).
        In other words, we can use simplified version of async database operation with the help of Kotlin Coroutines (using launch).
        Function Call: savePlayerData(playerData) */
    /*
    private fun savePlayerData(playerData: PlayerData) {
        class SavePlayerData : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg params: Void?): Void? {
                SimonDatabase(activity!!).getPlayerDataDao().addPlayer(playerData)
                return null
            }

            //Once doInBackground is finish, execute this function
            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)
                Toasty.info(activity!!.applicationContext, "New Player Score Registered!", Toast.LENGTH_SHORT, true).show();
            }
        }
        SavePlayerData().execute()
    }
    */
}
