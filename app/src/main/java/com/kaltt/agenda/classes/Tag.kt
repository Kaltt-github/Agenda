package com.kaltt.agenda.classes

import com.kaltt.agenda.classes.enums.ScheduleType

class Tag(
    var id: Int = 0,
    var icon: String = "Calendar",
    var name: String = "Tag",
    var description: String = "",
    var color: Double = 31.0,
    var priority: Int = 5,
    var isLazy: Boolean = false,
    var tasks:  ArrayList<String> = ArrayList(),
    var location: String = "",
    var isFullDay: Boolean = false,
    var end: Difference = Difference(days = 1),
    var anticipations:  ArrayList<Difference> = ArrayList(),
    var reminderType: ScheduleType = ScheduleType.DONT,
    var reminderX: Int = 0,
    var repeatType: ScheduleType = ScheduleType.DONT,
    var repeatX: Int = 0,
    var repeatLimit: Boolean = true,
    var repeatLimitX: Difference = Difference(years = 5),
    var posponableLimit: Int = 7
) {
    companion object {
        fun empty(): Tag {
            return Tag(
                name = "Default",
                description = ""
            )
        }
    }
    fun applyOn(e: Event) {
        e.icon = this.icon
        e.color = this.color
        e.priority = this.priority
        e.location = this.location
        e.isFullDay = this.isFullDay
        e.end = this.end.applyOn(e.start)
        e.posponableLimit = this.posponableLimit

        this.tasks.forEach { e.insertTask(it, -1) }
        this.anticipations.forEach { e.insertAnticipation(it.applyOn(e.start)) }
        e.setReminders(this.reminderType, this.reminderX)
        e.setRepetitions(
            this.repeatType,
            this.repeatX,
            if(this.repeatLimit) this.repeatLimitX.applyOn(e.start) else null
        )

        e.isLazy = this.isLazy
    }
    fun clone(): Tag {
        return Tag(
            id = this.id,
            name = this.name,
            description = this.description,
            color = this.color,
            priority = this.priority,
            isLazy = this.isLazy,
            tasks = this.tasks,
            location = this.location,
            end = this.end,
            anticipations = this.anticipations,
            reminderType = this.repeatType,
            reminderX = this.reminderX,
            repeatType = this.repeatType,
            repeatX = this.repeatX,
            repeatLimit = this.repeatLimit,
            repeatLimitX = this.repeatLimitX,
            posponableLimit = this.posponableLimit
        )
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