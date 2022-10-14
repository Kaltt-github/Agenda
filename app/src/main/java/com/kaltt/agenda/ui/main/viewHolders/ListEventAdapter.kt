package com.kaltt.agenda.ui.main.viewHolders

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kaltt.agenda.R
import com.kaltt.agenda.classes.Event

class ListEventAdapter(val items: ArrayList<Event>, context: Context)
    : RecyclerView.Adapter<ListEventAdapter.ListEventViewHolder>() {
    class ListEventViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.txt_event_list_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListEventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_event_in_list, parent, false)
        return ListEventViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListEventViewHolder, position: Int) {
        holder.name.text = items[position].name
    }

    override fun getItemCount(): Int {
        return items.size
    }

}