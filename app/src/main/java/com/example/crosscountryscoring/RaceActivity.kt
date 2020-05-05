package com.example.crosscountryscoring

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.crosscountryscoring.databinding.ActivityRaceBinding

class RaceActivity : AppCompatActivity(), TeamFinisherFragment.OnTeamFinisherClickedListener {

    private lateinit var binding : ActivityRaceBinding
    private lateinit var viewModel : RaceActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRaceBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        setContentView(binding.root)

        viewModel = ViewModelProviders.of(this).get(RaceActivityViewModel::class.java)
        binding.viewModel = viewModel
    }

    override fun onTeamFinisherClicked() {
        viewModel.runnerFinished()
    }

}
