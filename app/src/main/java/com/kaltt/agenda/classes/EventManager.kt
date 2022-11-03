package com.kaltt.agenda.classes

import androidx.recyclerview.widget.RecyclerView
import com.kaltt.agenda.classes.events.Event
import com.kaltt.agenda.classes.events.EventFather
import com.kaltt.agenda.ui.main.viewHolders.ListEventAdapter
import java.util.function.Function
import kotlin.reflect.KFunction0

class EventManager {
    companion object {
        private var instance: EventManager? = null
        fun getInstance(): EventManager {
            if(instance == null) {
                instance = EventManager()
            }
            return instance!!
        }
    }
    var sharedEvents: ArrayList<EventFather> = ArrayList()
    var ownedEvents: ArrayList<EventFather> = ArrayList()
    var googleEvents: ArrayList<EventFather> = ArrayList()

    var notifiers: ArrayList<KFunction0<Unit>> = ArrayList()
    var observers: ArrayList<ListEventAdapter> = ArrayList()

    var allEvents: ArrayList<Event>
        get() {
            val result = ArrayList<Event>()
            sharedEvents.forEach { result.addAll(it.selfWithChildren())}
            ownedEvents.forEach { result.addAll(it.selfWithChildren())}
            googleEvents.forEach { result.addAll(it.selfWithChildren())}
            result.sortBy { it.start }
            return result
        }
        set(value) {}

    fun addObserver(observer: ListEventAdapter, notifier: KFunction0<Unit>) {
        this.observers.add(observer)
        this.notifiers.add(notifier)
    }

    fun notifyChanges() {
        observers.forEach {
            it.notifyDataSetChanged()
        }
    }
}