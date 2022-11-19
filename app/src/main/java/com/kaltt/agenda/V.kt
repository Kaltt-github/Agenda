package com.kaltt.agenda

import com.kaltt.agenda.classes.events.*
import com.kaltt.agenda.classes.*
import com.kaltt.agenda.classes.interfaces.Event

class V private constructor() {
    companion object {
        const val successResult = 200
        private var instance: V? = null

        fun getInstance(): V {
            if(instance == null) {
                instance = V()
            }
            return instance!!
        }
    }
    var ownedTags: ArrayList<Tag> = ArrayList()
    var sharedTags: ArrayList<Tag> = ArrayList()
    val allTags: ArrayList<Tag> = ArrayList()

    var sharedEvents: ArrayList<EventFather> = ArrayList()
        set(value) {
            val ids = value.map { it.id }
            allEvents.removeIf { ids.contains(it.id) }
            field = value
            value.forEach {
                allEvents.addAll(it.selfWithChildren())
            }
            allEvents.sortBy { it.start }
        }
    var ownedEvents: ArrayList<EventFather> = ArrayList()
        set(value) {
            val ids = value.map { it.id }
            allEvents.removeIf { ids.contains(it.id) }
            field = value
            value.forEach {
                allEvents.addAll(it.selfWithChildren())
            }
            allEvents.sortBy { it.start }
        }
    var googleEvents: ArrayList<EventFather> = ArrayList()
        set(value) {
            val ids = value.map { it.id }
            allEvents.removeIf { ids.contains(it.id) }
            field = value
            value.forEach {
                allEvents.addAll(it.selfWithChildren())
            }
            allEvents.sortBy { it.start }
        }
    val allEvents: ArrayList<Event> = ArrayList()

}
// TODO pasar los conversores de ClassAdapter a cada objeto
// TODO pedir eventos de Calendar (solicitar pais e idioma)
// TODO animacion de deslizar a los lados
// TODO compartir eventos
// TODO eventos con alarma
// TODO tutorial para usuarios nuevos/opcion en ... para volver a ver
// TODO que las actualzaciones de Father actualicen Repeat
// TODO los tags se actualizan y actualiza los eventos
// TODO conservar los completed de reminder y repeat al hacer cambios
// TODO conservar los completed de reminder y repeat en base de datos
// TODO funcionamamiento sin wifi
// TODO aspecto de completado, expirado
// TODO inverted booleans
// TODO usar fecha local ultima subida, ultima bajada y comparar con nube ultima subida
// TODO agregar los idiomas y regiones de google maps? elegir idioma
