package edu.umsl.duc_ngo.simonsays.ui.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import edu.umsl.duc_ngo.simonsays.R
import edu.umsl.duc_ngo.simonsays.ui.BaseFragment
import edu.umsl.duc_ngo.simonsays.ui.scoreboard.ScoreboardFragment
import kotlinx.android.synthetic.main.gameover_fragment.*

private const val TAG = "GameOverFragment"
class GameoverFragment: BaseFragment() {
    companion object {
        fun newInstance() = GameoverFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.gameover_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        _play_again_btn.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id._game_activity, GameFragment.newInstance())
                .commit()
        }

        _high_score_btn.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id._game_activity, ScoreboardFragment.newInstance())
                .commit()
        }
    }
}