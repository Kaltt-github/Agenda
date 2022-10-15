package com.kaltt.agenda.classes

import com.kaltt.agenda.classes.enums.EventType
import java.time.LocalDateTime

class EventAnticipation(
    override val father: EventFather,  // SAVE ME VAR
    private val diff: Difference, // SAVE ME VAR
    override var wasSeen: Boolean // SAVE ME VAR
) : EventChild {
    override val type: EventType = EventType.ANTICIPATION
    override var isCompleted: Boolean
        get() = this.father.isCompleted || wasSeen
        set(value) {
            wasSeen = value
        }
    // NOT LAZY AFTER THIS
    override var isFullDay: Boolean = this.father.isFullDay
    override var start: LocalDateTime
        get() = this.diff.applyOn(this.father.start)
        set(value) {}
    override var end: LocalDateTime
        get() = this.father.anticipations[this.father.anticipations.indexOf(this)+1]?.start ?: this.father.start
        set(value) {}
    override var isLastAnticipation: Boolean
        get() = this.father.anticipations.indexOf(this)+1 == this.father.anticipations.size
        set(value) {}
    // FUNCTIONS
}