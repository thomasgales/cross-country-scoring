package com.example.crosscountryscoring.scoring

import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.internal.view.SupportMenuItem.SHOW_AS_ACTION_ALWAYS
import androidx.core.internal.view.SupportMenuItem.SHOW_AS_ACTION_NEVER
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

class RaceFragment : Fragment(), View.OnClickListener {

    private lateinit var viewModel: RaceViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RaceRecyclerViewAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewModelFactory: RaceViewModelFactory
    private val sharedVm: SharedTeamsViewModel by activityViewModels()

    private lateinit var timerService: CrossCountryRaceTimerService.LocalBinder
    private var mBound = false

    private var _binding: FragmentRaceBinding? = null

    private var previousUndoAvailable = false

    /** Defines callbacks for service binding, passed to bindService()  */
    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            timerService = service as CrossCountryRaceTimerService.LocalBinder
            mBound = true
            _binding?.timeElapsed = getSecondsElapsed()
            // Check if service was previously started and activity was destroyed without
            //  stopping the race. If so, tell view model the race is running.
            if (timerService.raceTimerRunning.value == true) {
                viewModel.startRace()
            }
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }

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
                setPositiveButton(positiveOption) { _, _ -> positiveCallback() }
                setNegativeButton(negativeOption) { _, _ -> negativeCallback() }
            }
            builder.setMessage(message)
            builder.create()
        }
        alertDialog?.show()
    }

    /**
     * Ends the race. Does NOT clear all scores and finishers.
     */
    private fun endRace(showRaceEndedToast: Boolean) {
        Intent(activity, CrossCountryRaceTimerService::class.java).also {
            activity?.unbindService(connection)
        }
        Intent(activity, CrossCountryRaceTimerService::class.java).also { intent ->
            activity?.stopService(intent)
            // Putting toast here instead of in service to avoid inadvertently showing toast if
            //  service gets destroyed before ever being started
            if (showRaceEndedToast) {
                Toast.makeText(activity, "Race ended", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.endRace()
        Intent(activity, CrossCountryRaceTimerService::class.java).also { intent ->
            activity?.bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
        activity?.invalidateOptionsMenu()
    }

    /**
     * Returns the number of seconds that have elapsed since the race was last started.
     */
    private fun getSecondsElapsed() : LiveData<Long> {
        return if (mBound) {
            timerService.totalSecondsElapsed
        } else {
            MutableLiveData(0L)
        }
    }

    /**
     * Should be called whenever the number of finished runners has changed. Decides if options menu
     *  needs to be updated.
     */
    private fun numberFinishedRunnersChanged() {
        if (viewModel.undoAvailable.value != previousUndoAvailable) {
            activity?.invalidateOptionsMenu()
            previousUndoAvailable = viewModel.undoAvailable.value ?: false
        }
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
        _binding?.timeElapsed = getSecondsElapsed()

        viewManager = LinearLayoutManager(activity)
        viewAdapter = RaceRecyclerViewAdapter(sharedVm.teams, viewModel, viewLifecycleOwner)
        sharedVm.teams.observe(viewLifecycleOwner, Observer {
            viewAdapter.onDatasetChange()
        })

        viewModel.undoAvailable.observe(viewLifecycleOwner, Observer {
            numberFinishedRunnersChanged()
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
        if (viewModel.raceRunning.value == true) {
            menu.findItem(R.id.edit_race_button).setShowAsAction(SHOW_AS_ACTION_NEVER)
        }
        else {
            menu.findItem(R.id.edit_race_button).setShowAsAction(SHOW_AS_ACTION_ALWAYS)
        }
        menu.findItem(R.id.undo_finisher_button).isVisible = viewModel.undoAvailable.value ?: false
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
                askForConfirmation(getString(R.string.confirm_race_end),
                    "End Race",
                    {endRace(true)},
                    "Cancel",
                    {})
                true
            }
            R.id.reset_race_button -> {
                // Ask if we should reset the race.
                askForConfirmation(getString(R.string.confirm_race_reset),
                    "Reset Race",
                    ::resetRace,
                    "Cancel"
                ) {}
                true
            }
            R.id.undo_finisher_button -> {
                val undoResult = viewModel.undoRunnerFinished()
                if (undoResult.undoPerformed) {
                    Toast.makeText(activity,
                        getString(R.string.undo_finisher_toast, undoResult.teamPerformedOn),
                        Toast.LENGTH_SHORT)
                        .show()
                }
                viewAdapter.onDatasetChange()
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

    override fun onResume() {
        // Hide keyboard if it was visible. Not relevant for this fragment.
        val imm: InputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val windowToken = _binding?.root?.windowToken
        imm.hideSoftInputFromWindow(windowToken, 0)
        super.onResume()
    }

    override fun onStart() {
        super.onStart()
        Intent(activity, CrossCountryRaceTimerService::class.java).also { intent ->
            activity?.bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        activity?.unbindService(connection)
        mBound = false
    }

    /**
     * Ends and resets the race.
     */
    private fun resetRace() {
        endRace(false)
        viewModel.resetRace()
        Toast.makeText(activity, "Race reset", Toast.LENGTH_SHORT).show()
        viewAdapter.onDatasetChange()
    }

    private fun startRace() {
        Intent(activity, CrossCountryRaceTimerService::class.java).also { intent ->
            activity?.startService(intent)
            Toast.makeText(activity, "Race started", Toast.LENGTH_SHORT).show()
        }
        viewModel.startRace()
        activity?.invalidateOptionsMenu()
    }
}
