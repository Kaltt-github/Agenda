package com.kaltt.agenda.classes

import com.kaltt.agenda.classes.enums.ScheduleType
import java.time.LocalDateTime

class Tag(
    var id: Int,
    val owner: String,
    var name: String,
    var description: String,
    var icon: String,
    var color: Double,
    var priority: Int,
    var isLazy: Boolean,
    var tasks: ArrayList<String>,
    var location: String,
    var isFullDay: Boolean,
    var length: Difference,
    var anticipations: ArrayList<Difference>,
    var pospositionLimit: Int,
    var reminderType: ScheduleType,
    var reminderDelay: Int,
    var repeatType: ScheduleType,
    var repeatDelay: Int,
    var repeatLimit: LocalDateTime?
) {
    companion object {
        fun empty(): Tag = Tag(
                -1,
                "",
                "Default",
                "Default Tag for events, provided by the app",
                "Icon",
                35.0,
                5,
                false,
                ArrayList(),
                "",
                true,
                Difference(days = 1),
                ArrayList(),
                1,
                ScheduleType.DONT,
                0,
                ScheduleType.DONT,
                0,
                null
            )
    }
    fun applyOn(e: Event) {
        e.icon = this.icon
        e.color = this.color
        e.priority = this.priority
        this.tasks.forEach { e.tasks.add(Task(e, it, false)) }
        e.location = this.location
        e.isFullDay = this.isFullDay
        e.end = this.length.applyOn(e.start)
        this.anticipations.forEach { e.addAnticipation(it) }
        e.posposition.daysLimit = this.pospositionLimit
        e.reminder.type = this.reminderType
        e.reminder.delay = this.reminderDelay
        e.setRepetitions(this.repeatType, this.repeatDelay, this.repeatLimit)

        e.isLazy = this.isLazy
    }
    fun clone(): Tag {
        return Tag(this.id, this.owner, this.name, this.description, this.icon, this.color, this.priority, this.isLazy, this.tasks, this.location, this.isFullDay, this.length, this.anticipations, this.pospositionLimit, this.reminderType, this.reminderDelay, this.repeatType, this.repeatDelay, this.repeatLimit)
    }
    fun save(eco: Boolean) {
        if (eco) {
            // TODO edit other events
        }
        // TODO
    }
    fun load() {
        // TODO
    }
}