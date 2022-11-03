package com.kaltt.agenda.apis

import com.kaltt.agenda.classes.events.EventFather

class Factory {
    companion object {
        fun mapToEvent(map: Map<String, Any>): EventFather {
            var e = EventFather(map["owner"]!! as String)
            e.name = map["name"]!! as String
            /*
    var id: Int
    var icon: String
    var name: String
    var tag: TagEagle
    var description: String
    var color: Double
    var priority: Int
    var isLazy: Boolean
    var tasks: ArrayList<Task>
    var location: String
    var isComplete: Boolean
    var isFullDay: Boolean
    var start: LocalDateTime
    var end: LocalDateTime

    var anticipations: ArrayList<EventAnticipation>
    fun anticipation(): EventAnticipation = this.anticipations[0]
    var posposition: EventPosposition
    var reminder: EventReminder
    var repetitions: ArrayList<EventRepeat>
    var repeatType: ScheduleType
    var repeatDelay: Int
    var repeatLimit: LocalDateTime?
    var sharedWith: ArrayList<String>
            * */
            return e;
        }
    }
}