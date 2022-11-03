package com.kaltt.agenda

import com.kaltt.agenda.classes.events.Event
import com.kaltt.agenda.classes.events.EventFather
import com.kaltt.agenda.ui.main.viewHolders.ListEventAdapter
import kotlin.reflect.KFunction0

class V {
    companion object {
        val successResult = 100
        var sharedEvents: ArrayList<EventFather> = ArrayList()
        var ownedEvents: ArrayList<EventFather> = ArrayList()
        var googleEvents: ArrayList<EventFather> = ArrayList()
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
    }
}

// TODO pedir eventos de Calendar (solicitar pais e idioma)
// TODO animacion de deslizar a los lados
// TODO implementacion evento pospuesto
// TODO compartir eventos
// TODO eventos con alarma
// TODO tutorial para usuarios nuevos/opcion en ... para volver a ver
