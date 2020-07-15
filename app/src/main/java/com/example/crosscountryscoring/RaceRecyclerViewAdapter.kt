package com.example.crosscountryscoring

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.crosscountryscoring.databinding.TeamViewBinding

class RaceRecyclerViewAdapter(private val myTeams: LiveData<List<TeamViewModel>>,
                              private val raceRunnerFinished: OnTeamClickedListener,
                              private val lifecycleOwner: LifecycleOwner) :
    RecyclerView.Adapter<TeamViewHolder>() {

    interface OnTeamClickedListener {
        fun onTeamClicked(teamViewModel: ITeamViewModel)
    }

    fun onDatasetChange() {
        notifyDataSetChanged()
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): TeamViewHolder {
        // create a new view
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val teamViewBinding: TeamViewBinding = TeamViewBinding.inflate(layoutInflater, parent, false)
        teamViewBinding.lifecycleOwner = lifecycleOwner
        return TeamViewHolder(teamViewBinding, raceRunnerFinished)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: TeamViewHolder, position: Int) {
        myTeams.value?.let {
            holder.bind(it[position])
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myTeams.value?.size ?: 0

}