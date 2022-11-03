package com.kaltt.agenda.classes.individualTests

import com.kaltt.agenda.classes.*
import com.kaltt.agenda.classes.enums.EventType
import com.kaltt.agenda.classes.enums.ScheduleType
import com.kaltt.agenda.classes.events.Event
import com.kaltt.agenda.classes.events.EventFather
import java.time.LocalDateTime

class TestEvent {
}

fun enum(t: EventType): String {
    var s = when(t) {
        EventType.FATHER ->         "Padre"
        EventType.POSPOSITION ->    "Posposicion"
        EventType.ANTICIPATION ->   "Antisipacion"
        EventType.REPEAT ->         "Repeticion"
        EventType.REMINDER ->       "Recordatorio"
    }
    return s
}
fun dateToString(d: LocalDateTime): String = "${d.year}.${d.monthValue+1}.${d.dayOfMonth} ${d.hour}:${d.minute}"

fun show(e: Event) {
    println(
        arrayListOf(
            e.owner,
            e.eventType.toString(),
            e.father.name,
            e.id,
            e.icon,
            e.name,
            e.tag.name,
            e.color.toString(),
            e.priority.toString(),
            e.isLazy.toString(),
            e.tasks.map { "${it.position}-${it.description}-${it.isDone}" }.joinToString("/"),
            e.location,
            e.isComplete.toString(),
            e.isFullDay.toString(),
            dateToString(e.start),
            dateToString(e.end),
            e.anticipations.size.toString(),
            e.posposition.daysLimit.toString(),
            "${e.reminder.type}|${e.reminder.delay}",
            e.repetitions.size.toString(),
            "${e.repeatDelay}|${e.repeatType}|${e.repeatLimit}",
            "${e.isFather()}|${e.isRepeat()}|${e.isPosposition()}|${e.isAnticipation()}|${e.isReminder()}",
            e.isExpired(),
            e.isActive(),
            "${ e.isLastRepetition() }|${ e.isLastAnticipation() }|${ e.isLastReminder() }|${ e.isLastPosposition() }",
        ).joinToString("|")
    )
}

fun main (arguments: Array<String>) {
    println("Probando clase EVENT")
    var q = EventFather("emasileo@gmail.com")
    // FATHER
    println("Evento independiente")
    q.selfWithChildren().forEach {
        show(it)
    }
    // ANTICIPATION
    println("Evento anticipado")
    var r = EventFather("ema3")
    r.addAnticipation(Difference(days = 1))
    r.addAnticipation(Difference(hours = 1))
    r.addAnticipation(Difference(days = 2))
    r.start = r.start.minusDays(3)
    r.selfWithChildren().forEach {
        show(it)
    }
    // POSPOSITION
    println("Evento pospuesto")
    var e = EventFather("ema2")
    e.start = e.start.minusDays(2)
    e.posposition.daysLimit = 3
    e.selfWithChildren().forEach {
        show(it)
    }
    // REMINDER
    println("Evento recordatorio")
    var w = EventFather("ema")
    w.reminder.type = ScheduleType.MINUTES
    w.reminder.delay = 50
    w.selfWithChildren().forEach {
        show(it)
    }
    // REPETITION
    println("EVENTO REPETIDO")
    var t = EventFather("reptition")
    t.setRepetitions(ScheduleType.WEEKS, 1, LocalDateTime.now().plusMonths(1L))
    t.selfWithChildren().forEach {
        show(it)
    }
    println("Hecho")
}
