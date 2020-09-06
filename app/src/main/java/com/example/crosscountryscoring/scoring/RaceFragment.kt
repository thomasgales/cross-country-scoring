package com.example.crosscountryscoring.scoring

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.*
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.crosscountryscoring.*
import com.example.crosscountryscoring.R
import com.example.crosscountryscoring.database.CC_ScoringDatabase
import com.example.crosscountryscoring.database.Race
import com.example.crosscountryscoring.databinding.FragmentRaceBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

interface OnRunnerFinishedListener {
    fun onRunnerFinished()
}

class RaceFragment : Fragment(), OnRunnerFinishedListener, View.OnClickListener {

    private lateinit var viewModel: RaceViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RaceRecyclerViewAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewModelFactory: RaceViewModelFactory
    private val sharedVm: SharedTeamsViewModel by activityViewModels()
    private var raceRunning: Boolean = false

    private var _binding: FragmentRaceBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.

    /**
     * Displays an AlertDialog with the message and options specified.
     * @see https://developer.android.com/guide/topics/ui/dialogs.html
     */
    private fun askForConfirmation(message: String,
                                   positiveOption: String,
                                   positiveCallback: () -> Unit,
                                   negativeOption: String,
                                   negativeCallback: () -> Unit ) {
        val alertDialog: AlertDialog? = activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setPositiveButton(positiveOption, DialogInterface.OnClickListener { _, _ -> positiveCallback()})
                setNegativeButton(negativeOption, DialogInterface.OnClickListener { _, _ -> negativeCallback()})
            }
            builder.setMessage(message)
            builder.create()
        }
        alertDialog?.show()
    }

    /**
     * Ends the race. Clears all scores and finishers.
     */
    private fun endRace() {
        viewModel.endRace()
        viewAdapter.onDatasetChange()
        _binding?.invalidateAll()
        // Reset buttons
        _binding?.toggleRaceStatusButton?.text = "Start Race"
        _binding?.endRaceButton?.visibility = View.GONE
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentRaceBinding.inflate(inflater, container, false)
        _binding?.lifecycleOwner = this
        // We have to grab race from database asynchronously, so pass in null for now
        val racesDao = activity?.let { CC_ScoringDatabase.getInstance(it).racesDao() }

        @Suppress("UNCHECKED_CAST")
        val teams = sharedVm.teams as LiveData<List<ITeamViewModel>>
        viewModelFactory = RaceViewModelFactory(null, teams, racesDao, this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(RaceViewModel::class.java)
        _binding?.viewModel = viewModel

        lifecycleScope.launch(Dispatchers.IO) {
            // FIXME using hard-coded raceId of 1 for now- need to implement multiple race feature.
            var race = racesDao?.getRace(1)
            viewModel.setDatabaseRace(race)
            // FIXME long-term we want users to add Races. For now add one for them.
            if (race == null) {
                racesDao?.addRace(Race("TestRace"))
                race = racesDao?.getRace(1)
                viewModel.setDatabaseRace(race)
            }
        }

        viewManager = LinearLayoutManager(activity)
        viewAdapter = RaceRecyclerViewAdapter(sharedVm.teams, viewModel, this)
        sharedVm.teams.observe(viewLifecycleOwner, Observer {
            viewAdapter.onDatasetChange()
        })

        recyclerView = _binding!!.raceRecyclerView.apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            adapter = viewAdapter
        }

        // Indicate that this fragment would like to add items to Options Menu
        setHasOptionsMenu(true)

        _binding?.toggleRaceStatusButton?.setOnClickListener(this)
        _binding?.endRaceButton?.setOnClickListener(this)

        return _binding?.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_main, menu)
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

    override fun onRunnerFinished() {
        _binding?.invalidateAll()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.toggleRaceStatusButton -> {
                if (raceRunning) {
                    // Stop the race!
                    _binding?.toggleRaceStatusButton?.text = "Resume"
                    // FIXME create better color scheme
                    _binding?.toggleRaceStatusButton?.background?.setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY)
                    _binding?.endRaceButton?.visibility = View.VISIBLE
                    raceRunning = false
                }
                else {
                    // Start or resume the race!
                    _binding?.toggleRaceStatusButton?.text = "Pause"
                    // FIXME create better color scheme
                    _binding?.toggleRaceStatusButton?.background?.setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY)
                    _binding?.endRaceButton?.visibility = View.GONE
                    raceRunning = true
                }
            }
            R.id.endRaceButton -> {
                askForConfirmation(getString(R.string.confirm_race_clear),
                    "End Race",
                    ::endRace,
                    "Cancel",
                    {})
            }
        }
    }
}
