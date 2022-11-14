package com.kaltt.agenda.ui.main.viewHolders

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.kaltt.agenda.R
import com.kaltt.agenda.classes.ColorTool
import com.kaltt.agenda.classes.Task
import com.kaltt.agenda.classes.enums.EventType
import com.kaltt.agenda.classes.interfaces.Event
import java.time.LocalDateTime

class ListEventAdapter(val items: ArrayList<Event>, context: Context)
    : RecyclerView.Adapter<ListEventAdapter.InListEventViewHolder>() {
    class InListEventViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.iw_event_list_icon)
        val childName: TextView = view.findViewById(R.id.txt_event_list_child_name)
        val childType: TextView = view.findViewById(R.id.txt_event_list_child_type)
        val fatherName: TextView = view.findViewById(R.id.txt_event_list_father_name)
        val endText: TextView = view.findViewById(R.id.txt_event_list_end)
        val background: ConstraintLayout = view.findViewById(R.id.cl_event_list_background)
        val tasks: LinearLayout = view.findViewById(R.id.ll_tasks_container)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InListEventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_event_in_list, parent, false)
        return InListEventViewHolder(view)
    }

    override fun onBindViewHolder(holder: InListEventViewHolder, position: Int) {
        val event = items[position]

        fun formatDateTime(fullDay: Boolean, start: LocalDateTime, end: LocalDateTime): String {
            fun twoDigits(n: Int): String = "0".repeat(2 - "$n".length)+"$n"
            fun dateFormat(date: LocalDateTime): String {
                return (if(fullDay) "" else  "${date.hour}:${twoDigits(date.minute)}") + (if(!fullDay && start.toLocalDate().equals(end.toLocalDate())) "" else " ${date.dayOfMonth}/${twoDigits(date.monthValue+1)}")
            }
            return "${dateFormat(start)}\n${dateFormat(end)}"
        }
        fun setHeader() {
            if(event.eventType == EventType.FATHER) {
                holder.childName.visibility = View.GONE
                holder.childType.visibility = View.GONE
                holder.fatherName.visibility = View.VISIBLE
                holder.fatherName.text = event.name
            } else {
                holder.childName.visibility = View.VISIBLE
                holder.childType.visibility = View.VISIBLE
                holder.fatherName.visibility = View.GONE
                holder.childName.text = event.name
                holder.childType.text = when(event.eventType) {
                    EventType.REMINDER -> "Recordatorio"
                    EventType.POSPOSITION -> "Pospuesto"
                    EventType.REPEAT -> "Repeticion"
                    EventType.ANTICIPATION -> "Anticipacion"
                    else -> { "???" }
                }
            }
            holder.endText.text = formatDateTime(event.isFullDay, event.start, event.end)
        }
        fun setTasks() {
            if(event.hasTasks()) {
                holder.tasks.visibility = View.VISIBLE
                while(holder.tasks.childCount > event.tasks.size) {
                    holder.tasks.removeViewAt(holder.tasks.childCount - 1)
                }
                event.tasks.forEachIndexed { i, it ->
                    val isNew = i > holder.tasks.childCount
                    val view = if(isNew)
                        LayoutInflater.from(holder.tasks.context).inflate(
                            R.layout.fragment_task_in_event_in_list,
                            holder.tasks, false
                        )
                        else holder.tasks.getChildAt(i)

                    view.findViewById<CheckBox>(R.id.cn_task).isChecked = it.isDone
                    view.findViewById<TextView>(R.id.txt_task_descripcion).text = it.description

                    if(isNew) {
                        holder.tasks.addView(view)
                    }
                }
            } else {
                holder.tasks.visibility = View.GONE
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