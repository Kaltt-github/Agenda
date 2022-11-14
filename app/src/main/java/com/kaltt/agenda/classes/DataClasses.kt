package com.kaltt.agenda.classes

import com.google.gson.annotations.SerializedName

data class DataUser (var email: String, var name: String)

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
)

data class DataDateTime(
    val year: Int,
    val month: Int,
    val day: Int,
    val hour: Int,
    val minute: Int
)

data class DataEventAnticipation(
    val isDone: Boolean,
    val date: DataDateTime
)

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
)

data class DataTag(val x: String)

/*
mapToObject(map, Person::class)
maybe val constructor = clazz.constructors.elementAt(1)
fun <T : Any> mapToObject(map: Map<String, Any>, clazz: KClass<T>)
 * */
/*
fun from(map: Map<String, String>) = object {
            val foo by map
            val bar by map

            val data = MyData(foo, bar)
        }.data
* */