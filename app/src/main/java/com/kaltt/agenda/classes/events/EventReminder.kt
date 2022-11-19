package com.kaltt.agenda.classes.events

import com.kaltt.agenda.classes.Difference
import com.kaltt.agenda.classes.Task
import com.kaltt.agenda.classes.enums.EventType
import com.kaltt.agenda.classes.interfaces.Event
import com.kaltt.agenda.classes.interfaces.EventChild
import java.time.LocalDateTime

class EventReminder(
    override val father: Event,
    override var fatherDifference: Difference
) : EventChild {
    override val eventType: EventType = EventType.REMINDER

    override var position: Int = -1
    override var localComplete: Boolean = false

    override var end: LocalDateTime
        get() = if(this.isLastReminder()) this.father.end else this.father.reminders[this.position+1].start
        set(value) {}

    override fun hasTasks(): Boolean = false
}