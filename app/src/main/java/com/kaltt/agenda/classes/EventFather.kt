package com.kaltt.agenda.classes

import com.kaltt.agenda.classes.enums.EventType
import com.kaltt.agenda.classes.enums.ScheduleType
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class EventFather(
    override val owner: String,
    override var tag: Tag = Tag.empty()
) : Event {
    // VALUES
    override val eventType: EventType = EventType.FATHER
    override val father = this
    override var id: Int = -1
    override var icon: String = "" 
    override var name: String = "New"
    override var description: String = ""
    override var color: Double = 0.0
    override var priority: Int = 0
    override var tasks: ArrayList<Task> = ArrayList()
    override var location: String = ""
    override var isComplete: Boolean = false
    override var isFullDay: Boolean = false
    private var startDate: LocalDate = LocalDate.now()
    private var startTime: LocalTime = LocalTime.now()
    private var length: Difference = Difference(days = 1)
    override var posposition = EventPosposition(this)
    override var anticipations: ArrayList<EventAnticipation> = ArrayList()
    override var repeatType: ScheduleType = ScheduleType.DONT
    override var repeatDelay: Int = 0
    override var repeatLimit: LocalDateTime? = null
    override var reminder: EventReminder = EventReminder(this)
    override var repetitions: ArrayList<EventRepeat> = ArrayList()
    override fun isLastRepetition(): Boolean = false
    override fun isLastAnticipation(): Boolean = false
    override fun isLastReminder(): Boolean = false
    override fun isLastPosposition(): Boolean = false
    override var isLazy: Boolean = false
        set(value) {
            if (value) {
                this.posposition.daysLimit = 0
                this.anticipations.clear()
                this.reminder.type = ScheduleType.DONT
            }
            field = value
        }
    // CALCULATED VALUES
    override var start: LocalDateTime
        get() = LocalDateTime.of(this.startDate, if (this.isFullDay) LocalTime.of(0, 0) else this.startTime)
        set(value) {
            this.startDate = value.toLocalDate()
            this.startTime = value.toLocalTime()
        }
    override var end: LocalDateTime
        get() = this.length.applyOn(this.start)
        set(value) { this.length = Difference.between(this.start, value) }

    init {
        this.tag.applyOn(this)
    }

    override fun setRepetitions(type: ScheduleType?, delay: Int?, limit: LocalDateTime?) {
        this.repeatType = type ?: this.repeatType
        this.repeatDelay = delay ?: this.repeatDelay
        this.repeatLimit = limit ?: this.repeatLimit
        this.repetitions.clear()
        if (repeatType == ScheduleType.DONT || repeatDelay == 0) {
            return
        }
        var difference = Difference.by(repeatType, repeatDelay)
        var date = difference.applyOn(this.start)
        var i = 0
        while(date.isBefore(repeatLimit ?: LocalDateTime.now().plusYears(10))) {
            this.repetitions.add(EventRepeat(this, i))
            date = difference.applyOn(date)
            i++
        }
    }
}