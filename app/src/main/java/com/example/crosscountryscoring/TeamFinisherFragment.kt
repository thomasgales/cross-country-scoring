package com.example.crosscountryscoring

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.crosscountryscoring.databinding.TeamFinisherFragmentBinding
import java.lang.ClassCastException


class TeamFinisherFragment : Fragment(), View.OnClickListener {

    companion object {
        fun newInstance() = TeamFinisherFragment()
    }

    interface OnTeamFinisherClickedListener {
        fun onTeamFinisherClicked()
    }

    private var listener: OnTeamFinisherClickedListener? = null

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
        viewAdapter = TeamFinisherAdapter(viewModel.fakeTeams, this)

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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? OnTeamFinisherClickedListener
        if (listener == null) {
            throw ClassCastException("$context must implement OnTeamFinisherClickedListener")
        }
    }

    override fun onClick(v: View) {
        listener?.onTeamFinisherClicked()
    }

}
