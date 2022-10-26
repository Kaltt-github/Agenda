package com.kaltt.agenda.classes.individualTests

import com.kaltt.agenda.classes.Task

class TestTask {
}

fun main (arguments: Array<String>) {
    println("Probando clase TASK")
    var x = FakeEvent()
    x.tasks.add(Task(x, "Task 1!", false))
    x.tasks.add(Task(x, "Task 2!", false))
    x.tasks.add(Task(x, "Task 3!", false))
    x.tasks.add(Task(x, "Task 4!", false))
    x.tasks.add(Task(x, "Task 5!", false))
    x.tasks.add(Task(x, "Task 6!", false))
    x.tasks.forEach {
        println(it.position)
    }
}