package com.kaltt.agenda.classes

import com.kaltt.agenda.classes.enums.FromType
import com.kaltt.agenda.classes.enums.ScheduleType
import com.kaltt.agenda.classes.interfaces.Event
import com.kaltt.agenda.classes.interfaces.Persistent
import java.time.LocalDateTime

class Tag(
    val from: FromType,
    override var id: String,
    var name: String,
    val owner: String,
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
    var repeatLimit: LocalDateTime?,
    var sharedWith: ArrayList<String>
): Persistent {
    companion object {
        fun empty(): Tag = Tag(
                FromType.APP,
                "",
                "Default",
                "App",
                "Default tag for events, provided by the app",
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
                null,
                ArrayList()
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
        e.pospositionDaysLimit = this.pospositionLimit
        e.setReminders(this.reminderType, this.reminderDelay)
        e.setRepetitions(this.repeatType, this.repeatDelay, this.repeatLimit)
        e.sharedWith.addAll(this.sharedWith)
        e.sharedWith.distinct()
        e.isLazy = this.isLazy
    }
    fun clone(): Tag {
        return Tag(this.from, this.id, this.owner, this.name, this.description, this.icon, this.color, this.priority, this.isLazy, this.tasks, this.location, this.isFullDay, this.length, this.anticipations, this.pospositionLimit, this.reminderType, this.reminderDelay, this.repeatType, this.repeatDelay, this.repeatLimit, this.sharedWith)
    }
}