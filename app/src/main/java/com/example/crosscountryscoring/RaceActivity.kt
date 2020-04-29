package com.example.crosscountryscoring

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.crosscountryscoring.databinding.ActivityRaceBinding

class RaceActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRaceBinding

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_race)
        binding = ActivityRaceBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewManager = LinearLayoutManager(this)
        val fakeTeams = arrayOf("Penn", "Riley", "Mishawaka", "Clay", "Washington", "Warsaw", "Kokomo")
        viewAdapter = TeamFinisherAdapter(fakeTeams)

        recyclerView = binding.teamFinisherRecyclerView.apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter
        }

    }

}
