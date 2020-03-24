package edu.umsl.duc_ngo.simonsays.ui.game

import android.animation.*
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import edu.umsl.duc_ngo.simonsays.R
import edu.umsl.duc_ngo.simonsays.data.PlayerData
import edu.umsl.duc_ngo.simonsays.data.SimonDatabase
import edu.umsl.duc_ngo.simonsays.ui.BaseFragment
import edu.umsl.duc_ngo.simonsays.ui.scoreboard.ScoreboardFragment
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
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.game_fragment, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        enabledButton(false)
        val difficulty = intent.getIntExtra(DIFFICULTY, 0)
        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)
        viewModel.initSequence(difficulty)

        //Keep track of score change
        viewModel.getScore().observe(viewLifecycleOwner, Observer {
            _current_score.text = it.toString()
        })

        //Keep track of simon sequence change
        viewModel.getSequence().observe(viewLifecycleOwner, Observer {sequence ->
            if(viewModel.isUpdating() == true) {
                Log.e(TAG, sequence.toString())

                val animationSequence = mutableListOf<ValueAnimator>()

                activity?.let {activity ->
                    for((index, seq) in sequence.withIndex()) {
                        val view = when(seq) {
                            0 -> _red_btn
                            1 -> _yellow_btn
                            2 -> _green_btn
                            3 -> _blue_btn
                            else -> _red_btn
                        }

                        val originalColor = view.background as? ColorDrawable
                        val targetColor = ContextCompat.getColor(activity, R.color.colorTarget)

                        val animator = ValueAnimator.ofObject(
                            ArgbEvaluator(),
                            originalColor?.color, targetColor, originalColor?.color
                        )

                        animator.addUpdateListener {valueAnimator ->
                            (valueAnimator.animatedValue as? Int)?.let {animatedValue ->
                                view.setBackgroundColor(animatedValue)
                            }
                        }

                        animator?.startDelay = ( 100 + ((index + 1) * 100) ).toLong()
                        animator?.duration = 1000.toLong()
                        animationSequence.add(animator)
                    }
                }

                val animatorSet = AnimatorSet()
                animatorSet.playSequentially(animationSequence as List<Animator>?)
                animatorSet.start()

                handler = Handler()
                runnable = Runnable {
                    enabledButton(true)
                    viewModel.startTime()
                }
                handler.postDelayed(runnable, animatorSet.totalDuration)
            }
        })

        //Keep track if the game is over
        viewModel.isGameOver().observe(viewLifecycleOwner, Observer {isGameOver ->
            if(isGameOver == true) {
                launch {
                    val playerData = PlayerData(viewModel.getScore().value!!)
                    context?.let {
                        SimonDatabase(it).getPlayerDataDao().addPlayer(playerData)

                        Toasty.Config.getInstance().allowQueue(true).apply()
                        Toasty.error(it, "GAME OVER!", Toast.LENGTH_SHORT, true).show();
                        Toasty.info(it, "Next sequence was: " + viewModel.lastSequence(), Toast.LENGTH_LONG, true).show();
                        Toasty.info(it, "Score Registered: " + viewModel.getScore().value!!, Toast.LENGTH_SHORT, true).show();
                        parentFragmentManager.beginTransaction()
                            .replace(R.id._game_activity, ScoreboardFragment.newInstance())
                            .commit()
                    }
                }
            }
        })

        //Update Timer
        viewModel.getTime().observe(viewLifecycleOwner, Observer {
            _timer_label.text = it.toString()
        })

        //Red : 0, Yellow : 1, Green : 2, Blue : 3
        context?.let {
            _red_btn.setOnClickListener {
                viewModel.resetTime()
                viewModel.checkSequence(0)
            }

            _yellow_btn.setOnClickListener {
                viewModel.resetTime()
                viewModel.checkSequence(1)
            }

            _green_btn.setOnClickListener {
                viewModel.resetTime()
                viewModel.checkSequence(2)
            }

            _blue_btn.setOnClickListener {
                viewModel.resetTime()
                viewModel.checkSequence(3)
            }
        }
    }

    override fun onDestroy() {
        handler.removeCallbacks(runnable)
        super.onDestroy()
    }

    private fun enabledButton(enable: Boolean) {
        _red_btn.isEnabled = enable
        _yellow_btn.isEnabled = enable
        _green_btn.isEnabled = enable
        _blue_btn.isEnabled = enable
    }
}
