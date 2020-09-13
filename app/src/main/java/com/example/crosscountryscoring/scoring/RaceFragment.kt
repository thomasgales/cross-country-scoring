package com.example.crosscountryscoring.scoring

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
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

interface TimerChangedListener {
    fun timerChanged(newTime: String)
}

class RaceFragment : Fragment(), View.OnClickListener, TimerChangedListener {

    private lateinit var viewModel: RaceViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RaceRecyclerViewAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewModelFactory: RaceViewModelFactory
    private val sharedVm: SharedTeamsViewModel by activityViewModels()
    private lateinit var timer: CountUpTimer

    private var timeElapsed_: MutableLiveData<String> = MutableLiveData("00:00")
    val timeElapsed: LiveData<String> = timeElapsed_

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
        timer.cancel()
        timeElapsed_.value = "00:00"
        viewAdapter.onDatasetChange()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // We have to grab race from database asynchronously, so pass in null for now
        val racesDao = activity?.let { CC_ScoringDatabase.getInstance(it).racesDao() }

        @Suppress("UNCHECKED_CAST")
        val teams = sharedVm.teams as LiveData<List<ITeamViewModel>>
        viewModelFactory = RaceViewModelFactory(null, teams, racesDao)
        viewModel = ViewModelProvider(this, viewModelFactory).get(RaceViewModel::class.java)

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

        timer = CountUpTimer(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentRaceBinding.inflate(inflater, container, false)
        _binding?.lifecycleOwner = viewLifecycleOwner

        _binding?.viewModel = viewModel
        _binding?.timeElapsed = timeElapsed

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

        _binding?.startRaceButton?.setOnClickListener(this)

        return _binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.end_race_button).isVisible = viewModel.raceRunning.value ?: true
        super.onPrepareOptionsMenu(menu)
    }

    // Item in top bar selected. Act on it!
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.edit_race_button -> {
                val action = RaceFragmentDirections.actionRaceFragmentToEditTeamsFragment()
                NavHostFragment.findNavController(nav_host_fragment).navigate(action)
                true
            }
            R.id.end_race_button -> {
                // Ask if we should stop the race
                askForConfirmation(getString(R.string.confirm_race_clear),
                    "End Race",
                    ::endRace,
                    "Cancel",
                    {})
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.startRaceButton -> {
                startRace()
            }
        }
    }

    private fun startRace() {
        viewModel.startRace()
        timer.start()
        activity?.invalidateOptionsMenu()
    }

    override fun timerChanged(newTime: String) {
        timeElapsed_.value = newTime
    }
}
