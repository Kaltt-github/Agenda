package com.kaltt.agenda.classes.tags

import com.kaltt.agenda.classes.enums.TagType

interface Tag {
    var id: Int
    var name: String
    val type: TagType
}