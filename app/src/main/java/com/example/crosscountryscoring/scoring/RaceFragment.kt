package com.example.crosscountryscoring.scoring

import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
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
    // This property is only valid between onCreateView and
    // onDestroyView.

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
        Intent(activity, CrossCountryRaceTimerService::class.java).also {
            activity?.unbindService(connection)
        }
        Intent(activity, CrossCountryRaceTimerService::class.java).also { intent ->
            activity?.stopService(intent)
            // Putting toast here instead of in service to avoid inadvertently showing toast if
            //  service gets destroyed before ever being started
            Toast.makeText(activity, "Race ended", Toast.LENGTH_SHORT).show()
        }
        viewModel.endRace()
        Intent(activity, CrossCountryRaceTimerService::class.java).also { intent ->
            activity?.bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
        viewAdapter.onDatasetChange()
    }

    private fun getSecondsElapsed() : LiveData<Long> {
        return if (mBound) {
            timerService.totalSecondsElapsed
        } else {
            MutableLiveData(0L)
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

    private fun startRace() {
        Intent(activity, CrossCountryRaceTimerService::class.java).also { intent ->
            activity?.startService(intent)
            Toast.makeText(activity, "Race started", Toast.LENGTH_SHORT).show()
        }
        viewModel.startRace()
        activity?.invalidateOptionsMenu()
    }
}
