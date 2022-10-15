package com.kaltt.agenda.classes

import com.kaltt.agenda.classes.enums.EventType
import com.kaltt.agenda.classes.enums.ScheduleType
import java.time.LocalDateTime

interface EventChild : Event {
    override val type: EventType
    var wasSeen: Boolean
    override var id: Int
        get() = this.father.id
        set(value) { this.father.id = value }
    override val father: Event
        get() = this.father
    override var icon: String
        get() = this.father.icon
        set(value) { this.father.icon = value }
    override var name: String
        get() = this.father.name
        set(value) { this.father.name = value }
    override var tag: Tag
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
    override var hasTasks: Boolean
        get() = super.hasTasks
        set(value) { this.father.hasTasks = value }
    override var tasks: ArrayList<Task>
        get() = this.father.tasks
        set(value) { this.father.tasks = value }
    override var hasLocation: Boolean
        get() = super.hasLocation
        set(value) { this.father.hasLocation = value }
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
    override var isPosponable: Boolean
        get() = this.father.isPosponable
        set(value) { this.father.isPosponable = value }
    override var posponableLimit: Int
        get() = this.father.posponableLimit
        set(value) { this.father.posponableLimit = value }
    override var hasAnticipations: Boolean
        get() = this.father.hasAnticipations
        set(value) { this.father.hasAnticipations = value }
    override var anticipations: ArrayList<EventAnticipation>
        get() = this.father.anticipations
        set(value) = Unit
    override var hasReminders: Boolean
        get() = this.father.hasReminders
        set(value) { this.father.hasReminders = value }
    override var reminderType: ScheduleType
        get() = this.father.reminderType
        set(value) { this.father.reminderType = value }
    override var reminderDelay: Int
        get() = this.father.reminderDelay
        set(value) { this.father.reminderDelay = value }
    override var reminders: ArrayList<EventReminder>
        get() = this.father.reminders
        set(value) = Unit
    override var hasRepetitions: Boolean
        get() = this.father.hasRepetitions
        set(value) { this.father.hasRepetitions = value }
    override var repeatType: ScheduleType
        get() = this.father.repeatType
        set(value) { this.father.repeatType = value }
    override var repeatDelay: Int
        get() = this.father.repeatDelay
        set(value) { this.father.repeatDelay = value }
    override var repeatLimit: LocalDateTime
        get() = this.father.repeatLimit
        set(value) { this.father.repeatLimit = value }
    override var repeats: ArrayList<EventRepeat>
        get() = this.father.repeats
        set(value) = Unit

    override fun save() {
        this.father.save()
    }

    override fun reload() {
        this.father.reload()
    }

    override fun insertTask(description: String, index: Int?) {
        this.father.insertTask(description, index)
    }

    override fun updateTask(index: Int, description: String?, completed: Boolean?) {
        this.father.updateTask(index, description, completed)
    }

    override fun removeTask(index: Int) {
        this.father.removeTask(index)
    }

    override fun clearTasks() {
        this.father.clearTasks()
    }

    override fun insertAnticipation(start: LocalDateTime) {
        this.father.insertAnticipation(start)
    }

    override fun updateAnticipation(index: Int, start: LocalDateTime) {
        this.father.updateAnticipation(index, start)
    }

    override fun removeAnticipation(index: Int) {
        this.father.removeAnticipation(index)
    }

    override fun clearAnticipations() {
        this.father.clearAnticipations()
    }

    override fun setReminders(type: ScheduleType, delay: Int) {
        this.father.setReminders(type, delay)
    }

    override fun setRepetitions(type: ScheduleType, delay: Int, limit: LocalDateTime?) {
        this.father.setRepetitions(type, delay, limit)
    }

    override var posposition: EventPosposition?
        get() = this.father.posposition
        set(value) { this.father.posposition = value }

    override fun canPospone(days: Int): Boolean {
        return this.father.canPospone(days)
    }

    override fun pospone(days: Int) {
        this.father.pospone(days)
    }
}