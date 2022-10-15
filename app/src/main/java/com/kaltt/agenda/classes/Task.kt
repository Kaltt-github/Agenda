package com.kaltt.agenda.classes

class Task(
    var father: Event,
    var description: String,
    var isDone: Boolean = false
) {
    var position: Int = 0
    fun setDescription(description: String = this.description): Task {
        this.description = description
        return this
    }

    fun setIsDone(isDone: Boolean = this.isDone): Task {
        this.isDone = isDone
        return this
    }

    fun setPosition(position: Int = this.position): Task {
        this.position = position
        return this
    }
}