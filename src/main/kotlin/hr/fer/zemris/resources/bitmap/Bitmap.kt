package hr.fer.zemris.resources.bitmap

import hr.fer.zemris.color.Color
import java.lang.IllegalArgumentException
import kotlin.math.ceil

class Bitmap(
    private val width: Int,
    private val height: Int,
    private val rgbComponents: IntArray
) {

    private val widthRange = 0 until width
    private val heightRange = 0 until height
    private val normalizedRange = 0.0..1.0

    init {
        if (width * height != rgbComponents.size) {
            throw IllegalArgumentException("Array is of size ${rgbComponents.size} and it should be size width($width) * height($height) = ${width * height}")
        }
    }

    operator fun get(x: Int, y: Int): Color {
        if (x !in widthRange || y !in heightRange ) {
            throw IllegalArgumentException("($x, $y) must be in range ([0, $width>, [0, $height>)")
        }

        return Color(rgbComponents[(y * width + x)])
    }

    operator fun get(u: Double, v: Double) :  Color? =
        if (u !in normalizedRange || v !in normalizedRange) {
            null //throw IllegalArgumentException("($u, $v) must be in range [0, 1]")
        } else {
            get(ceil(u * (width - 1)).toInt(), ceil(v * (height - 1)).toInt())
        }

}