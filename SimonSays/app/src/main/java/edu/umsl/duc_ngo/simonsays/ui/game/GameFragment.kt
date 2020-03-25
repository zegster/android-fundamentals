package edu.umsl.duc_ngo.simonsays.ui.game

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
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
    private var animatorSet: AnimatorSet? = null
    private var delayHandler: Handler? = null
    private var delayRunnable: Runnable? = null
    private var animationHandler: Handler? = null
    private var animationRunnable: Runnable? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)
        return inflater.inflate(R.layout.game_fragment, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //Only create a new sequence when the game haven't start yet
        val difficulty = intent.getIntExtra(DIFFICULTY, 0)
        viewModel.initSequence(difficulty)

        //Keep track of score change
        viewModel.getScore().observe(viewLifecycleOwner, Observer {
            _current_score.text = it.toString()
        })

        //Keep track of simon sequence change
        enabledButton(false)
        viewModel.isFinishUpdate().observe(viewLifecycleOwner, Observer {isFinishUpdate ->
            if(isFinishUpdate == true && viewModel.isGameOver().value == false) {
                viewModel.stopTime()
                enabledButton(false)
                _who_turn_label.setText(R.string.ready)

                //Create delay handler and animation sequence
                val animationSequence = mutableListOf<Animator>()
                delayHandler = Handler()
                delayRunnable = Runnable {
                    _who_turn_label.setText(R.string.simon_turn)
                    activity?.let {
                        for((index, seq) in viewModel.getSequence().value?.withIndex()!!) {
                            val view = when(seq) {
                                0 -> _red_btn
                                1 -> _yellow_btn
                                2 -> _green_btn
                                3 -> _blue_btn
                                else -> _red_btn
                            }

                            val objectAnimator = ObjectAnimator.ofFloat(view, "alpha", 0.25f, 1.0f)
                            objectAnimator.target = view
                            objectAnimator.startDelay = ( index * 100 ).toLong()
                            objectAnimator.duration = ( 1000 - (200 * difficulty) ).toLong()
                            objectAnimator.addListener(object : Animator.AnimatorListener {
                                override fun onAnimationStart(animation: Animator?) {}
                                override fun onAnimationEnd(animation: Animator?) {
                                    viewModel.updateSequenceLeft()
                                }
                                override fun onAnimationCancel(animation: Animator?) {}
                                override fun onAnimationRepeat(animation: Animator?) {}

                            })
                            animationSequence.add(objectAnimator)
                        }
                    }

                    //Update animation sequence
                    animatorSet = AnimatorSet()
                    animatorSet?.playSequentially(animationSequence)
                    animatorSet?.start()

                    //Create animation handler, turn on button when animation is done
                    animationHandler = Handler()
                    animationRunnable = Runnable {
                        enabledButton(true)
                        viewModel.startTime()
                        _who_turn_label.setText(R.string.player_turn)
                    }
                    animationHandler?.postDelayed(animationRunnable, animatorSet?.totalDuration!!)
                }
                delayHandler?.postDelayed(delayRunnable,3000)
            }
        })

        //Keep track if the game is over
        viewModel.isGameOver().observe(viewLifecycleOwner, Observer {isGameOver ->
            if(isGameOver == true && viewModel.isScoreRegister().value == false) {
                viewModel.stopTime()
                viewModel.gameFinish()
                enabledButton(false)
                _who_turn_label.setText(R.string.miss)

                launch {
                    val playerData = PlayerData(viewModel.getScore().value!!)
                    context?.let {
                        SimonDatabase(it).getPlayerDataDao().addPlayer(playerData)

                        Toasty.Config.getInstance().allowQueue(true).apply()
                        Toasty.error(it, "GAME OVER!", Toast.LENGTH_SHORT, true).show();
                        Toasty.info(it, "Next sequence was: " + viewModel.lastSequence(), Toast.LENGTH_LONG, true).show();
                        Toasty.info(it, "Score Registered: " + viewModel.getScore().value!!, Toast.LENGTH_SHORT, true).show();
                        parentFragmentManager.beginTransaction()
                            .add(R.id._game_activity, GameoverFragment.newInstance())
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
        animatorSet?.pause()
        animatorSet?.removeAllListeners()
        animatorSet = null
        delayHandler?.removeCallbacks(delayRunnable)
        animationHandler?.removeCallbacks(animationRunnable)
        super.onDestroy()
    }

    private fun enabledButton(enable: Boolean) {
        _red_btn.isEnabled = enable
        _yellow_btn.isEnabled = enable
        _green_btn.isEnabled = enable
        _blue_btn.isEnabled = enable
    }
}
