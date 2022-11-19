package com.kaltt.agenda.classes.enums

enum class EventType {
    FATHER,
    ANTICIPATION,
    REMINDER,
    REPEAT,
    POSPOSITION;
    companion object {
        fun fromString(s: String): EventType = when(s) {
            "ANTICIPATION" -> ANTICIPATION
            "POSPOSITION" -> POSPOSITION
            "REMINDER" -> REMINDER
            "REPEAT" -> REPEAT
            else -> FATHER
        }
    }
    override fun toString(): String = when(this) {
        ANTICIPATION -> "ANTICIPATION"
        POSPOSITION -> "POSPOSITION"
        REPEAT -> "REPEAT"
        REMINDER -> "REMINDER"
        FATHER -> "FATHER"
    }
}