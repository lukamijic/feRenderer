package hr.fer.zemris.graphicsAlgorithms

import hr.fer.zemris.extensions.toUnsignedInt
import hr.fer.zemris.geometry.model.Triangle
import kotlin.math.roundToInt

object BarycentricCoordinatesCalculator {

    /**
     * Calculates barycentric coordinate for given point in relation to given triangle
     * Equation https://codeplea.com/triangular-interpolation
     */
    fun calculateBarycentricCoordinate(triangle: Triangle, x: Int, y: Int) = with(triangle) {
        val w1 = ((p2.y - p3.y) * (x - p3.x) + (p3.x - p2.x) * (y - p3.y)).toDouble() /
                ((p2.y - p3.y) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.y - p3.y)).toDouble()

        val w2 = ((p3.y - p1.y) * (x - p3.x) + (p1.x - p3.x) * (y - p3.y)).toDouble() /
                ((p2.y - p3.y) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.y - p3.y)).toDouble()

        val w3 = 1 - w1 - w2

        BarycentricCoordinates(w1, w2, w3)
    }

    fun interpolateColorByte(c1: Byte, c2: Byte, c3: Byte, barycentricCoordinates: BarycentricCoordinates): Byte = with(barycentricCoordinates) {
        (c1.toUnsignedInt().toDouble() * w1 + c2.toUnsignedInt().toDouble() * w2 + c3.toUnsignedInt().toDouble() * w3)
            .roundToInt()
            .toByte()
    }
}


data class BarycentricCoordinates(
    val w1: Double,
    val w2: Double,
    val w3: Double
)