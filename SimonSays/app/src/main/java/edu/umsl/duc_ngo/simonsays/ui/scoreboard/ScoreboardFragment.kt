package edu.umsl.duc_ngo.simonsays.ui.scoreboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.umsl.duc_ngo.simonsays.R
import edu.umsl.duc_ngo.simonsays.data.PlayerData
import edu.umsl.duc_ngo.simonsays.data.SimonDatabase
import edu.umsl.duc_ngo.simonsays.ui.BaseFragment
import kotlinx.android.synthetic.main.scoreboard_fragment.*
import kotlinx.android.synthetic.main.scoreboard_layout.view.*
import kotlinx.coroutines.launch

private const val TAG = "ScoreboardFragment"
class ScoreboardFragment : BaseFragment() {
    companion object {
        fun newInstance() = ScoreboardFragment()

        lateinit var intent: Intent

        @JvmStatic
        fun newIntentInit(context: FragmentActivity?): Intent {
            val intent = Intent(context, ScoreboardActivity::class.java)
            Companion.intent = intent
            return intent
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.scoreboard_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        _scoreboard_rv.layoutManager = LinearLayoutManager(activity)
        _scoreboard_rv.setHasFixedSize(true) //Improve the performance by not changing it size.

        //Init viewModel to avoid no adapter attached error
        val playerList = listOf(PlayerData(0))
        _scoreboard_rv.adapter = ScoreboardAdapter(playerList)

        //Once database is retrieve, update the list with new data
        launch {
            context?.let {
                val allPlayerData = SimonDatabase(it).getPlayerDataDao().getAllPlayers()
                _scoreboard_rv.adapter = ScoreboardAdapter(allPlayerData)
            }
        }
    }

    inner class ScoreboardAdapter(private val playerList: List<PlayerData>): RecyclerView.Adapter<ScoreboardAdapter.ScoreboardViewHolder>() {
        /* Called when RecyclerView needs a new ViewHolder of the given type to represent an item. */
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreboardViewHolder {
            return ScoreboardViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.scoreboard_layout, parent, false)
            )
        }

        /* Return number of items to render */
        override fun getItemCount() = playerList.size

        /* Called by RecyclerView to display the data at the specified position. */
        override fun onBindViewHolder(holder: ScoreboardViewHolder, position: Int) {
            holder.view._player_id_label.text = playerList[position].id.toString()
            holder.view._player_score.text = playerList[position].score.toString()
        }

        /* Describes an item view and metadata about its place within the RecyclerView. */
        inner class ScoreboardViewHolder(val view: View): RecyclerView.ViewHolder(view)
    }
}
