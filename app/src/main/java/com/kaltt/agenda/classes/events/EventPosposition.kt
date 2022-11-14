package com.kaltt.agenda.classes.events

import com.kaltt.agenda.classes.Difference
import com.kaltt.agenda.classes.enums.EventType
import com.kaltt.agenda.classes.interfaces.Event
import com.kaltt.agenda.classes.interfaces.EventChild
import java.time.LocalDateTime

class EventPosposition(
    override val father: Event
) : EventChild {
    override var fatherDifference: Difference = Difference()
    override val eventType: EventType = EventType.POSPOSITION

    override var position: Int = -1
    override var localComplete: Boolean = false

    override var start: LocalDateTime
        get() = this.father.start.plusDays(this.father.posposed.toLong())
        set(value) {}
    override var end: LocalDateTime
        get() = Difference.between(this.father.start, this.father.end).applyOn(this.start)
        set(value) {}
}