package hr.fer.zemris.display

import hr.fer.zemris.color.Color
import hr.fer.zemris.color.RGB
import hr.fer.zemris.graphicsAlgorithms.BresenhamLineAlgorithm
import hr.fer.zemris.model.Point
import java.lang.IndexOutOfBoundsException
import java.util.*

class Canvas(
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

    fun drawPixel(x: Int, y: Int, color: Color) = drawPixel(x, y, color.toRGB())

    fun drawLine(p1: Point, p2: Point, rgb: RGB) =
        BresenhamLineAlgorithm.bresenhamCalculateLine(p1, p2)
            .filter(::isPointInCanvas)
            .forEach { p ->
                drawPixel(p.x, p.y, rgb)
            }


    fun drawLine(p1: Point, p2: Point, color: Color) =
        drawLine(p1, p2, color.toRGB())

    fun drawTriangle(p1: Point, p2: Point, p3: Point, color: Color) {
        drawLine(p1, p2, color)
        drawLine(p1, p3, color)
        drawLine(p2, p3, color)
    }

    fun fillTriangle(p1: Point, p2: Point, p3: Point, color: Color) {
        val lowestXForY = mutableMapOf<Int, Int>()
        val highestXForY = mutableMapOf<Int, Int>()

        listOf(
            BresenhamLineAlgorithm.bresenhamCalculateLine(p1, p2),
            BresenhamLineAlgorithm.bresenhamCalculateLine(p1, p3),
            BresenhamLineAlgorithm.bresenhamCalculateLine(p2, p3)
        ).forEach { points ->
            points.forEach { p ->
                lowestXForY.computeIfAbsent(p.y) { p.x }
                highestXForY.computeIfAbsent(p.y) { p.x }

                lowestXForY.computeIfPresent(p.y) { _: Int, value: Int -> if (p.x < value) p.x else value}
                highestXForY.computeIfPresent(p.y) { _: Int, value: Int -> if (p.x > value) p.x else value}
            }
        }

        lowestXForY.forEach { (y: Int, x: Int) ->
            scanLine(x, highestXForY[y]!!, y, color)
        }
    }

    fun clear(rgb: RGB) = Arrays.fill(rgbComponents, rgb)

    fun clear(color: Color) = Arrays.fill(rgbComponents, color.toRGB())

    fun fillRGBIntArray(dest: IntArray) {
        for (i in 0 until width * height) {
            dest[i] = rgbComponents[i].toColor().value
        }
    }

    private fun isPointInCanvas(p: Point) =
        p.x in 0 until width && p.y in 0 until height

    /**
     * This methods works correctly if startX is before endX
     */
    private fun scanLine(startX: Int, endX: Int, y: Int, color: Color) {
        if ((y in 0 until height).not()) return
        val xs = if(startX < 0) 0 else startX
        val xe = if(endX >= width) width - 1 else endX

        (xs..xe).forEach { x -> drawPixel(x, y, color) }
    }

}