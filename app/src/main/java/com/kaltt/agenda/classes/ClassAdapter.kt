package com.kaltt.agenda.classes

import com.google.firebase.auth.FirebaseUser
import com.kaltt.agenda.V
import com.kaltt.agenda.classes.enums.*
import com.kaltt.agenda.classes.events.*
import java.time.LocalDateTime
import kotlin.math.floor

class ClassAdapter {
    class fromString {
        class toEnum {
            companion object {
                fun asEventType(s: String): EventType  = when(s) {
                    "ANTICIPATION" -> EventType.ANTICIPATION
                    "POSPOSITION" -> EventType.POSPOSITION
                    "REMINDER" -> EventType.REMINDER
                    "REPEAT" -> EventType.REPEAT
                    else -> EventType.FATHER
                }
                fun asFromType(s: String): FromType  = when(s) {
                    "OWNED" -> FromType.OWNED
                    "SHARED" -> FromType.SHARED
                    "GOOGLE" -> FromType.GOOGLE
                    else -> FromType.APP
                }
                fun asScheduleType(s: String): ScheduleType = when(s) {
                    "MINUTES" -> ScheduleType.MINUTES
                    "HOURS" -> ScheduleType.HOURS
                    "DAYS" -> ScheduleType.DAYS
                    "WEEKS" -> ScheduleType.WEEKS
                    "MONTHS" -> ScheduleType.MONTHS
                    "YEARS" -> ScheduleType.YEARS
                    else -> ScheduleType.DONT
                }
            }
        }
    }
    class fromEnum {
        companion object {
            fun toString(e: FromType): String = when(e) {
                FromType.APP -> "APP"
                FromType.GOOGLE -> "GOOGLE"
                FromType.OWNED -> "OWNED"
                FromType.SHARED -> "SHARED"
            }
            fun toString(e: EventType): String = when(e) {
                EventType.ANTICIPATION -> "ANTICIPATION"
                EventType.POSPOSITION -> "POSPOSITION"
                EventType.REPEAT -> "REPEAT"
                EventType.REMINDER -> "REMINDER"
                EventType.FATHER -> "FATHER"
            }
            fun toString(e: ScheduleType): String = when(e) {
                ScheduleType.MINUTES -> "MINUTES"
                ScheduleType.HOURS -> "HOURS"
                ScheduleType.DAYS -> "DAYS"
                ScheduleType.WEEKS -> "WEEKS"
                ScheduleType.MONTHS -> "MONTHS"
                ScheduleType.YEARS -> "YEARS"
                ScheduleType.DONT -> "DONT"
            }
        }
    }
    class fromClass {
        companion object {
            fun toData(u: FirebaseUser): DataUser =
                DataUser(u.email!!, u.displayName!!)
            fun toData(d: Difference): DataDateTime =
                DataDateTime(d.years, d.months, d.days, d.hours, d.minutes)
            fun toData(t: LocalDateTime?): DataDateTime =
                if(t == null) DataDateTime(0,0,0,0,0)
                else DataDateTime(t.year, t.monthValue+1, t.dayOfMonth, t.hour, t.minute)
            fun toData(e: EventFather): DataEventFather = DataEventFather(
                e.owner,
                e.id,
                e.name,
                e.description,
                e.tag.id,
                e.icon,
                e.color,
                e.priority,
                e.isLazy,
                fromList.toArrayList(e.tasks.map { DataTask(it.position, it.description, it.isDone) }),
                e.location,
                e.isComplete,
                e.isFullDay,
                toData(e.start),
                toData(e.end),
                fromList.toArrayList(e.anticipations.map { DataEventAnticipation(it.localComplete, toData(it.fatherDifference)) }),
                e.pospositionDaysLimit,
                e.posposed,
                fromEnum.toString(e.reminderType),
                e.reminderDelay,
                fromEnum.toString(e.repeatType),
                e.repeatDelay,
                toData(e.repeatLimit),
                e.sharedWith
            )
            fun toData(es: ArrayList<EventFather>): ArrayList<DataEventFather> =
                fromList.toArrayList(es.map { toData(it) })

            fun toMap(e: EventFather): Map<String, Any> =
                hashMapOf(
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
                            "position" to it.position,
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
                    "repeatType" to fromEnum.toString(e.repeatType),
                    "repeatDelay" to e.repeatDelay,
                    "repeatLimit" to hashMapOf(
                        "year" to (e.repeatLimit?.year ?: 0),
                        "month" to (e.repeatLimit?.monthValue ?: -1 )+1,
                        "day" to (e.repeatLimit?.dayOfMonth ?: 0),
                        "hour" to (e.repeatLimit?.hour ?: 0),
                        "minute" to (e.repeatLimit?.minute ?: 0)
                    ),
                    "sharedWith" to e.sharedWith
                )
        }
    }
    class fromData {
        class toClass {
            companion object {
                fun asLocalDateTime(d: DataDateTime): LocalDateTime =
                    LocalDateTime.of(d.year, d.month, d.day, d.hour, d.minute)
                fun asDifference(d: DataDateTime): Difference =
                    Difference(d.year, d.month, d.day, d.hour, d.minute)
                fun asEventAnticipation(f: EventFather, d: DataEventAnticipation): EventAnticipation {
                    val a = EventAnticipation(f, asDifference(d.date))
                    if(d.isDone) {
                        a.isComplete = true
                    }
                    return a
                }
                fun asTask(f: EventFather, t: DataTask): Task = Task(f, t.dsecription, t.isDone)
                fun asEventFather(from: FromType, data: DataEventFather): EventFather {
                    val e = EventFather(from, data.owner)
                    e.name = data.name
                    e.description = data.description
                    e.tag = V.allTags.find { it.id == data.tag } ?: Tag.empty()
                    e.id = data.id
                    e.icon = data.icon
                    e.color = data.color
                    e.priority = data.priority
                    e.tasks.clear()
                    data.tasks.sortBy { it.position }
                    e.tasks = fromList.toArrayList(data.tasks.map { asTask(e, it) })
                    e.location = data.location
                    e.isComplete = data.isComplete
                    e.isFullDay = data.isFullDay
                    e.start = asLocalDateTime(data.start)
                    e.end = asLocalDateTime(data.end)
                    e.anticipations = fromList.toArrayList( data.anticipations.map { asEventAnticipation(e, it) })
                    e.pospositionDaysLimit = data.pospositionDaysLimit
                    e.posposed = data.posposed
                    e.setReminders(
                        fromString.toEnum.asScheduleType(data.reminderType),
                        data.remidnerDelay
                    )
                    e.setRepetitions(
                        fromString.toEnum.asScheduleType(data.repeatType),
                        data.repeatDelay,
                        if(!asDifference(data.repeatLimit).isEmpty()) {
                            asLocalDateTime(data.repeatLimit)
                        } else { null }
                    )
                    e.sharedWith = data.sharedWith
                    e.isLazy = data.isLazy
                    return e
                }
                fun asEventFather(from: FromType, maps: List<DataEventFather?>): ArrayList<EventFather> =
                    fromList.toArrayList(maps.filterNotNull().map { asEventFather(from, it) })
                fun asEventFather(body: DataGoogleCalendar): ArrayList<EventFather> {
                    val result = ArrayList<EventFather>()
                    fun extract(s: String): LocalDateTime {
                        var a = s.split("-").map { it.toInt() }
                        return LocalDateTime.of(a[0],a[1],a[2],0,0)
                    }
                    body.items.forEach {
                        var e = EventFather(FromType.GOOGLE, "Google")
                        e.id = it.id
                        e.name = it.name
                        e.description = it.description
                        e.start = extract(it.start.date)
                        e.end = extract(it.end.date)
                        e.color = listOf(4.0,44.0,137.0,216.0)[floor(Math.random()*4.0).toInt()]
                        e.pospositionDaysLimit = 0
                        e.isFullDay = true
                        result.add(e)
                    }
                    return result
                }
            }
        }
    }
    class fromMap {
        class toData {
            companion object {
                fun asTask(map: Map<String, Any>): DataTask = DataTask(
                    (map["position"]!!as Long).toInt(),
                    map["description"]!! as String,
                    map["isDone"]!! as Boolean
                )
                fun asTask(map: List<Map<String, Any>>): ArrayList<DataTask> =
                    fromList.toArrayList(map.map { asTask(it) })
                fun asDifference(map: Map<String, Any>): DataDateTime =
                    DataDateTime(
                        (map["year"]!!as Long).toInt(),
                        (map["month"]!!as Long).toInt(),
                        (map["day"]!!as Long).toInt(),
                        (map["hour"]!!as Long).toInt(),
                        (map["minute"]!!as Long).toInt()
                    )
                fun asEventAnticipation(map: Map<String, Any>): DataEventAnticipation =
                    DataEventAnticipation(
                        map["isDone"]!! as Boolean,
                        asDifference(map["date"]!! as Map<String, Any>)
                    )
                fun asEventAnticipation(map: List<Map<String, Any>>): ArrayList<DataEventAnticipation> =
                    fromList.toArrayList( map.map { asEventAnticipation(it) })
                fun asDateTime(map: Map<String, Any>): DataDateTime =
                    DataDateTime(
                        (map["year"] as Long? ?: 0L).toInt(),
                        (map["month"] as Long? ?: 0L).toInt(),
                        (map["day"] as Long? ?: 0L).toInt(),
                        (map["hour"] as Long? ?: 0L).toInt(),
                        (map["minute"] as Long? ?: 0L).toInt(),
                    )
                fun asEventFather(map: Map<String, Any>): DataEventFather =
                    DataEventFather(
                        map["owner"]!! as String,
                        map["id"]!! as String,
                        map["name"]!! as String,
                        map["description"]!! as String,
                        map["tag"]!! as String,
                        map["icon"]!! as String,
                        (map["color"]!! as Long).toDouble(),
                        (map["priority"]!!as Long).toInt(),
                        map["isLazy"]!! as Boolean,
                        asTask(map["tasks"]!! as ArrayList<Map<String, Any>>),
                        map["location"]!! as String,
                        map["isComplete"]!! as Boolean,
                        map["isFullDay"]!! as Boolean,
                        asDateTime(map["start"]!! as Map<String, Any>),
                        asDateTime(map["end"]!! as Map<String, Any>),
                        asEventAnticipation(map["anticipations"]!! as ArrayList<Map<String, Any>>),
                        (map["pospositionDaysLimit"]!!as Long).toInt(),
                        (map["posposed"]!!as Long).toInt(),
                        map["reminderType"]!! as String,
                        (map["reminderDelay"]!!as Long).toInt(),
                        map["repeatType"]!! as String,
                        (map["repeatDelay"]!!as Long).toInt(),
                        asDateTime(map["repeatLimit"]!! as Map<String, Any>),
                        map["sharedWith"]!! as ArrayList<String>
                    )
                fun asEventFather(map: List<Map<String, Any>>): ArrayList<DataEventFather> =
                    fromList.toArrayList( map.map { asEventFather(it) })
            }
        }
    }
    class fromList {
        companion object {
            fun <E : Any> toArrayList(l: List<E>): ArrayList<E> {
                val x = ArrayList<E>()
                x.addAll(l)
                return x
            }
        }
    }
}