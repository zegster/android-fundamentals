package edu.umsl.duc_ngo.simonsays.ui.game

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import edu.umsl.duc_ngo.simonsays.R
import edu.umsl.duc_ngo.simonsays.ui.BaseFragment
import kotlinx.android.synthetic.main.difficulty_fragment.*

private const val TAG = "DifficultyFragment"
class DifficultyFragment : BaseFragment() {
    companion object {
        fun newInstance() = DifficultyFragment()
    }

    private var mediaPlayer: MediaPlayer? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.difficulty_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        _easy_mode_btn.setOnClickListener {
            playStartSound()
            val intent = GameFragment.newIntentInit(activity, 0)
            startActivity(intent)
        }

        _medium_mode_btn.setOnClickListener {
            playStartSound()
            val intent = GameFragment.newIntentInit(activity, 1)
            startActivity(intent)
        }

        _hard_mode_btn.setOnClickListener {
            playStartSound()
            val intent = GameFragment.newIntentInit(activity, 2)
            startActivity(intent)
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
