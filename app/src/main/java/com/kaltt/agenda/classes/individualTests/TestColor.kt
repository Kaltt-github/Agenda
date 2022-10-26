package com.kaltt.agenda.classes.individualTests

import com.kaltt.agenda.classes.ColorTool

fun show(c: ColorTool) {
    println("${c.rgb}|${c.red}|${c.green}|${c.blue}|${c.hue}|${c.saturation}|${c.bright}")
}

fun main (arguments: Array<String>) {
    println("Probando clase COLOR")
    var l = ArrayList<ColorTool>()
    // Constructores
    l.add(ColorTool("#1bccba"))
    l.add(ColorTool(273.0, 78.0, 93.0))
    l.forEach { show(it) }
}