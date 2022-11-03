package com.kaltt.agenda.classes.events

import com.kaltt.agenda.classes.Difference
import com.kaltt.agenda.classes.enums.EventType
import java.time.LocalDateTime

class EventAnticipation(override val father: Event, var diff: Difference) : EventChild {
    override val eventType: EventType = EventType.ANTICIPATION
    var position: Int
        get() = this.father.anticipations.indexOf(this)
        set(value) {}
    override var lastSeen: LocalDateTime = LocalDateTime.MIN
        get() = if(this.position == 0) field else this.father.anticipations[0].lastSeen
        set(value) {
            if(position == 0) {
                field = value
            } else {
                this.father.anticipations[0].lastSeen = value
            }
        }
    override var start: LocalDateTime
        get() = this.diff.applyOn(this.father.start)
        set(value) { this.diff = Difference.between(this.father.start, value) }
    override var end: LocalDateTime
        get() = (this.father.anticipations.getOrNull(this.father.anticipations.indexOf(this)+1) ?: this.father).start
        set(value) {}
    override fun isLastAnticipation(): Boolean = this.position+1 == this.father.anticipations.size
    override fun isLastReminder(): Boolean = false
    override fun isLastRepetition(): Boolean = false
    override fun isLastPosposition(): Boolean = false

    init {
        this.father.anticipations.add(this)
    }
}