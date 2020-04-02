package hr.fer.zemris.display

import hr.fer.zemris.color.Color
import hr.fer.zemris.color.RGB
import hr.fer.zemris.graphicsAlgorithms.BarycentricCoordinatesCalculator
import hr.fer.zemris.graphicsAlgorithms.BresenhamLineAlgorithm
import hr.fer.zemris.geometry.model.Point
import hr.fer.zemris.geometry.model.Triangle
import hr.fer.zemris.graphicsAlgorithms.draw.TrianglePointsProcessor
import hr.fer.zemris.graphicsAlgorithms.lineclipping.CohenSutherlandLineClippingAlgorithm
import hr.fer.zemris.graphicsAlgorithms.lineclipping.LineClipping
import hr.fer.zemris.graphicsAlgorithms.util.BoundingBox
import java.lang.IndexOutOfBoundsException
import java.util.*

class Canvas(
    val width: Int,
    val height: Int,
    var lineClipping: LineClipping = CohenSutherlandLineClippingAlgorithm(0, width - 1, 0, height - 1)
) {

    private val widthRange = 0 until width
    private val heightRange = 0 until height
    private val boundingBox = BoundingBox(0, width - 1, 0, height - 1)
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
        lineClipping
            .clip(p1, p2)?.let { (p1, p2) ->
                BresenhamLineAlgorithm.bresenhamCalculateLine(p1, p2)
                    .filter(::isPointInCanvas)
                    .forEach { p ->
                        drawPixel(p.x, p.y, rgb)
                    }
            }

    fun drawLine(p1: Point, p2: Point, color: Color) =
        drawLine(p1, p2, color.toRGB())

    fun drawTriangle(triangle: Triangle, color: Color) =
        with(triangle) {
            drawLine(p1, p2, color)
            drawLine(p1, p3, color)
            drawLine(p2, p3, color)
        }

    fun fillTriangle(triangle: Triangle, rgb: RGB) =
        fillTriangle(triangle) { _, _ -> rgb }

    fun fillTriangle(triangle: Triangle, color: Color) =
        fillTriangle(triangle, color.toRGB())

    fun fillTriangle(triangle: Triangle, rgb1: RGB, rgb2: RGB, rgb3: RGB) =
        fillTriangle(triangle) { i, j ->
            val barycentricCoordinates = BarycentricCoordinatesCalculator.calculateBarycentricCoordinate(triangle, i, j)
            RGB(
                BarycentricCoordinatesCalculator.interpolateColorByte(rgb1.r, rgb2.r, rgb3.r, barycentricCoordinates),
                BarycentricCoordinatesCalculator.interpolateColorByte(rgb1.g, rgb2.g, rgb3.g, barycentricCoordinates),
                BarycentricCoordinatesCalculator.interpolateColorByte(rgb1.b, rgb2.b, rgb3.b, barycentricCoordinates)
            )
        }

    fun fillTriangle(triangle: Triangle, c1: Color, c2: Color, c3: Color) =
        fillTriangle(triangle, c1.toRGB(), c2.toRGB(), c3.toRGB())

    fun fillTriangle(triangle: Triangle, colorGetterForPixel: (Int, Int) -> RGB) {
        TrianglePointsProcessor(triangle, boundingBox)
            .processPoints { x, y -> drawPixel(x, y, colorGetterForPixel(x, y)) }
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
}