package com.kaltt.agenda.classes.enums

enum class FromType {
    APP,
    OWNED,
    SHARED,
    GOOGLE;
    companion object {
        fun fromString(s: String): FromType  = when(s) {
            "OWNED" -> OWNED
            "SHARED" -> SHARED
            "GOOGLE" -> GOOGLE
            else -> APP
        }
    }
    override fun toString(): String = when(this) {
        APP -> "APP"
        GOOGLE -> "GOOGLE"
        OWNED -> "OWNED"
        SHARED -> "SHARED"
    }
}