package com.kaltt.agenda.classes

import com.kaltt.agenda.classes.events.EventFather
import com.kaltt.agenda.classes.interfaces.Event

class Task(
    var father: Event,
    var description: String,
    var isDone: Boolean = false
) {
    companion object {
        fun from(f: EventFather, t: DataTask): Task = Task(f, t.dsecription, t.isDone)
    }
    val position: Int
    get() = this.father.tasks.indexOf(this)
}