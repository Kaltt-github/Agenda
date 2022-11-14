package com.kaltt.agenda.classes.events

import com.kaltt.agenda.classes.Difference
import com.kaltt.agenda.classes.Task
import com.kaltt.agenda.classes.enums.EventType
import com.kaltt.agenda.classes.enums.ScheduleType
import com.kaltt.agenda.classes.interfaces.Event
import com.kaltt.agenda.classes.interfaces.EventChild
import java.time.LocalDateTime

class EventRepeat(
    override val father: Event,
    override var fatherDifference: Difference,
) : EventChild {
    override val eventType: EventType = EventType.REPEAT

    override var position: Int = -1
    override var localComplete: Boolean = false

    override var tasks: ArrayList<Task> = ArrayList()
    override var isComplete: Boolean
        get() = this.localComplete
        set(value) { this.localComplete = value }

    override var end: LocalDateTime
        get() = this.fatherDifference.applyOn(this.father.end)
        set(value) {}

    override var anticipations: ArrayList<EventAnticipation> = ArrayList()
    override fun addAnticipation(date: LocalDateTime) {
        addAnticipation(Difference.between(date, this.start).opposite())
    }
    override fun addAnticipation(diff: Difference) {
        this.anticipations.add(EventAnticipation(this, diff))
        this.anticipations.sortBy { it.start }
        for ((i, a) in this.anticipations.withIndex()) {
            a.position = i
        }
    }

    override var posposition: EventPosposition = EventPosposition(this)
    override var posposed: Int = 0
    override fun pospose(days: Int): Boolean {
        val new = days + posposed
        if(new > pospositionDaysLimit) {
            return false
        }
        this.posposed = new
        return true
    }
    override fun posposeDaysLeft(): Int {
        return Difference.between(this.pospositionDateLimit(), LocalDateTime.now()).days
    }

    override var reminders: ArrayList<EventReminder> = ArrayList()
    override fun setReminders(type: ScheduleType, delay: Int) {
        this.reminderType = type
        this.reminderDelay = delay
        reminders.clear()
        var done = type == ScheduleType.DONT || delay == 0
        val diff = Difference.by(type, delay)
        var i = 1
        while(!done) {
            val r = EventReminder(this, diff.clone().multiply(i))
            r.position = i-1
            if(r.start.isBefore(this.end)) {
                this.reminders.add(r)
                i++
            } else {
                done = true
            }
        }
    }

    init {
        this.father.tasks.forEach {
            this.tasks.add(Task(this, it.description, false))
        }
        this.father.anticipations.forEach {
            this.addAnticipation(it.fatherDifference)
        }
        this.setReminders(this.reminderType, this.reminderDelay)
    }

    override fun selfWithChildren(): ArrayList<Event> {
        var e = ArrayList<Event>()
        e.add(this)
        if(this.hasAnticipations()) {
            e.addAll(this.anticipations)
        }
        if(this.isExpired() && this.isPosponable()) {
            e.add(this.posposition)
        } else if(this.hasReminders()) {
            e.addAll(this.reminders)
        }
        return e
    }
}