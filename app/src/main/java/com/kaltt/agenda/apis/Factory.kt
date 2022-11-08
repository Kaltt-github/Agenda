package com.kaltt.agenda.apis

import android.text.BoringLayout
import com.kaltt.agenda.V
import com.kaltt.agenda.classes.*
import com.kaltt.agenda.classes.enums.*
import com.kaltt.agenda.classes.events.*
import java.time.LocalDateTime

class Factory {
    companion object {
        fun stringToEnumEvent(s: String): EventType  = when(s) {
            "ANTICIPATION" -> EventType.ANTICIPATION
            "POSPOSITION" -> EventType.POSPOSITION
            "REMINDER" -> EventType.REMINDER
            "REPEAT" -> EventType.REPEAT
            else -> EventType.FATHER
        }
        fun stringToEnumFrom(s: String): FromType  = when(s) {
            "OWNED" -> FromType.OWNED
            "SHARED" -> FromType.SHARED
            "GOOGLE" -> FromType.GOOGLE
            else -> FromType.APP
        }
        fun stringToEnumSchedule(s: String): ScheduleType = when(s) {
            "MINUTES" -> ScheduleType.MINUTES
            "HOURS" -> ScheduleType.HOURS
            "DAYS" -> ScheduleType.DAYS
            "WEEKS" -> ScheduleType.WEEKS
            "MONTHS" -> ScheduleType.MONTHS
            "YEARS" -> ScheduleType.YEARS
            else -> ScheduleType.DONT
        }

        fun enumToString(e: FromType): String = when(e) {
            FromType.APP -> "APP"
            FromType.GOOGLE -> "GOOGLE"
            FromType.OWNED -> "OWNED"
            FromType.SHARED -> "SHARED"
        }
        fun enumToString(e: EventType): String = when(e) {
            EventType.ANTICIPATION -> "ANTICIPATION"
            EventType.POSPOSITION -> "POSPOSITION"
            EventType.REPEAT -> "REPEAT"
            EventType.REMINDER -> "REMINDER"
            EventType.FATHER -> "FATHER"
        }
        fun enumToString(e: ScheduleType): String = when(e) {
            ScheduleType.MINUTES -> "MINUTES"
            ScheduleType.HOURS -> "HOURS"
            ScheduleType.DAYS -> "DAYS"
            ScheduleType.WEEKS -> "WEEKS"
            ScheduleType.MONTHS -> "MONTHS"
            ScheduleType.YEARS -> "YEARS"
            ScheduleType.DONT -> "DONT"
        }

        fun mapToEvent(from: FromType, map: Map<String, Any>): EventFather {
            val e = EventFather(from, map["owner"]!! as String)
            e.name = map["name"]!! as String
            e.description = map["description"]!! as String
            e.tag = V.allTags.find { it.id == map["tag"]!! as String } ?: Tag.empty()
            e.id = map["id"]!! as String
            e.icon = map["icon"]!! as String
            e.color = (map["color"]!! as Long).toDouble()
            e.priority = (map["priority"]!! as Long).toInt()
            e.tasks.clear()
            val tasks = map["tasks"]!! as ArrayList<Map<String, Any>>
            tasks.forEach {
                e.tasks.add(
                    Task(
                        e,
                        it["description"]!! as String,
                        it["isDone"]!! as Boolean
                    )
                )
            }
            e.tasks.sortBy { it.position }
            e.location = map["location"]!! as String
            e.isComplete = map["isComplete"]!! as Boolean
            e.isFullDay = map["isFullDay"]!! as Boolean
            val start = map["start"]!! as Map<String, Long>
            e.start = LocalDateTime.of(
                start["year"]!!.toInt(),
                start["month"]!!.toInt(),
                start["day"]!!.toInt(),
                start["hour"]!!.toInt(),
                start["minute"]!!.toInt()
            )
            val end = map["end"]!! as Map<String, Long>
            e.end = LocalDateTime.of(
                end["year"]!!.toInt(),
                end["month"]!!.toInt(),
                end["day"]!!.toInt(),
                end["hour"]!!.toInt(),
                end["minute"]!!.toInt()
            )
            val anticipations = map["anticipations"] as ArrayList<Map<String, Long>>
            anticipations.forEach {
                e.addAnticipation(
                    Difference(
                        it["year"]!!.toInt(),
                        it["month"]!!.toInt(),
                        it["day"]!!.toInt(),
                        it["hour"]!!.toInt(),
                        it["minute"]!!.toInt()
                    )
                )
            }
            e.pospositionDaysLimit = (map["pospositionDaysLimit"]!! as Long).toInt()
            e.posposed = (map["posposed"] as Long).toInt()
            e.setReminders(
                stringToEnumSchedule(map["reminderType"]!! as String),
                (map["reminderDelay"]!! as Long).toInt()
            )
            val repeatLimit = map["repeatLimit"] as Map<String, Long>
            var limitDate: LocalDateTime? = null
            if(repeatLimit["empty"]!! != 1L) {
                limitDate = LocalDateTime.of(
                    repeatLimit["year"]!!.toInt(),
                    repeatLimit["month"]!!.toInt(),
                    repeatLimit["day"]!!.toInt(),
                    repeatLimit["hour"]!!.toInt(),
                    repeatLimit["minute"]!!.toInt()
                )
            }
            e.setRepetitions(
                stringToEnumSchedule(map["repeatType"]!! as String),
                (map["repeatDelay"]!! as Long).toInt(),
                limitDate
            )
            e.sharedWith.clear()
            val shared = map["sharedWith"]!! as ArrayList<String>
            e.sharedWith.addAll(shared)
            e.isLazy = map["isLazy"]!! as Boolean
            return e;
        }
        fun mapsToEvents(from: FromType, maps: List<Map<String, Any>?>): ArrayList<EventFather> {
            val result = ArrayList<EventFather>()
            result.addAll(maps.filterNotNull().map { mapToEvent(from, it) })
            return result
        }

        fun eventToMap(e: EventFather): Map<String, Any> {
            return hashMapOf(
                "owner" to e.owner,
                "id" to e.id,
                "name" to e.name,
                "description" to e.description,
                "tag" to e.tag.id,
                "icon" to e.icon,
                "color" to e.color,
                "priority" to e.priority,
                "isLazy" to e.isLazy,
                "tasks" to e.tasks.map {
                    hashMapOf(
                        "description" to it.description,
                        "isDone" to it.isDone
                    )
                },
                "location" to e.location,
                "isComplete" to e.isComplete,
                "isFullDay" to e.isFullDay,
                "start" to hashMapOf(
                    "year" to e.start.year,
                    "month" to e.start.monthValue+1,
                    "day" to e.start.dayOfMonth,
                    "hour" to e.start.hour,
                    "minute" to e.start.minute
                ),
                "end" to hashMapOf(
                    "year" to e.end.year,
                    "month" to e.end.monthValue+1,
                    "day" to e.end.dayOfMonth,
                    "hour" to e.end.hour,
                    "minute" to e.end.minute,
                ),
                "anticipations" to e.anticipations.map {
                    hashMapOf(
                        "year" to it.fatherDifference.years,
                        "month" to it.fatherDifference.months,
                        "day" to it.fatherDifference.days,
                        "hour" to it.fatherDifference.hours,
                        "minute" to it.fatherDifference.minutes,
                        "isDone" to it.localComplete
                    )
                },
                "pospositionDaysLimit" to e.pospositionDaysLimit,
                "posposed" to e.posposed,
                "reminderType" to e.reminderType,
                "reminderDelay" to e.reminderDelay,
                "repeatType" to enumToString(e.repeatType),
                "repeatDelay" to e.repeatDelay,
                "repeatLimit" to if(e.repeatLimit != null) {
                    hashMapOf(
                        "year" to e.repeatLimit!!.year,
                        "month" to e.repeatLimit!!.monthValue+1,
                        "day" to e.repeatLimit!!.dayOfMonth,
                        "hour" to e.repeatLimit!!.hour,
                        "minute" to e.repeatLimit!!.minute,
                        "empty" to 0
                    )
                } else { hashMapOf( "empty" to 1) },
                "sharedWith" to e.sharedWith
            )
        }
        fun eventsToMaps(es: ArrayList<EventFather>): ArrayList<Map<String, Any>> {
            val result = ArrayList<Map<String, Any>>()
            result.addAll(es.map { eventToMap(it) })
            return result
        }
    }
}