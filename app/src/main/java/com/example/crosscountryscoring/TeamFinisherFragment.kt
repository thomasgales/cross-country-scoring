package com.example.crosscountryscoring

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.crosscountryscoring.databinding.TeamFinisherFragmentBinding


class TeamFinisherFragment : Fragment() {

    companion object {
        fun newInstance() = TeamFinisherFragment()
    }

    private lateinit var viewModel: TeamFinisherViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private var _binding: TeamFinisherFragmentBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = TeamFinisherFragmentBinding.inflate(inflater, container, false)

        viewModel = ViewModelProviders.of(this).get(TeamFinisherViewModel::class.java)

        viewManager = LinearLayoutManager(activity)
        viewAdapter = TeamFinisherAdapter(viewModel.fakeTeams)

        recyclerView = binding.teamFinisherRecyclerView.apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            adapter = viewAdapter
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
