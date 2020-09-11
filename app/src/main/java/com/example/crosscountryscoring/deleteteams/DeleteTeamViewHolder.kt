package com.example.crosscountryscoring.deleteteams

import androidx.recyclerview.widget.RecyclerView
import com.example.crosscountryscoring.database.Team
import com.example.crosscountryscoring.databinding.DeleteTeamViewBinding

interface TeamDeleteStateChangeListener {
    fun teamDeleteStateChanged(position: Int, newState: DeleteState)
}

class DeleteTeamViewHolder(private var binding: DeleteTeamViewBinding,
    private val teamStateChangedListener: TeamDeleteStateChangeListener) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(teamInfo : Pair<Team, DeleteState>) {
        binding.team = teamInfo.first
        binding.delete = teamInfo.second

        binding.deleteTeamCheckbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                teamStateChangedListener.teamDeleteStateChanged(adapterPosition, DeleteState.DELETE)
            }
            else {
                teamStateChangedListener.teamDeleteStateChanged(adapterPosition, DeleteState.KEEP)
            }
        }

        binding.executePendingBindings()
    }
}