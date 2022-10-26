package com.kaltt.agenda.classes

import com.kaltt.agenda.classes.enums.ScheduleType
import java.time.Duration
import java.time.LocalDateTime
import java.time.Period

class Difference(
    var years: Int = 0,
    var months: Int = 0,
    var days: Int = 0,
    var hours: Int = 0,
    var minutes: Int = 0
) {
    companion object {
        fun between(x: LocalDateTime, y: LocalDateTime): Difference {
            var min = if(x.isBefore(y)) x else y
            var max = if(x.isBefore(y)) y else x
            return Difference(
                years = max.year - min.year,
                months = max.monthValue - min.monthValue,
                days = max.dayOfMonth - min.dayOfMonth,
                hours = max.hour - min.hour,
                minutes = max.minute - min.minute
            )
        }
        fun by(type: ScheduleType, delay: Int): Difference{
            var x = Difference()
            when (type) {
                ScheduleType.YEARS ->  x.years = delay
                ScheduleType.MONTHS ->  x.months = delay
                ScheduleType.WEEKS ->  x.days = delay*7
                ScheduleType.DAYS ->  x.days = delay
                ScheduleType.HOURS ->  x.hours = delay
                ScheduleType.MINUTES ->  x.minutes = delay
                ScheduleType.DONT -> {}// nothing
            }
            return x
        }
    }
    fun applyOn(date: LocalDateTime, times: Int = 1): LocalDateTime = date
        .plusYears((this.years * times).toLong())
        .plusMonths((this.months * times).toLong())
        .plusDays((this.days * times).toLong())
        .plusHours((this.hours * times).toLong())
        .plusMinutes((this.minutes * times).toLong())
    fun isEmpty(): Boolean = years == 0 && months == 0 && days == 0 && hours == 0 && minutes == 0
    fun opposite(): Difference = Difference(
        years = this.years*-1,
        months = this.months*-1,
        days = this.days*-1,
        hours = this.hours*-1,
        minutes = this.minutes*-1
    )
    fun multiply(n: Int): Difference {
        this.years *= n
        this.months *= n
        this.days *= n
        this.hours *= n
        this.minutes *= n
        return this
    }
}