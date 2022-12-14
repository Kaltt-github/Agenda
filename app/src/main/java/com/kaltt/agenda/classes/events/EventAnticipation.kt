package com.kaltt.agenda.classes.events

import com.kaltt.agenda.classes.DataDateTime
import com.kaltt.agenda.classes.DataEventAnticipation
import com.kaltt.agenda.classes.Difference
import com.kaltt.agenda.classes.enums.EventType
import com.kaltt.agenda.classes.interfaces.Event
import com.kaltt.agenda.classes.interfaces.EventChild
import java.time.LocalDateTime

class EventAnticipation(
    override val father: Event,
    override var fatherDifference: Difference
) : EventChild {

    companion object {
        fun from(f: EventFather, d: DataEventAnticipation): EventAnticipation = EventAnticipation(
            f, Difference.from(d.date)
        )
    }

    override val eventType: EventType = EventType.ANTICIPATION

    override var position: Int = -1
    override var localComplete: Boolean = false

    override var end: LocalDateTime
        get() = if(this.isLastAnticipation()) this.father.start else this.father.anticipations[this.position+1].start
        set(value) {}
}