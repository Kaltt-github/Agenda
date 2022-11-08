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
            e.tag = V.allTags.find { it.id == map["tag"]!! as Int }!!
            e.id = map["id"]!! as Int
            e.icon = map["icon"]!! as String
            e.color = map["color"]!! as Double
            e.priority = map["priority"]!! as Int
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
            val start = map["start"]!! as Map<String, Int>
            e.start = LocalDateTime.of(
                start["year"]!!,
                start["month"]!!,
                start["day"]!!,
                start["hour"]!!,
                start["minute"]!!
            )
            val end = map["end"]!! as Map<String, Int>
            e.end = LocalDateTime.of(
                end["year"]!!,
                end["month"]!!,
                end["day"]!!,
                end["hour"]!!,
                end["minute"]!!
            )
            val anticipations = map["anticipations"] as ArrayList<Map<String, Int>>
            anticipations.forEach {
                e.addAnticipation(
                    Difference(
                        it["year"]!!,
                        it["month"]!!,
                        it["day"]!!,
                        it["hour"]!!,
                        it["minute"]!!
                    )
                )
            }
            e.pospositionDaysLimit = map["pospositionDaysLimit"]!! as Int
            e.posposed = map["posposed"] as Int
            e.setReminders(
                stringToEnumSchedule(map["reminderType"]!! as String),
                map["reminderDelay"]!! as Int
            )
            val repeatLimit: Map<String, Int>? = map["repeatLimit"] as Map<String, Int>
            var limitDate: LocalDateTime? = null
            if(repeatLimit != null) {
                limitDate = LocalDateTime.of(
                    repeatLimit["year"]!!,
                    repeatLimit["month"]!!,
                    repeatLimit["day"]!!,
                    repeatLimit["hour"]!!,
                    repeatLimit["minute"]!!
                )
            }
            e.setRepetitions(
                stringToEnumSchedule(map["repeatType"]!! as String),
                map["repeatDelay"]!! as Int,
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
            val result = HashMap<String, Any>()
            result["id"] = e.id
            result["name"] = e.name
            result["description"] = e.description
            result["tag"] = e.tag.id
            result["icon"] = e.icon
            result["color"] = e.color
            result["priority"] = e.priority
            val tasks = ArrayList<Map<String, Any>>()
            e.tasks.forEach {
                val task = HashMap<String, Any>()
                task["description"] = it.description
                task["isDone"] = it.isDone
                tasks.add(task)
            }
            result["tasks"] = tasks
            result["location"] = e.location
            result["isComplete"] = e.isComplete
            result["isFullDay"] = e.isFullDay
            val start = HashMap<String, Int>()
            start["year"] = e.start.year
            start["month"] = e.start.monthValue+1
            start["day"] = e.start.dayOfMonth
            start["hour"] = e.start.hour
            start["minute"] = e.start.minute
            result["start"] = start
            val end = HashMap<String, Int>()
            end["year"] = e.end.year
            end["month"] = e.end.monthValue+1
            end["day"] = e.end.dayOfMonth
            end["hour"] = e.end.hour
            end["minute"] = e.end.minute
            result["end"] = end
            val anticipations = ArrayList<Map<String,Int>>()
            e.anticipations.forEach {
                val a = HashMap<String, Int>()
                a["year"] = it.fatherDifference.years
                a["month"] = it.fatherDifference.months
                a["day"] = it.fatherDifference.days
                a["hour"] = it.fatherDifference.hours
                a["minute"] = it.fatherDifference.minutes
            }
            result["pospositionDaysLimit"] = e.pospositionDaysLimit
            result["posposed"] = e.posposed
            result["reminderType"] = e.reminderType
            result["reminderDelay"] = e.reminderDelay
            result["repeatType"] = enumToString(e.repeatType)
            result["repeatDelay"] = e.repeatDelay
            if(e.repeatLimit != null) {
                val limit = HashMap<String, Int>()
                limit["year"] = e.repeatLimit!!.year
                limit["month"] = e.repeatLimit!!.monthValue+1
                limit["day"] = e.repeatLimit!!.dayOfMonth
                limit["hour"] = e.repeatLimit!!.hour
                limit["minute"] = e.repeatLimit!!.minute
                result["repeatLimit"] = limit
            }
            result["sharedWith"] = e.sharedWith
            return result
        }
        fun eventsToMaps(es: ArrayList<EventFather>): ArrayList<Map<String, Any>> {
            val result = ArrayList<Map<String, Any>>()
            result.addAll(es.map { eventToMap(it) })
            return result
        }
    }
}