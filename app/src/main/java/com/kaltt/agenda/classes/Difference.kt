package com.kaltt.agenda.classes

import com.kaltt.agenda.classes.enums.ScheduleType
import java.time.LocalDateTime

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
            max.minusMinutes(min.minute.toLong())
            max.minusHours(min.hour.toLong())
            max.minusDays(min.dayOfMonth.toLong())
            max.minusMonths(min.monthValue.toLong())
            max.minusYears(min.year.toLong())
            return Difference(
                years = max.year,
                months = max.monthValue,
                days = max.dayOfMonth,
                hours = max.hour,
                minutes = max.minute
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
    fun applyOn(date: LocalDateTime, times: Int = 1): LocalDateTime {
        var x = date
        if (this.years > 0) {
            x.plusYears((this.years * times).toLong())
        } else if (this.years < 0) {
            x.minusYears((this.years * times).toLong())
        }
        if (this.months > 0) {
            x.plusMonths((this.months * times).toLong())
        } else if (this.months < 0) {
            x.minusMonths((this.months * times).toLong())
        }
        if (this.days > 0) {
            x.plusDays((this.days * times).toLong())
        } else if (this.days < 0) {
            x.minusDays((this.days * times).toLong())
        }
        if (this.hours > 0) {
            x.plusHours((this.hours * times).toLong())
        } else if (this.hours < 0) {
            x.minusHours((this.hours * times).toLong())
        }
        if (this.minutes > 0) {
            x.plusMinutes((this.minutes * times).toLong())
        } else if (this.minutes < 0) {
            x.minusMinutes((this.minutes * times).toLong())
        }
        return x
    }
}