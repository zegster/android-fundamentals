package edu.umsl.duc_ngo.simonsays.ui.scoreboard

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import edu.umsl.duc_ngo.simonsays.R

private const val TAG = "ScoreboardFragment"
class ScoreboardFragment : Fragment() {
    companion object {
        fun newInstance() = ScoreboardFragment()
    }

//    private lateinit var viewModel: ScoreboardViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.scoreboard_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}
