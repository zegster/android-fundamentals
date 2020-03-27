package edu.umsl.duc_ngo.simonsays.ui.main

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import edu.umsl.duc_ngo.simonsays.R
import edu.umsl.duc_ngo.simonsays.ui.BaseFragment
import edu.umsl.duc_ngo.simonsays.ui.game.DifficultyFragment
import edu.umsl.duc_ngo.simonsays.ui.scoreboard.ScoreboardFragment
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.main_fragment.*
import kotlin.system.exitProcess

private const val TAG = "MainFragment"
class MainFragment : BaseFragment() {
    companion object {
        fun newInstance() = MainFragment()
    }

    private var mediaPlayer: MediaPlayer? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        /* Starting the GameActivity */
        _start_game_btn.setOnClickListener {
            playStartSound()
            parentFragmentManager.beginTransaction()
                .replace(R.id._main_activity, DifficultyFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }

        /* View scoreboard screen */
        _high_score_btn.setOnClickListener {
            playStartSound()
            val intent = ScoreboardFragment.newIntentInit(activity)
            startActivity(intent)
        }

        /* Exit Game */
        _quit_game_btn.setOnClickListener {
            _start_game_btn.isEnabled = false
            _high_score_btn.isEnabled = false
            playStartSound()
            context?.let {
                Toasty.info(it,"See You Next Time", Toast.LENGTH_SHORT, false).show();
            }
            Handler().postDelayed({exitProcess(0)}, 2000)
        }
    }

    override fun onDetach() {
        mediaPlayer?.release()
        super.onDetach()
    }

    override fun onDestroy() {
        mediaPlayer?.release()
        super.onDestroy()
    }

    private fun playStartSound() {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(context, R.raw.start_sound)
        mediaPlayer?.setVolume(1.0f, 1.0f)
        mediaPlayer?.start()
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
