package com.example.crosscountryscoring

import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.crosscountryscoring.databinding.TeamViewBinding

// Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder.
class TeamViewHolder(teamViewBinding: TeamViewBinding,
                     private val raceRunnerFinished: RaceRecyclerViewAdapter.OnTeamClickedListener) :
        RecyclerView.ViewHolder(teamViewBinding.root), View.OnClickListener {

    private var binding: TeamViewBinding = teamViewBinding
    private lateinit var teamViewModel: TeamViewModel
    var teamBtn : Button = teamViewBinding.teamName

    init {
        teamBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (binding.viewModel != null) {
            raceRunnerFinished.onTeamClicked(teamViewModel)
        }
        binding.invalidateAll()
    }

    fun bind(teamViewModel: TeamViewModel) {
        binding.viewModel = teamViewModel
        this.teamViewModel = teamViewModel
        binding.executePendingBindings()
    }

}