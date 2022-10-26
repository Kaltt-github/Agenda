package com.kaltt.agenda.classes

import com.kaltt.agenda.classes.enums.EventType
import com.kaltt.agenda.classes.enums.ScheduleType
import java.time.LocalDateTime

class EventRepeat(override var father: Event, val position: Int) : EventChild {
    override val eventType: EventType = EventType.REPEAT
    override var lastSeen: LocalDateTime = LocalDateTime.MIN

    var type: ScheduleType
        get() = this.father.repeatType
        set(value) { this.father.setRepetitions(type = value,null,null) }
    var delay: Int
        get() = this.father.repeatDelay
        set(value) { this.father.setRepetitions(null, delay = value,null) }
    var limit: LocalDateTime?
        get() = this.father.repeatLimit
        set(value) { this.father.setRepetitions(null, null, limit = value) }
    override var start: LocalDateTime
        get() = Difference.by(this.type, this.delay).multiply(this.position+1).applyOn(this.father.start)
        set(value) {}
    override var end: LocalDateTime
        get() = Difference.between(this.father.start, this.father.end).applyOn(this.start)
        set(value) {}

    override fun isLastRepetition(): Boolean = this.father.repetitions.size == this.position+1
    override fun isLastAnticipation(): Boolean = false
    override fun isLastReminder(): Boolean = false
    override fun isLastPosposition(): Boolean = false

    override var tasks: ArrayList<Task> = ArrayList()
    override var isComplete: Boolean = false
    override var posposition = EventPosposition(this)
    override var anticipations: ArrayList<EventAnticipation> = ArrayList()
    override var reminder: EventReminder = EventReminder(this)

    init {
        this.father.anticipations.forEach {
            this.addAnticipation(it.diff)
        }
        this.posposition.daysLimit = this.father.posposition.daysLimit
        this.reminder.type = this.father.reminder.type
        this.reminder.delay = this.father.reminder.delay
        this.father.tasks.forEach {
            this.tasks.add(Task(this, it.description, false))
        }
    }

    override fun allEvents(): ArrayList<Event> {
        var e = ArrayList<Event>()
        e.add(this)
        if(this.hasAnticipations()) {
            e.addAll(this.anticipations)
        }
        if(this.isExpired() && this.isPosponable()) {
            e.add(this.posposition)
        } else if(this.hasReminders()) {
            e.add(this.reminder)
        }
        return e
    }
}