package com.example.crosscountryscoring.deleteteams

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.crosscountryscoring.R
import com.example.crosscountryscoring.SharedTeamsViewModel
import com.example.crosscountryscoring.databinding.FragmentDeleteTeamsBinding

class DeleteTeamsFragment : Fragment() {

    private lateinit var viewModel: DeleteTeamsViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: DeleteTeamsRecyclerViewAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private val sharedVm: SharedTeamsViewModel by activityViewModels()
    private lateinit var viewModelFactory: DeleteTeamsViewModelFactory

    private var _binding: FragmentDeleteTeamsBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

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

    private fun deleteTeams() {
        viewModel.saveChanges(viewAdapter.getTeams())
        findNavController().navigateUp()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentDeleteTeamsBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        viewModelFactory = DeleteTeamsViewModelFactory(sharedVm)
        viewModel = ViewModelProvider(this, viewModelFactory).get(DeleteTeamsViewModel::class.java)

        viewManager = LinearLayoutManager(activity)

        viewAdapter = DeleteTeamsRecyclerViewAdapter(sharedVm.teams)
        sharedVm.teams.observe(viewLifecycleOwner, Observer {
            viewAdapter.onDatasetChange()
        })

        recyclerView = binding.deleteTeamsRecyclerView.apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            adapter = viewAdapter
        }

        setHasOptionsMenu(true)

        binding.cancelDeleteTeamsButton.setOnClickListener {
            // Don't save changes- user cancelled!
            findNavController().navigateUp()
        }

        binding.confirmDeleteTeamsButton.setOnClickListener {
            askForConfirmation(getString(R.string.confirm_delete_teams),
                "Delete",
                ::deleteTeams,
                "Cancel",
                {})
        }

        return binding.root
    }

    // Button in top bar pressed. Act on it!
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Don't save changes- user didn't press confirm!
                // TO DO: Ask user if they meant to confirm deletion?
                findNavController().navigateUp()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}