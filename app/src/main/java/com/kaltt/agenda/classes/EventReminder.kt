package com.kaltt.agenda.classes

import com.kaltt.agenda.classes.enums.EventType
import java.time.LocalDateTime

class EventReminder(
    override val father: EventFather, // SAVE ME VAR
    private val diff: Difference, // SAVE ME VAR
    override var wasSeen: Boolean
) : EventChild {
    override val type: EventType = EventType.REMINDER
    override var isCompleted: Boolean
        get() = this.father.isCompleted || wasSeen
        set(value) {
            wasSeen = value
        }
    override var start: LocalDateTime
        get() = this.diff.applyOn(this.father.start, this.father.reminders.indexOf(this)+1)
        set(value) {}
    override var end: LocalDateTime
        get() = this.diff.applyOn(this.start)
        set(value) {}
    override var isLastReminder: Boolean
        get() = this.father.reminders.indexOf(this)+1 == this.father.reminders.size
        set(value) {}
}