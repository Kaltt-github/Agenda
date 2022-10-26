package com.kaltt.agenda.classes

import com.kaltt.agenda.classes.enums.EventType
import com.kaltt.agenda.classes.enums.ScheduleType
import java.time.LocalDateTime

class EventReminder(override val father: Event) : EventChild {
    // CONFIG
    override val eventType: EventType = EventType.REMINDER
    var diff: Difference
        get() = Difference.by(this.type, this.delay)
        set(value) {}
    var type: ScheduleType = ScheduleType.DONT
    var delay: Int = 0
    override var lastSeen: LocalDateTime = this.father.start
    private fun starts(): ArrayList<LocalDateTime> {
        var r = ArrayList<LocalDateTime>()
        var t = this.father.start
        if (!diff.isEmpty()) {
            while (t.isBefore(this.father.end)) {
                if (t.isAfter(LocalDateTime.now())) {
                    r.add(t)
                }
                t = diff.applyOn(t)
            }
        }
        return r
    }

    override var start: LocalDateTime
        get() = starts().getOrNull(0) ?: this.father.end
        set(value) {}
    override var end: LocalDateTime
        get() = starts().getOrNull(1) ?: diff.applyOn(this.father.end)
        set(value) {}

    override fun isLastReminder(): Boolean = starts().getOrNull(1) == null
    override fun isLastPosposition(): Boolean = false
    override fun isLastRepetition(): Boolean = false
    override fun isLastAnticipation(): Boolean = false

}