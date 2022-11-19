package com.kaltt.agenda.classes

import com.google.firebase.auth.FirebaseUser
import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class DataUser (var email: String, var name: String) {
    companion object {
        fun from(u: FirebaseUser): DataUser =
            DataUser(u.email!!, u.displayName!!)
    }
}

data class DataDate(
    val date: String
)

data class DataEventGoogle(
    val id: String,
    @SerializedName("summary")
    val name: String,
    val description: String,
    val start: DataDate,
    val end: DataDate
)

data class DataGoogleCalendar(
    val items: ArrayList<DataEventGoogle>
)

data class DataTask(
    val position: Int,
    val dsecription: String,
    val isDone: Boolean
) {
    companion object {
        fun from(map: Map<String, Any>): DataTask = DataTask(
            (map["position"]!!as Long).toInt(),
            map["description"]!! as String,
            map["isDone"]!! as Boolean
        )
    }
}

data class DataDateTime(
    val year: Int,
    val month: Int,
    val day: Int,
    val hour: Int,
    val minute: Int
) {
    companion object {
        fun from(t: LocalDateTime?): DataDateTime =
            if(t == null) DataDateTime(0,0,0,0,0)
            else DataDateTime(t.year, t.monthValue+1, t.dayOfMonth, t.hour, t.minute)
        fun from(map: Map<String, Any>): DataDateTime =
            DataDateTime(
                (map["year"] as Long? ?: 0).toInt(),
                (map["month"] as Long? ?: 0).toInt(),
                (map["day"] as Long? ?: 0).toInt(),
                (map["hour"] as Long? ?: 0).toInt(),
                (map["minute"] as Long? ?: 0).toInt()
            )
    }
    fun toLocalDateTime(): LocalDateTime =
        LocalDateTime.of(year, month, day, hour, minute)
}

data class DataEventAnticipation(
    val isDone: Boolean,
    val date: DataDateTime
) {
    companion object {
        fun from(map: Map<String, Any>): DataEventAnticipation =
            DataEventAnticipation(
                map["isDone"]!! as Boolean,
                DataDateTime.from(map["date"]!! as Map<String, Any>)
            )
    }
}

data class DataEventFather(
    val owner: String,
    val id: String,
    val name: String,
    val description: String,
    val tag: String,
    val icon: String,
    val color: Double,
    val priority: Int,
    val isLazy: Boolean,
    val tasks: ArrayList<DataTask>,
    val location: String,
    val isComplete: Boolean,
    val isFullDay: Boolean,
    val start: DataDateTime,
    val end: DataDateTime,
    val anticipations: List<DataEventAnticipation>,
    val pospositionDaysLimit: Int,
    val posposed: Int,
    val reminderType: String,
    val remidnerDelay: Int,
    val repeatType: String,
    val repeatDelay: Int,
    val repeatLimit: DataDateTime,
    val sharedWith: ArrayList<String>
) {
    companion object {
        fun from(map: Map<String, Any>): DataEventFather =
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
                ArrayList((map["tasks"]!! as ArrayList<Map<String, Any>>).map { DataTask.from(it) }),
                map["location"]!! as String,
                map["isComplete"]!! as Boolean,
                map["isFullDay"]!! as Boolean,
                DataDateTime.from(map["start"]!! as Map<String, Any>),
                DataDateTime.from(map["end"]!! as Map<String, Any>),
                ArrayList((map["anticipations"]!! as ArrayList<Map<String, Any>>).map { DataEventAnticipation.from(it) }),
                (map["pospositionDaysLimit"]!!as Long).toInt(),
                (map["posposed"]!!as Long).toInt(),
                map["reminderType"]!! as String,
                (map["reminderDelay"]!!as Long).toInt(),
                map["repeatType"]!! as String,
                (map["repeatDelay"]!!as Long).toInt(),
                DataDateTime.from(map["repeatLimit"]!! as Map<String, Any>),
                map["sharedWith"]!! as ArrayList<String>
            )
    }
}

data class DataTag(val x: String)