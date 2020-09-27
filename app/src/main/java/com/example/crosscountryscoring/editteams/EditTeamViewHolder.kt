package com.example.crosscountryscoring.editteams

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.RecyclerView
import com.example.crosscountryscoring.database.Team
import com.example.crosscountryscoring.databinding.EditTeamViewBinding

class EditTeamViewHolder(private var binding: EditTeamViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

    fun bind(teamInfo : Pair<Team, ChangeState>) {
        binding.team = teamInfo.first
        binding.executePendingBindings()
    }

    fun selectText() {
        binding.teamNameEditBox.requestFocus()
        binding.teamNameEditBox.setSelection(0, binding.teamNameEditBox.text.length)
    }

}