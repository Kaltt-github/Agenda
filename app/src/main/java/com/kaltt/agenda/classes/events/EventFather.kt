package com.kaltt.agenda.classes.events

import com.kaltt.agenda.V
import com.kaltt.agenda.classes.*
import com.kaltt.agenda.classes.enums.*
import com.kaltt.agenda.classes.interfaces.Event
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class EventFather(
    override val from: FromType,
    override val owner: String,
    override var tag: Tag = Tag.empty()
) : Event {

    companion object{
        fun from(from: FromType, data: DataEventFather): EventFather {
            val e = EventFather(from, data.owner)
            e.name = data.name
            e.description = data.description
            e.tag = V.getInstance().allTags.find { it.id == data.tag } ?: Tag.empty()
            e.id = data.id
            e.icon = data.icon
            e.color = data.color
            e.priority = data.priority
            e.tasks.clear()
            data.tasks.sortBy { it.position }
            e.tasks = ArrayList(data.tasks.map { Task.from(e, it) })
            e.location = data.location
            e.isComplete = data.isComplete
            e.isFullDay = data.isFullDay
            e.start = data.start.toLocalDateTime()
            e.end = data.end.toLocalDateTime()
            data.anticipations.map { }
            e.anticipations = ArrayList( data.anticipations.map { EventAnticipation.from(e, it) })
            e.pospositionDaysLimit = data.pospositionDaysLimit
            e.posposed = data.posposed
            e.setReminders(
                ScheduleType.from(data.reminderType),
                data.remidnerDelay
            )
            e.setRepetitions(
                ScheduleType.from(data.repeatType),
                data.repeatDelay,
                if(!Difference.from(data.repeatLimit).isEmpty()) {
                    data.repeatLimit.toLocalDateTime()
                } else { null }
            )
            e.sharedWith = data.sharedWith
            e.isLazy = data.isLazy
            return e
        }
        fun from(it: DataEventGoogle): EventFather {
            fun extract(s: String): LocalDateTime {
                var a = s.split("-").map { it.toInt() }
                return LocalDateTime.of(a[0],a[1],a[2],0,0)
            }

                var e = EventFather(FromType.GOOGLE, "Google")
                e.id = it.id
                e.name = it.name
                e.description = it.description
                e.start = extract(it.start.date)
                e.end = extract(it.end.date)
                e.pospositionDaysLimit = 0
                e.isFullDay = true
            return e
        }
    }

    override val eventType: EventType = EventType.FATHER
    override val father = this
    override var id: String = ""
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
        var i = 1
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
    override fun toData(): DataEventFather = DataEventFather(
        owner,
        id,
        name,
        description,
        tag.id,
        icon,
        color,
        priority,
        isLazy,
        ArrayList(tasks.map { DataTask(it.position, it.description, it.isDone) }),
        location,
        isComplete,
        isFullDay,
        DataDateTime.from(start),
        DataDateTime.from(end),
        ArrayList(anticipations.map { DataEventAnticipation(it.localComplete, it.fatherDifference.toData()) }),
        pospositionDaysLimit,
        posposed,
        reminderType.toString(),
        reminderDelay,
        repeatType.toString(),
        repeatDelay,
        DataDateTime.from(repeatLimit),
        sharedWith
    )
    private fun asEventAnticipation(d: DataEventAnticipation): EventAnticipation {
        val a = EventAnticipation(this, Difference.from(d.date))
        if(d.isDone) {
            a.isComplete = true
        }
        return a
    }
    override fun toMap(): Map<String, Any> =
        hashMapOf(
            "owner" to owner,
            "id" to id,
            "name" to name,
            "description" to description,
            "tag" to tag.id,
            "icon" to icon,
            "color" to color,
            "priority" to priority,
            "isLazy" to isLazy,
            "tasks" to tasks.map {
                hashMapOf(
                    "position" to it.position,
                    "description" to it.description,
                    "isDone" to it.isDone
                )
            },
            "location" to location,
            "isComplete" to isComplete,
            "isFullDay" to isFullDay,
            "start" to hashMapOf(
                "year" to start.year,
                "month" to start.monthValue+1,
                "day" to start.dayOfMonth,
                "hour" to start.hour,
                "minute" to start.minute
            ),
            "end" to hashMapOf(
                "year" to end.year,
                "month" to end.monthValue+1,
                "day" to end.dayOfMonth,
                "hour" to end.hour,
                "minute" to end.minute,
            ),
            "anticipations" to anticipations.map {
                hashMapOf(
                    "year" to it.fatherDifference.years,
                    "month" to it.fatherDifference.months,
                    "day" to it.fatherDifference.days,
                    "hour" to it.fatherDifference.hours,
                    "minute" to it.fatherDifference.minutes,
                    "isDone" to it.localComplete
                )
            },
            "pospositionDaysLimit" to pospositionDaysLimit,
            "posposed" to posposed,
            "reminderType" to reminderType,
            "reminderDelay" to reminderDelay,
            "repeatType" to repeatType.toString(),
            "repeatDelay" to repeatDelay,
            "repeatLimit" to hashMapOf(
                "year" to (repeatLimit?.year ?: 0),
                "month" to (repeatLimit?.monthValue ?: -1 )+1,
                "day" to (repeatLimit?.dayOfMonth ?: 0),
                "hour" to (repeatLimit?.hour ?: 0),
                "minute" to (repeatLimit?.minute ?: 0)
            ),
            "sharedWith" to sharedWith
        )
}