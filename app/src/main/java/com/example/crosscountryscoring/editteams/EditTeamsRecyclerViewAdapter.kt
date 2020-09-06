package com.example.crosscountryscoring.editteams

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.crosscountryscoring.database.Team
import com.example.crosscountryscoring.TeamViewModel
import com.example.crosscountryscoring.databinding.EditTeamViewBinding

enum class ChangeState {
    EXISTED, ADDED
}

/*
 * RecyclerViewAdapter used for modifying the teams of a race. For simplicity, this class takes a
 * snapshot of the current teams as a MutableList. The user is then free to make changes and save
 * the changes to the database when finished.
 */
class EditTeamsRecyclerViewAdapter(private val databaseTeams: LiveData<List<TeamViewModel>>)
    : RecyclerView.Adapter<EditTeamViewHolder>() {

    private var myTeams: MutableList<Pair<Team, ChangeState>> = databaseTeams.value?.map {
        Pair(it.teamWithRunners.team, ChangeState.EXISTED)
    }?.toMutableList() ?: mutableListOf()

    fun addTeam() {
        val team = Team("[Team Name]")
        myTeams.add(Pair(
            team,
            ChangeState.ADDED
        ))
        notifyItemInserted(myTeams.size)
    }

    fun onDatasetChange() {
        myTeams = databaseTeams.value?.map {
            Pair(it.teamWithRunners.team, ChangeState.EXISTED)
        }?.toMutableList() ?: mutableListOf()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditTeamViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val teamEditViewBinding: EditTeamViewBinding = EditTeamViewBinding.inflate(layoutInflater, parent, false)
        return EditTeamViewHolder(teamEditViewBinding)
    }

    override fun getItemCount(): Int {
        return myTeams.size
    }

    fun getTeams(): List<Pair<Team, ChangeState>> {
        return myTeams.toList()
    }

    override fun onBindViewHolder(holder: EditTeamViewHolder, position: Int) {
        holder.bind(myTeams[position])
    }
}