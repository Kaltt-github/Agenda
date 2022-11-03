package com.kaltt.agenda.classes.tags

import com.kaltt.agenda.classes.enums.TagType

class TagLazy(
    override var id: Int,
    override var name: String,
): Tag {
    override val type: TagType = TagType.LAZY
    fun toEagle(): TagEagle {
        return TagEagle.empty() // TODO
    }
}