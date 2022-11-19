package com.kaltt.agenda.classes.enums

enum class ScheduleType {
    DONT,
    MINUTES,
    HOURS,
    DAYS,
    WEEKS,
    MONTHS,
    YEARS;
    companion object {
        fun from(s: String): ScheduleType = when(s) {
            "MINUTES" -> MINUTES
            "HOURS" -> HOURS
            "DAYS" -> DAYS
            "WEEKS" -> WEEKS
            "MONTHS" -> MONTHS
            "YEARS" -> YEARS
            else -> DONT
        }
    }
    override fun toString(): String = when(this) {
        MINUTES -> "MINUTES"
        HOURS -> "HOURS"
        DAYS -> "DAYS"
        WEEKS -> "WEEKS"
        MONTHS -> "MONTHS"
        YEARS -> "YEARS"
        DONT -> "DONT"
    }
}