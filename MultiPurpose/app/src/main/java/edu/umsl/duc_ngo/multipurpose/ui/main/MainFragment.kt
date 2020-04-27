package edu.umsl.duc_ngo.multipurpose.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import edu.umsl.duc_ngo.multipurpose.R
import edu.umsl.duc_ngo.multipurpose.ui.BaseFragment

class MainFragment : BaseFragment() {
    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

//        /* Starting the GameActivity */
//        _start_game_btn.setOnClickListener {
//            playStartSound()
//            parentFragmentManager.beginTransaction()
//                .replace(R.id._main_activity, DifficultyFragment.newInstance())
//                .addToBackStack(null)
//                .commit()
//        }
//
//        /* View scoreboard screen */
//        _high_score_btn.setOnClickListener {
//            playStartSound()
//            val intent = ScoreboardFragment.newIntentInit(activity)
//            startActivity(intent)
//        }
//
//        /* Exit Game */
//        _quit_game_btn.setOnClickListener {
//            _start_game_btn.isEnabled = false
//            _high_score_btn.isEnabled = false
//            playStartSound()
//            context?.let {
//                Toasty.info(it,"See You Next Time", Toast.LENGTH_SHORT, false).show();
//            }
//            Handler().postDelayed({exitProcess(0)}, 2000)
//        }

    }
}