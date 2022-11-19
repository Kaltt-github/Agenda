package com.kaltt.agenda.classes.interfaces

import com.kaltt.agenda.classes.DataEventFather
import com.kaltt.agenda.classes.Difference
import com.kaltt.agenda.classes.Tag
import com.kaltt.agenda.classes.Task
import com.kaltt.agenda.classes.enums.EventType
import com.kaltt.agenda.classes.enums.FromType
import com.kaltt.agenda.classes.enums.ScheduleType
import com.kaltt.agenda.classes.events.EventAnticipation
import com.kaltt.agenda.classes.events.EventPosposition
import com.kaltt.agenda.classes.events.EventReminder
import com.kaltt.agenda.classes.events.EventRepeat
import java.time.LocalDateTime

interface Event: Persistent {
    // CONFIG
    val owner: String
    val eventType: EventType
    val father: Event
    val from: FromType
    // VALUES
    var icon: String
    var name: String
    var tag: Tag
    var description: String
    var color: Double
    var priority: Int
    var isLazy: Boolean
    var tasks: ArrayList<Task>
    var location: String
    var isComplete: Boolean
    var isFullDay: Boolean
    var start: LocalDateTime
    var end: LocalDateTime

    var anticipations: ArrayList<EventAnticipation>
    fun addAnticipation(date: LocalDateTime)
    fun addAnticipation(diff: Difference)

    var posposition: EventPosposition
    var pospositionDaysLimit: Int
    var posposed: Int
    fun pospositionDateLimit() = Difference(days = this.pospositionDaysLimit).applyOn(this.start)
    fun pospose(days: Int): Boolean
    fun posposeDaysLeft(): Int

    var reminders: ArrayList<EventReminder>
    var reminderType: ScheduleType
    var reminderDelay: Int
    fun setReminders(type: ScheduleType = this.reminderType, delay: Int = this.reminderDelay)

    var repeats: ArrayList<EventRepeat>
    var repeatType: ScheduleType
    var repeatDelay: Int
    var repeatLimit: LocalDateTime?
    fun setRepetitions(type: ScheduleType, delay: Int, limit: LocalDateTime?)

    var sharedWith: ArrayList<String>

    fun isFather(): Boolean = this.eventType == EventType.FATHER
    fun isRepeat(): Boolean = this.eventType == EventType.REPEAT
    fun isPosposition(): Boolean = this.eventType == EventType.POSPOSITION
    fun isAnticipation(): Boolean = this.eventType == EventType.ANTICIPATION
    fun isReminder(): Boolean = this.eventType == EventType.REMINDER

    fun isExpired(): Boolean = this.end.isBefore(LocalDateTime.now()) && !this.isComplete
    fun isActive(): Boolean = (this.start.isBefore(LocalDateTime.now()) || this.start.isEqual(LocalDateTime.now())) && (this.end.isAfter(LocalDateTime.now()) || this.end.isEqual(LocalDateTime.now()))

    fun isPosponable(): Boolean = this.pospositionDaysLimit != 0
    fun hasRepetitions(): Boolean = this.repeatType != ScheduleType.DONT || this.repeatDelay != 0
    fun hasReminders(): Boolean = this.reminderType != ScheduleType.DONT || this.reminderDelay != 0
    fun hasTasks(): Boolean = this.tasks.size != 0
    fun hasLocation(): Boolean = this.location.isNotBlank()
    fun hasAnticipations(): Boolean = this.anticipations.size != 0

    fun selfWithChildren(): ArrayList<Event>

    fun isLastRepetition(): Boolean = false
    fun isLastAnticipation(): Boolean = false
    fun isLastReminder(): Boolean = false
    fun isLastPosposition(): Boolean = false

    fun toData(): DataEventFather
    fun toMap(): Map<String, Any>
}