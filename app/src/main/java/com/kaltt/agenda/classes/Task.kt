package com.kaltt.agenda.classes

import com.kaltt.agenda.classes.interfaces.Event

class Task(
    var father: Event,
    var description: String,
    var isDone: Boolean = false
) {
    val position: Int
    get() = this.father.tasks.indexOf(this)
}