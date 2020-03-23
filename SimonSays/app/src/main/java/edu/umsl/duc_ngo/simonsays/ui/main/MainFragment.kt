package edu.umsl.duc_ngo.simonsays.ui.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import edu.umsl.duc_ngo.simonsays.R
import edu.umsl.duc_ngo.simonsays.data.PlayerData
import edu.umsl.duc_ngo.simonsays.utilities.InjectorUtils
import kotlinx.android.synthetic.main.main_fragment.*

private const val TAG = "MainFragment"
class MainFragment : Fragment() {
    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeUi()
    }

    private fun initializeUi() {
        val factory = InjectorUtils.provideMainViewModelFactory()
        var currentScore = 0

        viewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)
        viewModel.getPlayer().observe(viewLifecycleOwner, Observer {
            Log.i(TAG, it.score.toString())
            _score_txt.text = it.score.toString()
            currentScore = it.score
        })

        _start_game_btn.setOnClickListener {
            val playerData = PlayerData(++currentScore)
            viewModel.updatePlayer(playerData)
            _score_txt.text = currentScore.toString()
        }
    }
}
