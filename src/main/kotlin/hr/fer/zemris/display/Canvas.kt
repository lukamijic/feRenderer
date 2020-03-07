package hr.fer.zemris.display

import hr.fer.zemris.color.Color
import hr.fer.zemris.color.RGB
import java.lang.IndexOutOfBoundsException
import java.util.*

private const val BGR_COMPONENTS_COUNT = 3

class Canvas (
    val width: Int,
    val height: Int
) {

    private val widthRange = 0 until width
    private val heightRange = 0 until height
    private val rgbComponents = Array(width * height) { RGB.BLACK }

    fun drawPixel(x: Int, y: Int, rgb: RGB) {
        if (x in widthRange && y in heightRange) {
            val componentIndex = y * width + x
            rgbComponents[componentIndex] = rgb
        } else {
            throw IndexOutOfBoundsException("($x, $y) is out of ([0, $width>, [0, $height>")
        }
    }

    fun drawPixel(x: Int, y: Int, color: Color) =  drawPixel(x, y, color.toRGB())

    fun clear(rgb: RGB) = Arrays.fill(rgbComponents, rgb)

    fun clear(color: Color) = Arrays.fill(rgbComponents, color.toRGB())

    fun fillRGBIntArray(dest: IntArray) {
        for (i in 0 until width * height) {
            dest[i] = rgbComponents[i].toColor().value
        }
    }

}