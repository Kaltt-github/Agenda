package com.kaltt.agenda.classes.events

import com.kaltt.agenda.classes.*
import com.kaltt.agenda.classes.enums.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class EventFather(
    override val from: FromType,
    override val owner: String,
    override var tag: Tag = Tag.empty()
) : Event {
    override val eventType: EventType = EventType.FATHER
    override val father = this
    override var id: Int = -1
    override var icon: String = "" 
    override var name: String = "New"
    override var description: String = ""
    override var color: Double = 0.0
    override var priority: Int = 0
    override var tasks: ArrayList<Task> = ArrayList()
    override var location: String = ""
    override var isComplete: Boolean = false
    override var isFullDay: Boolean = false
    private var startDate: LocalDate = LocalDate.now()
    private var startTime: LocalTime = LocalTime.now()
    private var length: Difference = Difference(days = 1)

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
    override var pospositionDaysLimit: Int = 0
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
    override var reminderType: ScheduleType = ScheduleType.DONT
    override var reminderDelay: Int = 0
    override fun setReminders(type: ScheduleType, delay: Int) {
        this.reminderType = type
        this.reminderDelay = delay
        reminders.clear()
        var done = type == ScheduleType.DONT || delay == 0
        val diff = Difference.by(type, delay)
        var i = 1;
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

    override var repeats: ArrayList<EventRepeat> = ArrayList()
    override var repeatType: ScheduleType = ScheduleType.DONT
    override var repeatDelay: Int = 0
    override var repeatLimit: LocalDateTime? = null
    override fun setRepetitions(type: ScheduleType, delay: Int, limit: LocalDateTime?) {
        this.repeatType = type
        this.repeatDelay = delay
        this.repeatLimit = limit
        this.repeats.clear()
        var done = type == ScheduleType.DONT || delay == 0
        val diff = Difference.by(type, delay)
        val defaultLimit = LocalDateTime.now().plusYears(10)
        var i = 1;
        while(!done) {
            val r = EventRepeat(this, diff.clone().multiply(i))
            r.position = i-1
            if(r.start.isBefore(repeatLimit ?: defaultLimit)) {
                this.repeats.add(r)
                i++
            } else {
                done = true
            }
        }
    }

    override var sharedWith: ArrayList<String> = ArrayList()

    override var isLazy: Boolean = false
        set(value) {
            if (value) {
                this.pospositionDaysLimit = 0
                this.anticipations.clear()
                this.setReminders(ScheduleType.DONT, 0)
                this.setRepetitions(ScheduleType.DONT, 0, null)
            }
            field = value
        }
    // CALCULATED VALUES
    override var start: LocalDateTime
        get() = LocalDateTime.of(this.startDate, if (this.isFullDay) LocalTime.of(0, 0) else this.startTime)
        set(value) {
            this.startDate = value.toLocalDate()
            this.startTime = value.toLocalTime()
        }
    override var end: LocalDateTime
        get() = this.length.applyOn(this.start)
        set(value) { this.length = Difference.between(this.start, value) }

    init {
        this.tag.applyOn(this)
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
        if(this.hasRepetitions()){
            this.repeats.forEach {
                e.addAll(it.selfWithChildren())
            }
        }
        return e
    }
}