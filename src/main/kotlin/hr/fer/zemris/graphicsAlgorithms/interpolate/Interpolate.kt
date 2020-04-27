package hr.fer.zemris.graphicsAlgorithms.interpolate

import hr.fer.zemris.extensions.toUnsignedInt
import hr.fer.zemris.graphicsAlgorithms.BarycentricCoordinates
import kotlin.math.roundToInt

object Interpolate {

    fun interpolateColorByte(c1: Byte, c2: Byte, c3: Byte, barycentricCoordinates: BarycentricCoordinates): Byte = with(barycentricCoordinates) {
        (c1.toUnsignedInt().toDouble() * w1 + c2.toUnsignedInt().toDouble() * w2 + c3.toUnsignedInt().toDouble() * w3)
            .roundToInt()
            .toByte()
    }

    fun interpolateDouble(d1: Double, d2: Double, d3: Double, barycentricCoordinates: BarycentricCoordinates) =
        with(barycentricCoordinates) {
            d1 * w1 + d2 * w2 + d3 * w3
        }
}