package edu.umsl.duc_ngo.simonsays.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import edu.umsl.duc_ngo.simonsays.R
import edu.umsl.duc_ngo.simonsays.data.PlayerData
import edu.umsl.duc_ngo.simonsays.data.SimonDatabase
import edu.umsl.duc_ngo.simonsays.ui.BaseFragment
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.coroutines.launch

private const val TAG = "MainFragment"
class MainFragment : BaseFragment() {
    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        _start_game_btn.setOnClickListener {
            //Simplified version of async database operation with the help of Kotlin Coroutines
            launch {
                val playerData = PlayerData(99)
                context?.let {
                    SimonDatabase(it).getPlayerDataDao().addPlayer(playerData)
                    Toasty.info(it, "New Player Score Registered!", Toast.LENGTH_SHORT, true).show();
                }
            }
        }
    }

    //You can't access the database in the main thread, it will crash if you do so.
    //Whenever we want to do database operation, we want to do it asynchronously.
    //Room doesn't allow us to perform the database operation in main thread because it can take a long time.
    //We can use Kotlin Coroutines to reduce/minimize the async lengthy process/code (old references below)
    //Function Call: savePlayerData(playerData)
    /*
    private fun savePlayerData(playerData: PlayerData) {
        class SavePlayerData : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg params: Void?): Void? {
                SimonDatabase(activity!!).getPlayerDataDao().addPlayer(playerData)
                return null
            }

            //Once doInBackground is finish, execute this function
            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)
                Toasty.info(activity!!.applicationContext, "New Player Score Registered!", Toast.LENGTH_SHORT, true).show();
            }
        }
        SavePlayerData().execute()
    }
    */



//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        initializeUi()
//    }

//    private lateinit var viewModel: MainViewModel
//    private fun initializeUi() {
//        val factory = InjectorUtils.provideMainViewModelFactory()
//        var currentScore = 0
//
//        viewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)
//        viewModel.getPlayer().observe(viewLifecycleOwner, Observer {
//            Log.i(TAG, it.score.toString())
//            _score_txt.text = it.score.toString()
//            currentScore = it.score
//        })
//
//        _start_game_btn.setOnClickListener {
//            val playerData = PlayerData(++currentScore)
//            viewModel.updatePlayer(playerData)
//            _score_txt.text = currentScore.toString()
//        }
//    }
}
