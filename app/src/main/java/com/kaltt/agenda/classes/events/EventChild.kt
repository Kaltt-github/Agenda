package com.kaltt.agenda.classes.events

import com.kaltt.agenda.classes.Difference
import com.kaltt.agenda.classes.tags.TagEagle
import com.kaltt.agenda.classes.Task
import com.kaltt.agenda.classes.enums.ScheduleType
import java.time.LocalDateTime

interface EventChild : Event {
    var lastSeen: LocalDateTime
    override var id: Int
        get() = this.father.id
        set(value) { this.father.id = value }
    override val owner: String
        get() = this.father.owner
    override var icon: String
        get() = this.father.icon
        set(value) { this.father.icon = value }
    override var name: String
        get() = this.father.name
        set(value) { this.father.name = value }
    override var tag: TagEagle
        get() = this.father.tag
        set(value) { this.father.tag = value }
    override var description: String
        get() = this.father.description
        set(value) { this.father.description = value }
    override var color: Double
        get() = this.father.color
        set(value) { this.father.color = value }
    override var priority: Int
        get() = this.father.priority
        set(value) { this.father.priority = value }
    override var isLazy: Boolean
        get() = this.father.isLazy
        set(value) { this.father.isLazy = value }
    override var tasks: ArrayList<Task>
        get() = this.father.tasks
        set(value) { this.father.tasks = value }
    override var location: String
        get() = this.father.location
        set(value) { this.father.location = value }
    override var isFullDay: Boolean
        get() = this.father.isFullDay
        set(value) { this.father.isFullDay = value }
    override var start: LocalDateTime
        get() = this.father.start
        set(value) { this.father.start = value }
    override var end: LocalDateTime
        get() = this.father.end
        set(value) { this.father.end = value }
    override var posposition: EventPosposition
        get() = this.father.posposition
        set(value) { this.father.posposition = value }
    override var repeatType: ScheduleType
        get() = this.father.repeatType
        set(value) { this.father.repeatType = value }
    override var repeatDelay: Int
        get() = this.father.repeatDelay
        set(value) { this.father.repeatDelay = value }
    override var repeatLimit: LocalDateTime?
        get() = this.father.repeatLimit
        set(value) { this.father.repeatLimit = value }
    override var isComplete: Boolean
        get() = this.father.isComplete || this.end.isBefore(this.lastSeen)
        set(value) { this.lastSeen = LocalDateTime.now() }
    override var anticipations: ArrayList<EventAnticipation>
        get() = this.father.anticipations
        set(value) { this.father.anticipations = value }
    override var reminder: EventReminder
        get() = this.father.reminder
        set(value) { this.father.reminder = value }
    override var repetitions: ArrayList<EventRepeat>
        get() = this.father.repetitions
        set(value) { this.father.repetitions = value }
    override var sharedWith: ArrayList<String>
        get() = this.father.sharedWith
        set(value) { this.father.sharedWith = value }
    override fun anticipation(): EventAnticipation = this.father.anticipation()
    override fun repeat(): EventRepeat? = this.father.repeat()
    override fun isPosponable(): Boolean = this.father.isPosponable()
    override fun addAnticipation(diff: Difference) = this.father.addAnticipation(diff)
    override fun pospone(days: Int) = this.father.pospone(days)
    override fun hasRepetitions(): Boolean = this.father.hasRepetitions()
    override fun hasReminders(): Boolean = this.father.hasReminders()
    override fun hasLocation(): Boolean = this.father.hasLocation()
    override fun hasTasks(): Boolean = this.father.hasTasks()
    override fun hasAnticipations(): Boolean = this.father.hasAnticipations()
    override fun addAnticipation(date: LocalDateTime) = this.father.addAnticipation(date)
    override fun selfWithChildren(): ArrayList<Event> = this.father.selfWithChildren()
    override fun setRepetitions(type: ScheduleType?, delay: Int?, limit: LocalDateTime?) = this.father.setRepetitions(type, delay, limit)
}