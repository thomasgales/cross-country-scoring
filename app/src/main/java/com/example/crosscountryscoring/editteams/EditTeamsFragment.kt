package com.example.crosscountryscoring.editteams

import android.os.Bundle
import android.view.*
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.crosscountryscoring.R
import com.example.crosscountryscoring.SharedTeamsViewModel
import com.example.crosscountryscoring.databinding.FragmentEditTeamsBinding
import kotlinx.android.synthetic.main.activity_main.*

class EditTeamsFragment : Fragment() {

    private lateinit var viewModel: EditTeamsViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: EditTeamsRecyclerViewAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private val sharedVm: SharedTeamsViewModel by activityViewModels()
    private lateinit var viewModelFactory: EditTeamsViewModelFactory

    private var _binding: FragmentEditTeamsBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentEditTeamsBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        viewModelFactory = EditTeamsViewModelFactory(sharedVm)
        viewModel = ViewModelProvider(this, viewModelFactory).get(EditTeamsViewModel::class.java)

        viewManager = LinearLayoutManager(activity)

        viewAdapter = EditTeamsRecyclerViewAdapter(sharedVm.teams)
        sharedVm.teams.observe(viewLifecycleOwner, Observer {
            viewAdapter.onDatasetChange()
        })

        recyclerView = binding.editTeamsRecyclerView.apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            adapter = viewAdapter
        }

        binding.addTeamButton.setOnClickListener {
            viewAdapter.addTeam()
        }

        // Indicate that this fragment would like to add items to Options Menu
        setHasOptionsMenu(true)

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            viewModel.verifyAndSaveChanges(viewAdapter.getTeams())
            findNavController().navigateUp()
        }
        callback.isEnabled = true

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.edit_teams_menu, menu)
    }

    // Button in top bar pressed. Act on it!
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                viewModel.verifyAndSaveChanges(viewAdapter.getTeams())
                findNavController().navigateUp()
                true
            }
            R.id.deleteTeamsButton -> {
                viewModel.verifyAndSaveChanges(viewAdapter.getTeams())
                val action = EditTeamsFragmentDirections.actionEditFragmentToDeleteTeamsFragment()
                NavHostFragment.findNavController(nav_host_fragment).navigate(action)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}