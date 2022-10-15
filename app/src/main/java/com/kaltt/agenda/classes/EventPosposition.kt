package com.kaltt.agenda.classes

import com.kaltt.agenda.classes.enums.EventType

class EventPosposition(override var father: Event, var days: Int
) : EventChild {

    override val type: EventType = EventType.POSPOSITION
    override var wasSeen: Boolean = false // SAVE ME VAR
    override var isCompleted: Boolean
    get() {
        return wasSeen || isCompleted
    }
    set(value) {
        wasSeen = value
    }
}