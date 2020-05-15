package com.example.crosscountryscoring

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.crosscountryscoring.databinding.TeamViewBinding

class RaceRecyclerViewAdapter(private val myDataset: List<TeamViewModel>, private val raceRunnerFinished: OnTeamClickedListener) :
    RecyclerView.Adapter<TeamViewHolder>() {

    interface OnTeamClickedListener {
        fun onTeamClicked(teamViewModel: TeamViewModel)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): TeamViewHolder {
        // create a new view
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val teamViewBinding: TeamViewBinding = TeamViewBinding.inflate(layoutInflater, parent, false)
//        val teamButton = LayoutInflater.from(parent.context)
//            .inflate(R.layout.team_view, parent, false) as View
        return TeamViewHolder(teamViewBinding, raceRunnerFinished)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: TeamViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.bind(myDataset[position])
//        holder.name.text = myDataset[position].name
//        holder.team = myDataset[position]
//        holder.name.setOnClickListener(raceRunnerFinished)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size

}