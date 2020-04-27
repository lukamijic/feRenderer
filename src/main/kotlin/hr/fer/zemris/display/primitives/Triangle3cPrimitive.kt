package hr.fer.zemris.display.primitives

import hr.fer.zemris.color.Color
import hr.fer.zemris.color.RGB
import hr.fer.zemris.display.Canvas
import hr.fer.zemris.extensions.toUnsignedInt
import hr.fer.zemris.geometry.model.Point
import hr.fer.zemris.geometry.model.Triangle
import hr.fer.zemris.graphicsAlgorithms.BarycentricCoordinates
import hr.fer.zemris.graphicsAlgorithms.BarycentricCoordinatesCalculator
import hr.fer.zemris.graphicsAlgorithms.draw.TrianglePointsProcessor
import hr.fer.zemris.graphicsAlgorithms.interpolate.Interpolate
import kotlin.math.roundToInt

data class Triangle3cPrimitive(
    val triangle: Triangle,
    val c1: Color,
    val c2: Color,
    val c3: Color
) : Primitive {

    override fun draw(canvas: Canvas) {
        val rgb1 = c1.toRGB()
        val rgb2 = c2.toRGB()
        val rgb3 = c3.toRGB()

        TrianglePointsProcessor(triangle, canvas.canvasBoundingBox)
            .processPoints { x, y, z ->
                val barycentricCoordinates = BarycentricCoordinatesCalculator.calculateBarycentricCoordinate(triangle, x, y)
                val color = RGB(
                    Interpolate.interpolateColorByte(rgb1.r, rgb2.r, rgb3.r, barycentricCoordinates),
                    Interpolate.interpolateColorByte(rgb1.g, rgb2.g, rgb3.g, barycentricCoordinates),
                    Interpolate.interpolateColorByte(rgb1.b, rgb2.b, rgb3.b, barycentricCoordinates)
                )
                canvas.drawPixel(x, y, z, color)
            }
    }
}