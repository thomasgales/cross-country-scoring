package com.example.crosscountryscoring.deleteteams

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.crosscountryscoring.R
import com.example.crosscountryscoring.TeamViewModel
import com.example.crosscountryscoring.database.Team
import com.example.crosscountryscoring.databinding.DeleteTeamViewBinding
import kotlinx.coroutines.withContext

enum class DeleteState {
    KEEP, DELETE
}

class DeleteTeamsRecyclerViewAdapter(private val context: Context,
                                     private val databaseTeams: LiveData<List<TeamViewModel>>)
    : RecyclerView.Adapter<DeleteTeamViewHolder>(), TeamDeleteStateChangeListener {

    private var myTeams: MutableList<Pair<Team, DeleteState>> = databaseTeams.value?.map {
        Pair(it.team.value ?: Team(context.getString(R.string.invalid_team_name_placeholder)), DeleteState.KEEP)
    }?.toMutableList() ?: mutableListOf<Pair<Team, DeleteState>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeleteTeamViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val teamEditViewBinding: DeleteTeamViewBinding = DeleteTeamViewBinding.inflate(layoutInflater, parent, false)
        return DeleteTeamViewHolder(teamEditViewBinding, this)
    }

    override fun getItemCount(): Int {
        return myTeams.size
    }

    fun getTeams(): List<Pair<Team, DeleteState>> {
        return myTeams.toList()
    }

    override fun onBindViewHolder(holder: DeleteTeamViewHolder, position: Int) {
        holder.bind(myTeams[position])
    }

    fun onDatasetChange() {
        myTeams = databaseTeams.value?.map {
            Pair(it.team.value ?: Team(context.getString(R.string.invalid_team_name_placeholder)), DeleteState.KEEP)
        }?.toMutableList() ?: mutableListOf()
        notifyDataSetChanged()
    }

    override fun teamDeleteStateChanged(position: Int, newState: DeleteState) {
        myTeams[position] = Pair(myTeams[position].first, newState)
    }
}