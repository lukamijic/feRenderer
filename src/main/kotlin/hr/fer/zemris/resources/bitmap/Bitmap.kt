package hr.fer.zemris.resources.bitmap

import hr.fer.zemris.color.Color
import java.lang.IllegalArgumentException

class Bitmap(
    private val width: Int,
    private val height: Int,
    private val rgbComponents: IntArray
) {

    init {
        if (width * height != rgbComponents.size) {
            throw IllegalArgumentException("Array is of size ${rgbComponents.size} and it should be size width($width) * height($height) = ${width * height}")
        }
    }

    operator fun get(x: Int, y: Int): Color {
        if (x !in 0 until width || y !in 0 until height ) {
            throw IllegalArgumentException("($x, $y) must be in range ([0, $width>, [0, $height>)")
        }

        return Color(rgbComponents[(y * width + x)])
    }
}