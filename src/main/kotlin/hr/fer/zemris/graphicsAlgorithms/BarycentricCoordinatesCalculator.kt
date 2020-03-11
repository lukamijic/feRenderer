package hr.fer.zemris.graphicsAlgorithms

import hr.fer.zemris.model.Triangle

object BarycentricCoordinatesCalculator {

    /**
     * Calculates barycentric coordinate for given point in relation to given triangle
     * Equation https://codeplea.com/triangular-interpolation
     */
    fun calculateBarycentricCoordinate(triangle: Triangle, x: Int, y: Int) = with(triangle) {
        val w1 = ((p2.y - p3.y) * (x - p3.x) + (p3.x - p2.x) * (y - p3.y)).toFloat() /
                ((p2.y - p3.y) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.y - p3.y)).toFloat()

        val w2 = ((p3.y - p1.y) * (x - p3.x) + (p1.x - p3.x) * (y - p3.y)).toFloat() /
                ((p2.y - p3.y) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.y - p3.y)).toFloat()

        val w3 = 1 - w1 - w2

        BarycentricCoordinates(w1, w2, w3)
    }

    fun interpolateColorByte(c1: Byte, c2: Byte, c3: Byte, barycentricCoordinates: BarycentricCoordinates) {

    }
}


data class BarycentricCoordinates(
    val w1: Float,
    val w2: Float,
    val w3: Float
)