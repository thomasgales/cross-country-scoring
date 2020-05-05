package com.example.crosscountryscoring

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView

class TeamFinisherAdapter(private val myDataset: Array<String>, private val raceRunnerFinished: View.OnClickListener) :
    RecyclerView.Adapter<TeamFinisherAdapter.MyViewHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val name : Button = view.findViewById(R.id.team_name)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): TeamFinisherAdapter.MyViewHolder {
        // create a new view
        val teamButton = LayoutInflater.from(parent.context)
            .inflate(R.layout.team_finisher_view, parent, false) as View
        return MyViewHolder(teamButton)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.name.text = myDataset[position]
        holder.name.setOnClickListener(raceRunnerFinished)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size

}