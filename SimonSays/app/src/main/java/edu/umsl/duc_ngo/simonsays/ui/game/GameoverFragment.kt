package edu.umsl.duc_ngo.simonsays.ui.game

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import edu.umsl.duc_ngo.simonsays.R
import edu.umsl.duc_ngo.simonsays.ui.BaseFragment
import edu.umsl.duc_ngo.simonsays.ui.scoreboard.ScoreboardFragment
import kotlinx.android.synthetic.main.gameover_fragment.*

private const val TAG = "GameoverFragment"
class GameoverFragment: BaseFragment() {
    companion object {
        fun newInstance() = GameoverFragment()
    }

    private var mediaPlayer: MediaPlayer? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.gameover_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        _play_again_btn.setOnClickListener {
            playStartSound()
            Handler().postDelayed({
                parentFragmentManager.beginTransaction()
                    .replace(R.id._game_activity, GameFragment.newInstance())
                    .commit()
            }, 1000)
        }

        _high_score_btn.setOnClickListener {
            playStartSound()
            Handler().postDelayed({
                parentFragmentManager.beginTransaction()
                    .replace(R.id._game_activity, ScoreboardFragment.newInstance())
                    .commit()
            }, 1000)
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
}
