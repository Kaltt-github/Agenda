package com.kaltt.agenda.ui.main.viewHolders

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.recyclerview.widget.RecyclerView
import com.kaltt.agenda.R
import com.kaltt.agenda.classes.ColorTool
import com.kaltt.agenda.classes.Event
import com.kaltt.agenda.classes.enums.EventType
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class ListEventAdapter(val items: ArrayList<Event>, context: Context)
    : RecyclerView.Adapter<ListEventAdapter.InListEventViewHolder>() {
    class InListEventViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.iw_event_list_icon)
        val childName: TextView = view.findViewById(R.id.txt_event_list_child_name)
        val childType: TextView = view.findViewById(R.id.txt_event_list_child_type)
        val fatherName: TextView = view.findViewById(R.id.txt_event_list_father_name)
        val endText: TextView = view.findViewById(R.id.txt_event_list_end)
        val background: ConstraintLayout = view.findViewById(R.id.cl_event_list_background)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InListEventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_event_in_list, parent, false)
        return InListEventViewHolder(view)
    }

    override fun onBindViewHolder(holder: InListEventViewHolder, position: Int) {
        var event = items[position]
        fun formatDateTime(date: LocalDateTime): String {
            var hour = date.hour.toString()
            var minute = date.minute.toString()
            var time = "${"0".repeat(2-hour.length)}${hour}:${"0".repeat(2 - minute.length)}${minute}hs"
            return (if(LocalDate.now().isEqual(date.toLocalDate())) "Hoy" else "${date.dayOfMonth}/${date.monthValue+1}/${date.year}") + "\n${time}"
        }
        fun setHeader() {
            if(event.eventType == EventType.FATHER) {
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
                holder.childType.text = when(event.eventType) {
                    EventType.REMINDER -> "Recordatorio"
                    EventType.POSPOSITION -> "Pospuesto"
                    EventType.REPEAT -> "Repeticion"
                    EventType.ANTICIPATION -> "Anticipacion"
                    else -> { "???" }
                }
            }
            holder.endText.text = formatDateTime(event.end)
        }
        fun setTasks() {
            if(event.hasTasks()) {
                //var taskArray = ArrayAdapter(this, R.layout.fragment_task_in_event_in_list, event.tasks)
                // Visibilidad VISIBLE
                // Agregar tasks
            } else {
                // Visibilidad GONE
            }
        }
        fun setColors() {
            var background = Color.parseColor(ColorTool(event.color, 75.0, 85.0).rgb)
            var image = Color.parseColor(ColorTool(event.color, 50.0, 95.0).rgb)
            var icon = Color.parseColor(ColorTool(event.color, 85.0, 40.0).rgb)
            var title = Color.parseColor(ColorTool(event.color, 78.0, 14.0).rgb)
            var time = Color.parseColor(ColorTool(event.color, 65.0, 25.0).rgb)
            var tasks = Color.parseColor(ColorTool(event.color, 8.0, 89.0).rgb)

            holder.background.background.setColorFilter(background, PorterDuff.Mode.SRC_ATOP)
            holder.icon.background.setColorFilter(image, PorterDuff.Mode.SRC_ATOP)
            holder.icon.setColorFilter(icon)

            if(event.isFather()) {
                holder.fatherName.setTextColor(title)
            } else {
                holder.childName.setTextColor(title)
                holder.childType.setTextColor(title)
            }
            holder.endText.setTextColor(time)
        }
        setHeader()
        setTasks()
        setColors()
    }

    override fun getItemCount(): Int {
        return items.size
    }

}