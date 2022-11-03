package com.kaltt.agenda.classes.events

import com.kaltt.agenda.classes.Difference
import com.kaltt.agenda.classes.tags.TagEagle
import com.kaltt.agenda.classes.Task
import com.kaltt.agenda.classes.enums.EventType
import com.kaltt.agenda.classes.enums.ScheduleType
import java.time.LocalDateTime

interface Event {
    // CONFIG
    val owner: String
    val eventType: EventType
    val father: Event
    // VALUES
    var id: Int
    var icon: String
    var name: String
    var tag: TagEagle
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
    fun anticipation(): EventAnticipation = this.anticipations[0]
    var posposition: EventPosposition
    var reminder: EventReminder
    var repetitions: ArrayList<EventRepeat>
    var repeatType: ScheduleType
    var repeatDelay: Int
    var repeatLimit: LocalDateTime?
    var sharedWith: ArrayList<String>
    fun repeat(): EventRepeat? = this.repetitions.getOrNull(0)

    fun isFather(): Boolean = this.eventType == EventType.FATHER
    fun isRepeat(): Boolean = this.eventType == EventType.REPEAT
    fun isPosposition(): Boolean = this.eventType == EventType.POSPOSITION
    fun isAnticipation(): Boolean = this.eventType == EventType.ANTICIPATION
    fun isReminder(): Boolean = this.eventType == EventType.REMINDER
    fun isPosponable(): Boolean = this.posposition.daysLimit != 0

    fun isExpired(): Boolean = this.end.isBefore(LocalDateTime.now()) && !this.isComplete
    fun isActive(): Boolean = (this.start.isBefore(LocalDateTime.now()) || this.start.isEqual(LocalDateTime.now())) && (this.end.isAfter(LocalDateTime.now()) || this.end.isEqual(LocalDateTime.now()))

    fun hasRepetitions(): Boolean = this.repeat()?.type != ScheduleType.DONT
    fun hasReminders(): Boolean = this.reminder.type != ScheduleType.DONT
    fun hasTasks(): Boolean = this.tasks.size != 0
    fun hasLocation(): Boolean = this.location.isNotBlank()
    fun hasAnticipations(): Boolean = this.anticipations.size != 0

    fun selfWithChildren(): ArrayList<Event> {
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
        if(this.hasRepetitions()){
            this.repetitions.forEach {
                e.addAll(it.selfWithChildren())
            }
        }
        return e
    }

    fun addAnticipation(date: LocalDateTime) {
        addAnticipation(Difference.between(date, this.start).opposite())
    }
    fun addAnticipation(diff: Difference) {
        EventAnticipation(this, diff)
        this.anticipations.sortBy { it.start }
    }
    fun setRepetitions(type: ScheduleType?, delay: Int?, limit: LocalDateTime?)

    fun isLastRepetition(): Boolean
    fun isLastAnticipation(): Boolean
    fun isLastReminder(): Boolean
    fun isLastPosposition(): Boolean

    fun pospone(days: Int) {
        this.posposition.lastSeen = this.posposition.lastSeen.plusDays(days.toLong())
    }
}