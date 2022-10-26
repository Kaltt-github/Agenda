package com.kaltt.agenda.classes.individualTests

import com.kaltt.agenda.classes.*
import com.kaltt.agenda.classes.enums.EventType
import com.kaltt.agenda.classes.enums.ScheduleType
import java.time.LocalDateTime

class FakeEvent: Event {
    override val owner: String = "Owner"
    override val eventType: EventType = EventType.FATHER
    override val father: Event = this
    override var id: Int = 0
    override var icon: String = "Icono"
    override var name: String = "Nomre"
    override var tag: Tag = Tag.empty()
    override var description: String = "Descripcion"
    override var color: Double = 35.0
    override var priority: Int =5
    override var isLazy: Boolean = false
    override var tasks: ArrayList<Task> = ArrayList()
    override var location: String = "Location"
    override var isComplete: Boolean = false
    override var isFullDay: Boolean = false
    override var start: LocalDateTime = LocalDateTime.now().minusDays(1)
    override var end: LocalDateTime = LocalDateTime.now().plusDays(1)
    override var anticipations: ArrayList<EventAnticipation> = ArrayList()

    override fun anticipation(): EventAnticipation {
        return super.anticipation()
    }

    override var posposition: EventPosposition = EventPosposition(this)
    override var reminder: EventReminder = EventReminder(this)
    override var repetitions: ArrayList<EventRepeat> = ArrayList()
    override var repeatType: ScheduleType = ScheduleType.DONT
    override var repeatDelay: Int = 0
    override var repeatLimit: LocalDateTime? = null

    override fun setRepetitions(type: ScheduleType?, delay: Int?, limit: LocalDateTime?) {
        this.repeatType = type ?: this.repeatType
        this.repeatDelay = delay ?: this.repeatDelay
        this.repeatLimit = limit ?: this.repeatLimit
    }

    override fun isLastRepetition(): Boolean = false
    override fun isLastAnticipation(): Boolean = false
    override fun isLastReminder(): Boolean = false
    override fun isLastPosposition(): Boolean = false
}