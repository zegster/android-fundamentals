package edu.umsl.duc_ngo.simonsays.ui.game

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import edu.umsl.duc_ngo.simonsays.R
import edu.umsl.duc_ngo.simonsays.data.PlayerData
import edu.umsl.duc_ngo.simonsays.data.SimonDatabase
import edu.umsl.duc_ngo.simonsays.ui.BaseFragment
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.game_fragment.*
import kotlinx.coroutines.launch

private const val TAG = "GameFragment"
class GameFragment : BaseFragment() {
    companion object {
        fun newInstance() = GameFragment()
        private const val DIFFICULTY = "edu.umsl.duc_ngo.difficulty"

        lateinit var intent: Intent

        @JvmStatic
        fun newIntentInit(context: FragmentActivity?, difficulty: Int): Intent {
            val intent = Intent(context, GameActivity::class.java)
            intent.putExtra(DIFFICULTY, difficulty)
            Companion.intent = intent
            return intent
        }
    }

    private lateinit var viewModel: GameViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.game_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //Add score to database
//        launch {
//            val playerData = PlayerData(99)
//            context?.let {
//                SimonDatabase(it).getPlayerDataDao().addPlayer(playerData)
//                Toasty.info(it, "New Player Score Registered!", Toast.LENGTH_SHORT, true).show();
//            }
//        }
        val difficulty = intent.getIntExtra(DIFFICULTY, 0)
        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)
        viewModel.generateRandomSequence(difficulty)

        //Keep track of score change
        viewModel.currentScore.observe(viewLifecycleOwner, Observer {
            Log.i(TAG, it.toString())
            _current_score.text = it.toString()
        })

        //Keep track of simon sequence change
        viewModel.simonSequence.observe(viewLifecycleOwner, Observer {
            Log.i(TAG, "Size: " + it.size.toString())
            Log.i(TAG, "Sequence: $it")
        })

        //Red : 0, Yellow : 1, Green : 2, Blue : 3
        _red_btn.setOnClickListener {
            viewModel.gamePlay(0)
        }

        _yellow_btn.setOnClickListener {
            viewModel.gamePlay(1)
        }

        _green_btn.setOnClickListener {
            viewModel.gamePlay(2)
        }

        _blue_btn.setOnClickListener {
            viewModel.gamePlay(3)
        }

    }
}
