package com.kaltt.agenda.classes

import com.kaltt.agenda.classes.enums.EventType
import com.kaltt.agenda.classes.enums.ScheduleType
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class EventFather(override var tag: Tag = Tag.empty()) : Event {
    override val type: EventType = EventType.FATHER // SAVE ME VAR
    override var father: EventFather
    get() = this
    set(value) {}
    override var id: Int = -1 // SAVE ME VAR
    set(value) {
        if(field == -1) {
            field = value
        }
    }
    override var icon: String = "" // SAVE ME VAR
    override var name: String = "" // SAVE ME VAR
    override var description: String = "" // SAVE ME VAR

    override var color: Double = 0.0 // SAVE ME VAR
    override var priority: Int = 0 // SAVE ME VAR
    override var isLazy: Boolean = false // SAVE ME VAR
    set(value) {
        if(value) {
            this.isPosponable = false
            clearAnticipations()
            setReminders(ScheduleType.DONT, 0)
            setRepetitions(ScheduleType.DONT, 0, LocalDateTime.now())
        }
        field = value
    }
    override var tasks: ArrayList<Task> = ArrayList() // SAVE ME VAR
    set(value) {
        clearTasks()
        value.forEach { insertTask(it.description, -1) }
    }
    override var hasLocation: Boolean
    get() = this.location.isNotBlank()
    set(value) {
        this.location = if(value) "here" else ""
    }

    override var location: String = "" // SAVE ME VAR
    override var isCompleted: Boolean = false // SAVE ME VAR

    private var startDate: LocalDate = LocalDate.now() // SAVE ME VAR
    private var startTime: LocalTime = LocalTime.now() // SAVE ME VAR
    override var isFullDay: Boolean = false // SAVE ME VAR
    set(value) {
        if(value) {
            length.hours = 0
            length.minutes = 0
            length.days = Math.max(length.days, 1)
        }
        field = value
    }
    override var start: LocalDateTime
    get() = LocalDateTime.of(LocalDate.now(), if (this.isFullDay) LocalTime.of(0,0) else this.startTime)
    set(value) {
        this.startDate = value.toLocalDate()
        this.startTime = value.toLocalTime()
    }
    private var length: Difference = Difference() // SAVE ME VAR
    override var end: LocalDateTime
    get() = this.length.applyOn(this.start)
    set(value) {
        this.length = Difference.between(this.start, value)
    }
    override var posposition: EventPosposition? = null // SAVE ME VAR
    override var posponableLimit: Int = 0 // SAVE ME VAR
    override var hasAnticipations: Boolean
    get() = this.anticipations.size != 0
    set(value) {
        if (!value) {
            clearAnticipations()
        }
    }
    override var anticipations: ArrayList<EventAnticipation> = ArrayList() // SAVE ME VAR
    override var hasReminders: Boolean
    get() = this.reminderType != ScheduleType.DONT
    set(value) {
        if(value) {
            setReminders(ScheduleType.HOURS, 1)
        } else {
            setReminders(ScheduleType.DONT, 0)
        }
    }
    override var reminderType: ScheduleType = ScheduleType.DONT // SAVE ME VAR
    set(value) {
        setReminders(value, this.reminderDelay)
    }
    override var reminderDelay: Int = 0 // SAVE ME VAR
    set(value) {
        setReminders(this.reminderType, value)
    }
    override var reminders: ArrayList<EventReminder> = ArrayList() // SAVE ME VAR
    override var hasRepetitions: Boolean
    get() = this.repeatType != ScheduleType.DONT
    set(value) {
        if(value) {
            setRepetitions(ScheduleType.WEEKS, 1, Difference(months = 1).applyOn(LocalDateTime.now()))
        } else{
            setRepetitions(ScheduleType.DONT, 0, LocalDateTime.now())
        }
    }
    override var repeatType: ScheduleType = ScheduleType.DONT // SAVE ME VAR
    set(value) {
        setRepetitions(value, this.repeatDelay, this.repeatLimit)
    }
    override var repeatDelay: Int = 0 // SAVE ME VAR
    set(value) {
        setRepetitions(this.repeatType, value, this.repeatLimit)
    }
    private var hasLimit: Boolean = false // SAVE ME VAR
    override var repeatLimit: LocalDateTime = LocalDateTime.now() // SAVE ME VAR
    get() {
        return if(hasLimit) field else Difference(years = 10).applyOn(this.start)
    }
    set(value) {
        setRepetitions(this.repeatType, this.repeatDelay, value)
    }
    override var repeats: ArrayList<EventRepeat> = ArrayList() // SAVE ME VAR
    init {
        this.tag.applyOn(this)
    }

    override fun save() {
        // TODO
    }
    override fun reload() {
        // TODO
    }

    override fun insertTask(description: String, index: Int?) {
        var new = Task(this, description)
        if(index != null && index >= 0) {
            this.tasks[index] = new
        } else {
            this.tasks.add(new)
        }
    }
    override fun updateTask(index: Int, description: String?, completed: Boolean?) {
        var x = this.tasks[index]
        x.description = description ?: x.description
        x.isDone = completed ?: x.isDone
        this.tasks[index] = x
    }
    override fun removeTask(index: Int) {
        this.tasks.removeAt(index)
    }
    override fun clearTasks() {
        this.tasks.clear()
    }

    override fun insertAnticipation(start: LocalDateTime) {
        if (this.anticipations.find { it.start.isEqual(start) } != null) {
            return
        }
        this.anticipations.add(EventAnticipation(this, Difference.between(this.start, start), false))
        this.anticipations.sortBy { it.start }
    }
    override fun updateAnticipation(index: Int, start: LocalDateTime) {
        this.anticipations[index].start = start
        this.anticipations.sortBy { it.start }
    }
    override fun removeAnticipation(index: Int) {
        this.anticipations.removeAt(index)
    }
    override fun clearAnticipations() {
        this.anticipations.clear()
    }

    override fun setReminders(type: ScheduleType, delay: Int) {
        var diff = Difference.by(type, delay)
        var start = diff.applyOn(this.start)
        this.reminders.clear()
        while (start.isBefore(this.end)) {
            this.reminders.add(EventReminder(this, diff, false))
            start = diff.applyOn(start)
        }
    }
    override fun setRepetitions(type: ScheduleType, delay: Int, limit: LocalDateTime?) {
        var diff = Difference.by(type, delay)
        var start = diff.applyOn(this.start)
        this.repeats.clear()
        var lastDate = limit ?: Difference(years = 10).applyOn(this.start)
        while (start.isBefore(lastDate)) {
            this.repeats.add(EventRepeat(this, start, false))
            start = diff.applyOn(start)
        }
    }


}