package com.kaltt.agenda.classes

import com.kaltt.agenda.classes.enums.EventType
import java.time.LocalDateTime

class EventPosposition(override var father: Event) : EventChild {
    // CONFIG
    override val eventType: EventType = EventType.POSPOSITION
    // VALUES
    var daysLimit: Int = 0
    var lastInteraction: LocalDateTime? = null
    override var lastSeen: LocalDateTime
        get() = lastInteraction ?: this.father.end
        set(value) { lastInteraction = value }
    // MODIFICATIONS
    override var start: LocalDateTime
        get() = lastSeen
        set(value) { lastSeen = value }
    override var end: LocalDateTime
        get() = this.father.end.plusDays(this.daysLimit.toLong())
        set(value) {}
    fun daysLeft(): Int {
        return Difference.between(LocalDateTime.now(), Difference(days = daysLimit).applyOn(father.start)).days
    }
    override var isComplete: Boolean
        get() = this.father.isComplete
        set(value) { this.father.isComplete }

    override fun isLastRepetition(): Boolean = false
    override fun isLastAnticipation(): Boolean = false
    override fun isLastReminder(): Boolean = false
    override fun isLastPosposition(): Boolean = Difference.between(this.start, this.end).days == 0
}