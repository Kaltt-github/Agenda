package com.kaltt.agenda.classes

import com.kaltt.agenda.classes.events.Event

class Task(
    var father: Event,
    var description: String,
    var isDone: Boolean = false
) {
    var position: Int
    get() = this.father.tasks.indexOf(this)
    set(value) {}
}