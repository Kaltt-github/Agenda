package com.kaltt.agenda.ui.main.viewHolders

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.kaltt.agenda.R
import com.kaltt.agenda.classes.Color
import com.kaltt.agenda.classes.Event
import com.kaltt.agenda.classes.enums.EventType

class ListEventAdapter(val items: ArrayList<Event>, context: Context)
    : RecyclerView.Adapter<ListEventAdapter.ListEventViewHolder>() {
    class ListEventViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.iw_event_list_icon)
        val childName: TextView = view.findViewById(R.id.txt_event_list_child_name)
        val childType: TextView = view.findViewById(R.id.txt_event_list_child_type)
        val fatherName: TextView = view.findViewById(R.id.txt_event_list_father_name)
        val background: ConstraintLayout = view.findViewById(R.id.cl_event_list_background)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListEventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_event_in_list, parent, false)
        return ListEventViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListEventViewHolder, position: Int) {
        var event = items[position]
        //holder.background.setBackgroundColor(Color(event.color.toDouble(), 81.0, 88.0).toInt())
        if(event.isFather) {
            if(holder.childName.visibility == View.VISIBLE) {
                holder.childName.visibility = View.INVISIBLE
                holder.childType.visibility = View.INVISIBLE
                holder.fatherName.visibility = View.VISIBLE
            }
            holder.fatherName.text = event.name
        } else {
            if(holder.childName.visibility == View.INVISIBLE) {
                holder.childName.visibility = View.VISIBLE
                holder.childType.visibility = View.VISIBLE
                holder.fatherName.visibility = View.INVISIBLE
            }
            holder.childName.text = event.name
            holder.childType.text = when(event.type) {
                EventType.REMINDER -> "Recordatorio"
                EventType.POSPOSITION -> "Pospuesto"
                EventType.REPEAT -> "Repeticion"
                EventType.ANTICIPATION -> "Anticipacion"
                else -> "???"
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

}