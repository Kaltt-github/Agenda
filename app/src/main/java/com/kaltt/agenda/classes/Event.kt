package com.kaltt.agenda.classes

import com.kaltt.agenda.classes.enums.EventType
import com.kaltt.agenda.classes.enums.ScheduleType
import java.time.LocalDateTime

interface Event {
    // IMPORTANT INFO
    val type: EventType
    var id: Int
    val father: Event
    // DEFAULT INFO
    var isFather: Boolean
        get() = this.type == EventType.FATHER
        set(value) {}
    var icon: String
    var name: String
    var tag: Tag
    var description: String
    var color: Double
    var priority: Int
    var isLazy: Boolean
    // LAZY SAFETY
    var hasTasks: Boolean
        get() = this.tasks.size != 0
        set(value) {
            if(!value) {
                clearTasks()
            }
        }
    var tasks: ArrayList<Task>
    var hasLocation: Boolean
        get() = this.location.isNotBlank()
        set(value) {
            this.location = if(value) "here" else ""
        }
    var location: String
    var isCompleted: Boolean

    // NOT LAZY AFTER THIS
    var isFullDay: Boolean
    var start: LocalDateTime
    var end: LocalDateTime
    var isExpired: Boolean
        get() = this.end.isBefore(LocalDateTime.now()) && !this.isCompleted
        set(value) {}
    var isActive: Boolean
        get() = (this.start.isBefore(LocalDateTime.now()) || this.start.isEqual(LocalDateTime.now())) && (this.end.isAfter(LocalDateTime.now()) || this.end.isEqual(LocalDateTime.now()))
        set(value) {}
    // IS POSPONABLE
    var isPosponable: Boolean
        get() = posponableLimit != 0
        set(value) {
            if(value) {
                if(posponableLimit == 0) {
                    posponableLimit = 7
                }
            } else {
                posponableLimit = 0
            }
        }
    var isPosposition: Boolean
        get() = this.type == EventType.POSPOSITION
        set(value) {}
    var posponableLimit: Int
    var posposition: EventPosposition?

    // ANTICIPATIONS BEFORE EVENT
    var hasAnticipations: Boolean
    var anticipations: ArrayList<EventAnticipation>
    var isAnticipation: Boolean
        get() = this.type == EventType.ANTICIPATION
        set(value) {}
    var isLastAnticipation: Boolean
        get() = false
        set(value) {}

    // REMINDERS DURING EVENT
    var hasReminders: Boolean
    var reminderType: ScheduleType
    var reminderDelay: Int
    var reminders: ArrayList<EventReminder>
    var isReminder: Boolean
        get() = this.type == EventType.REMINDER
        set(value) {}
    var isLastReminder: Boolean
        get() = false
        set(value) {}

    // REPETITIONS AFTER EVENT
    var hasRepetitions: Boolean
    var repeatType: ScheduleType
    var repeatDelay: Int
    var repeatLimit: LocalDateTime
    var repeats: ArrayList<EventRepeat>
    var isRepetition: Boolean
        get() = this.type == EventType.REPEAT
        set(value) {}
    var isLastRepetition: Boolean
        get() = false
        set(value) {}
    // FUNCTIONS
    fun save()
    fun reload()

    fun insertTask(description: String, index: Int?)
    fun updateTask(index: Int, description: String?, completed: Boolean?)
    fun removeTask(index: Int)
    fun clearTasks()

    fun insertAnticipation(start: LocalDateTime)
    fun updateAnticipation(index: Int, start: LocalDateTime)
    fun removeAnticipation(index: Int)
    fun clearAnticipations()

    fun setReminders(type: ScheduleType, delay: Int)
    fun setRepetitions(type: ScheduleType, delay: Int, limit: LocalDateTime?)

    fun canPospone(days: Int): Boolean {
        return this.posponableLimit <= days
    }
    fun pospone(days: Int) {
        if(canPospone(days)) {
            this.posposition = EventPosposition(this, days)
        }
    }
}