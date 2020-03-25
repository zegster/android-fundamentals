package edu.umsl.duc_ngo.simonsays.ui.game

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.difficulty_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        _easy_mode_btn.setOnClickListener {
            val intent = GameFragment.newIntentInit(activity, 0)
            startActivity(intent)
        }

        _medium_mode_btn.setOnClickListener {
            val intent = GameFragment.newIntentInit(activity, 1)
            startActivity(intent)
        }

        _hard_mode_btn.setOnClickListener {
            val intent = GameFragment.newIntentInit(activity, 2)
            startActivity(intent)
        }
    }
}
