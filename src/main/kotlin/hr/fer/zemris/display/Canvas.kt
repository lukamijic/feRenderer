package hr.fer.zemris.display

import hr.fer.zemris.color.Color
import hr.fer.zemris.color.RGB
import hr.fer.zemris.display.depth.ZBuffer
import hr.fer.zemris.graphicsAlgorithms.clipping.line.CohenSutherlandLineClippingAlgorithm
import hr.fer.zemris.graphicsAlgorithms.clipping.line.LineClipping
import hr.fer.zemris.graphicsAlgorithms.util.BoundingBox
import java.lang.IndexOutOfBoundsException
import java.util.*

class Canvas(
    val width: Int,
    val height: Int,
    var lineClipping: LineClipping = CohenSutherlandLineClippingAlgorithm(
        0,
        width - 1,
        0,
        height - 1
    )
) {

    val canvasBoundingBox = BoundingBox(0, width - 1, 0, height - 1)

    private val widthRange = 0 until width
    private val heightRange = 0 until height
    private val rgbComponents = Array(width * height) { RGB.BLACK }

    private val zBuffer = ZBuffer(width, height)

    fun drawPixel(x: Int, y: Int, z: Double, rgb: RGB) {
        val componentIndex = y * width + x
        if (x in widthRange && y in heightRange) {
            if (zBuffer.set(componentIndex, z)) {
                rgbComponents[componentIndex] = rgb
            }
        } else {
            throw IndexOutOfBoundsException("($x, $y) is out of ([0, $width>, [0, $height>")
        }
    }

    fun drawPixel(x: Int, y: Int, z: Double, color: Color) = drawPixel(x, y, z, color.toRGB())

    fun clear(rgb: RGB) = Arrays.fill(rgbComponents, rgb)

    fun clear(color: Color) = Arrays.fill(rgbComponents, color.toRGB())

    fun clearDepth() = zBuffer.reset()

    fun fillRGBIntArray(dest: IntArray) {
        for (i in 0 until width * height) {
            dest[i] = rgbComponents[i].toColor().value
        }
    }
}