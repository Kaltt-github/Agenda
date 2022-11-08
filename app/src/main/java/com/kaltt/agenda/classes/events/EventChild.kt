package com.kaltt.agenda.classes.events

import com.kaltt.agenda.classes.Difference
import com.kaltt.agenda.classes.Tag
import com.kaltt.agenda.classes.Task
import com.kaltt.agenda.classes.enums.FromType
import com.kaltt.agenda.classes.enums.ScheduleType
import java.time.LocalDateTime

interface EventChild : Event {
    var position: Int
    var fatherDifference: Difference
    var localComplete: Boolean
    override var start: LocalDateTime
        get() = this.fatherDifference.applyOn(this.father.start)
        set(value) {
            this.fatherDifference = Difference.between(value, this.father.start).opposite()
        }

    override var isComplete: Boolean
        get() = this.localComplete || this.father.isComplete
        set(value) {
            this.localComplete = value
        }

    override val from: FromType
        get() = this.father.from
    override var id: String
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
    override var tasks: ArrayList<Task>
        get() = this.father.tasks
        set(value) { this.father.tasks = value }
    override var location: String
        get() = this.father.location
        set(value) { this.father.location = value }
    override var isFullDay: Boolean
        get() = this.father.isFullDay
        set(value) { this.father.isFullDay = value }
    override var posposed: Int
        get() = this.father.posposed
        set(value) { this.father.posposed = value }
    override fun pospose(days: Int): Boolean = this.father.pospose(days)
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
    override var anticipations: ArrayList<EventAnticipation>
        get() = this.father.anticipations
        set(value) { this.father.anticipations = value }
    override var reminders: ArrayList<EventReminder>
        get() = this.father.reminders
        set(value) { this.father.reminders = value }
    override var repeats: ArrayList<EventRepeat>
        get() = this.father.repeats
        set(value) { this.father.repeats = value }
    override var sharedWith: ArrayList<String>
        get() = this.father.sharedWith
        set(value) { this.father.sharedWith = value }
    override var pospositionDaysLimit: Int
        get() = this.father.pospositionDaysLimit
        set(value) { this.father.pospositionDaysLimit = value }
    override var reminderType: ScheduleType
        get() = this.father.reminderType
        set(value) { this.father.reminderType = value }
    override var reminderDelay: Int
        get() = this.father.reminderDelay
        set(value) { this.father.reminderDelay = value }
    override fun setReminders(type: ScheduleType, delay: Int) = this.father.setReminders(type, delay)
    override fun isPosponable(): Boolean = this.father.isPosponable()
    override fun addAnticipation(diff: Difference) = this.father.addAnticipation(diff)
    override fun hasRepetitions(): Boolean = this.father.hasRepetitions()
    override fun hasReminders(): Boolean = this.father.hasReminders()
    override fun hasLocation(): Boolean = this.father.hasLocation()
    override fun hasTasks(): Boolean = this.father.hasTasks()
    override fun posposeDaysLeft(): Int = this.father.posposeDaysLeft()
    override fun hasAnticipations(): Boolean = this.father.hasAnticipations()
    override fun addAnticipation(date: LocalDateTime) = this.father.addAnticipation(date)
    override fun selfWithChildren(): ArrayList<Event> = this.father.selfWithChildren()
    override fun setRepetitions(type: ScheduleType, delay: Int, limit: LocalDateTime?) = this.father.setRepetitions(type, delay, limit)
    override fun isLastRepetition(): Boolean = isRepeat() && this.position + 1 == this.repeats.size
    override fun isLastAnticipation(): Boolean = isAnticipation() && this.position + 1 == this.anticipations.size
    override fun isLastReminder(): Boolean = isReminder() && this.position + 1 == this.reminders.size
    override fun isLastPosposition(): Boolean = isPosposition() && this.father.posposed == this.father.pospositionDaysLimit
}