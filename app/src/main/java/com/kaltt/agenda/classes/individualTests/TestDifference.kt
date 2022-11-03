package com.kaltt.agenda.classes.individualTests

import com.kaltt.agenda.classes.Difference
import com.kaltt.agenda.classes.enums.ScheduleType
import java.time.*

class TestDifference {
}

fun show(d: Difference) {
    println("${d.years} ${d.months} ${d.days} ${d.hours} ${d.minutes}")
}
fun show(d: LocalDateTime) {
    println("${d.year}/${d.month+1}/${d.dayOfMonth} ${d.hour}:${d.minute}")
}

fun main (arguments: Array<String>) {
    println("Probando clase DIFFERENCE")
    // CONSTRUCTORS
    var a = LocalDateTime.of(LocalDate.of(2022,5,14), LocalTime.of(17, 30))
    var c = LocalDateTime.of(LocalDate.of(2022,1,1), LocalTime.of(2, 2))
    var b = LocalDateTime.of(LocalDate.of(2021,3,4), LocalTime.of(1, 12))
    var x = Difference.between(a, b)
    var z = Difference.between(b, c)
    println(" Y M D H M")
    // 1 2 10 16 18
    show(x)
    // ??
    show(z)
    var y = Difference.by(ScheduleType.WEEKS,10)
    // 0 0 70
    show(y)
    // FUNCIONES
    // apply
    var w = x.applyOn(b)
    show(w)
    show(a)
    // multiply
    // opposite
    var q = x.opposite().applyOn(a)
    show(q)
    show(b)
    var e = Difference(days = -1).applyOn(a)
    var r = Difference(days = 1).opposite().applyOn(a)
    show(a)
    show(e)
    show(r)
    // empty
    println(Difference().isEmpty())
    var o = Difference.between(LocalDateTime.now(),LocalDateTime.now())
    println(o.isEmpty())
    println(x.isEmpty())
    println(z.isEmpty())
    println("apply 1 day")
    var p = LocalDateTime.of(LocalDate.now(), LocalTime.of(0,0))
    println(p)
    p = Difference(days = 1).applyOn(p)
    println(p)
}