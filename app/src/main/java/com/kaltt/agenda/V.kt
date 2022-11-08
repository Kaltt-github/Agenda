package com.kaltt.agenda

import com.kaltt.agenda.classes.events.*
import com.kaltt.agenda.classes.*

class V {
    companion object {
        val successResult = 100

        var ownedTags: ArrayList<Tag> = ArrayList()
        var sharedTags: ArrayList<Tag> = ArrayList()

        val allTags: ArrayList<Tag>
            get() {
                var result = ArrayList<Tag>()
                result.addAll(ownedTags)
                result.addAll(sharedTags)
                return result
            }

        var sharedEvents: ArrayList<EventFather> = ArrayList()
        var ownedEvents: ArrayList<EventFather> = ArrayList()
        var googleEvents: ArrayList<EventFather> = ArrayList()

        val allEvents: ArrayList<Event>
            get() {
                val result = ArrayList<Event>()
                sharedEvents.forEach { result.addAll(it.selfWithChildren())}
                ownedEvents.forEach { result.addAll(it.selfWithChildren())}
                googleEvents.forEach { result.addAll(it.selfWithChildren())}
                result.sortBy { it.start }
                return result
            }
    }
}

// TODO pedir eventos de Calendar (solicitar pais e idioma)
// TODO animacion de deslizar a los lados
// TODO compartir eventos
// TODO eventos con alarma
// TODO tutorial para usuarios nuevos/opcion en ... para volver a ver
// TODO que las actualzaciones de Father actualicen Repeat
// TODO los tags se actualizan y actualiza los eventos
// TODO conservar los completed de reminder y repeat al hacer cambios
// TODO conservar los completed de reminder y repeat en base de datos

