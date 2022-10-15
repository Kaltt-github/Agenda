package com.kaltt.agenda.classes

import com.kaltt.agenda.classes.enums.EventType
import java.time.LocalDateTime

class EventRepeat(override var father: EventFather, start : LocalDateTime, override var wasSeen: Boolean
) : EventChild {
    override val type: EventType = EventType.REPEAT // SAVE ME VAR
    override var isCompleted: Boolean
        get() = wasSeen
        set(value) { wasSeen = value}
    init {
        this.start = start // SAVE ME VAR
    }
    override var posposition: EventPosposition? = null // SAVE ME VAR
    override fun canPospone(days: Int): Boolean {
        return this.posponableLimit <= days
    }
    override fun pospone(days: Int) {
        if(canPospone(days)) {
            this.posposition = EventPosposition(this, days)
        }
    }
}