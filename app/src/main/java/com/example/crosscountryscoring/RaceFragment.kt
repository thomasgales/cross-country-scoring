package com.example.crosscountryscoring

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.crosscountryscoring.databinding.FragmentRaceBinding
import kotlinx.android.synthetic.main.activity_main.*


class RaceFragment : Fragment() {

    private lateinit var viewModel: RaceViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RaceRecyclerViewAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewModelFactory: RaceViewModelFactory
    private val sharedVm: SharedTeamsViewModel by activityViewModels()

    private var _binding: FragmentRaceBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentRaceBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        viewModelFactory = RaceViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory).get(RaceViewModel::class.java)
        binding.viewModel = viewModel

        viewManager = LinearLayoutManager(activity)
        viewAdapter = RaceRecyclerViewAdapter(sharedVm.teams, viewModel, this)
        sharedVm.teams.observe(viewLifecycleOwner, Observer {
            viewAdapter.onDatasetChange()
        })

        recyclerView = binding.raceRecyclerView.apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            adapter = viewAdapter
        }

        // Indicate that this fragment would like to add items to Options Menu
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Item in top bar selected. Act on it!
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.edit_race_button -> {
                val action = RaceFragmentDirections.actionRaceFragmentToEditTeamsFragment()
                NavHostFragment.findNavController(nav_host_fragment).navigate(action)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
