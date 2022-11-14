package com.kaltt.agenda.classes

class ColorTool {
    private var updatingRGB: Boolean = false
    private var updatingHSB: Boolean = false
    constructor(rgb: String) {
        this.rgb = rgb
        updatingHSB = true
        updateHSB()
        updatingRGB = true
    }
    constructor(h: Double, s: Double, b: Double) {
        this.hsb = arrayListOf(h, s, b)
        updatingRGB = true
        updateRGB()
        updatingHSB = true
    }

    var red = 1.0
        set(value) {
            updatingRGB = false
            field = limitHex(value)
            updateHSB()
        }
    var blue = 2.0
        set(value) {
            updatingRGB = false
            field = limitHex(value)
            updateHSB()
        }
    var green = 3.0
        set(value) {
            updatingRGB = false
            field = limitHex(value)
            updateHSB()
        }
    var hue = 360.0
        set(value) {
            updatingHSB = false
            field = limitHue(value)
            updateRGB()
        }
    var saturation = 100.0
        set(value) {
            updatingHSB = false
            field = limitHundred(value)
            updateRGB()
        }
    var bright = 100.0
        set(value) {
            updatingHSB = false
            field = limitHundred(value)
            updateRGB()
        }
    var rgb: String
        get() = "#${toHex(red)}${toHex(green)}${toHex(blue)}"
        set(value) {
            updatingRGB = false
            this.red = value.substring(1,3).toInt(16).toDouble()
            this.green = value.substring(3,5).toInt(16).toDouble()
            this.blue = value.substring(5,7).toInt(16).toDouble()
            updateHSB()
        }
    var hsb: List<Double>
        get() = listOf(this.hue, this.saturation, this.bright)
        set(value) {
            updatingHSB = false
            this.hue = value[0]
            this.saturation = value[1]
            this.bright = value[2]
            updateRGB()
        }

    // USAGE
    private fun updateRGB() {
        if(!updatingRGB) {
            return
        }
        val z: Int = Math.floorDiv(this.hue.toInt(), 60)
        val max: String
        val mid: String
        val min: String
        val a: String
        val b: String
        when (z) {
            0, 6 -> {
                max = "red"
                mid = "green"
                min = "blue"
            }
            1 -> {
                max = "green"
                mid = "red"
                min = "blue"
            }
            2 -> {
                max = "green"
                mid = "blue"
                min = "red"
            }
            3 -> {
                max = "blue"
                mid = "green"
                min = "red"
            }
            4 -> {
                max = "blue"
                mid = "red"
                min = "green"
            }
            else -> { // 5
                max = "red"
                mid = "blue"
                min = "green"
            }
        }
        var x: Int
        when (max) {
            "red" -> {
                x = if (z == 6) 6 else 0
                a = "green"
                b = "blue"
            }
            "green" -> {
                x = 2
                a = "blue"
                b = "red"
            }
            else -> { // blue
                x = 4
                a = "red"
                b = "green"
            }
        }
        setByWord(max, (bright * 255)/100)
        setByWord(min, (1 - saturation/100) * getByWord(max))
        x = ((hue / 60 - x) * (getByWord(max) - getByWord(min))).toInt()
        if (a == mid) {
            setByWord(a, getByWord(b) + x)
        } else {
            setByWord(b, getByWord(a) - x)
        }
    }

    private fun updateHSB() {
        if(!updatingHSB) {
            return
        }
        val max: String
        val min: String
        if (green > red && green > blue) {
            max = "green"
            min = if (blue > red) "red" else "blue"
        } else if (blue > green && blue > red) {
            max = "blue"
            min = if (green > red) "red" else "green"
        } else {
            max = "red"
            min = if (blue > green) "green" else "blue"
        }
        // HUE
        val a: String
        val b: String
        val x: Int
        when (max) {
            "green" -> {
                a = "blue"
                b = "red"
                x = 2
            }
            "blue" -> {
                a = "red"
                b = "green"
                x = 4
            }
            else -> { // red
                a = "green"
                b = "blue"
                x = if (getByWord(b) >= getByWord(a)) 6 else 0
            }
        }
        hue = limitHue(((getByWord(a) - getByWord(b)) / (getByWord(max) - getByWord(min)) + x) * 60)
        // SATURATION
        saturation = limitHundred((1 - getByWord(min) / getByWord(max)) * 100)
        // BRIGHT
        bright = limitHundred(getByWord(max) / 225 * 100)
    }

    private fun getByWord(s: String): Double {
        return when (s) {
            "red" -> red
            "green" -> green
            "blue" -> blue
            "hue" -> hue
            "saturation" -> saturation
            "bright" -> bright
            else -> 0.0
        }
    }

    private fun setByWord(s: String, x: Double): ColorTool {
        return when (s) {
            "red" -> {
                red = x
                this
            }
            "green" -> {
                green = x
                this
            }
            "blue" -> {
                blue = x
                this
            }
            "hue" -> {
                hue = x
                this
            }
            "saturation" -> {
                saturation = x
                this
            }
            "bright" -> {
                bright = x
                this
            }
            else -> this
        }
    }

    private fun limit(min: Double, x: Double, max: Double): Double {
        if (x < min) {
            return min
        }
        return if (x > max) {
            max
        } else x
    }

    private fun limitHex(x: Double): Double {
        return limit(0.0, x, 255.0)
    }

    private fun limitHundred(x: Double): Double {
        return limit(0.0, x, 100.0)
    }

    private fun limitHue(x: Double): Double {
        return limit(0.0, x % 360, 360.0)
    }

    private fun toHex(x: Double): String {
        val s = Integer.toHexString(x.toInt())
        return if (s.length == 1) "0$s" else s
    }
}