package hr.fer.zemris.math.vector

import hr.fer.zemris.math.exceptions.CrossProductIncompatibilityException
import hr.fer.zemris.math.exceptions.IncompatibleVectorsException
import java.lang.IndexOutOfBoundsException
import kotlin.math.pow
import kotlin.math.sqrt

private const val CROSS_PRODUCT_DIMENSION = 3

data class Vector(
    private val values: FloatArray
) {

    val dimension = values.size

    val norm = norm()

    operator fun get(index: Int): Float =
        if (index >= dimension) {
            throw IndexOutOfBoundsException("Vector index $index is out of bounds [0, ${values.size - 1}]")
        } else {
            values[index]
        }

    operator fun plus(other: Vector) =
        if (dimension != other.dimension) {
            throw IncompatibleVectorsException(dimension, other.dimension)
        } else {
            Vector(
                FloatArray(dimension) { i -> this[i] + other[i] }
            )
        }

    operator fun minus(other: Vector) =
        if (dimension != other.dimension) {
            throw IncompatibleVectorsException(dimension, other.dimension)
        } else {
            Vector(
                FloatArray(dimension) { i -> this[i] - other[i] }
            )
        }

    operator fun times(factor: Float) =
        Vector(
            FloatArray(dimension) { i -> this[i] * factor }
        )

    operator fun times(other: Vector): Float =
        if (dimension != other.dimension) {
            throw IncompatibleVectorsException(dimension, other.dimension)
        } else {
            values.zip(other.values) { v1, v2 -> v1 * v2 }
                .fold(0f) { accumulated, currentValue -> accumulated + currentValue }
        }

    /**
     * This is vector cross product
     */
    infix fun x(other: Vector) =
        if (dimension != CROSS_PRODUCT_DIMENSION || other.dimension != CROSS_PRODUCT_DIMENSION) {
            throw CrossProductIncompatibilityException(dimension, other.dimension)
        } else {
            Vector(
                floatArrayOf(
                    this[1] * other[2] - this[2] * other[1],
                    this[2] * other[0] - this[0] * other[2],
                    this[0] * other[1] - this[1] * other[0]
                )
            )
        }

    /**
     * Calculates cos(l) = (v1 * v2) / (|v1| * |v2|)
     */
    infix fun cos(other: Vector) = (this * other) / (norm * other.norm )


    fun normalize() = this * (1.0f / norm)

    private fun norm() =
        sqrt(
            values
                .fold(0f) { accumulated, current -> accumulated + current.pow(2) }
        )


    override fun equals(other: Any?): Boolean =
        when (other) {
            is Vector -> values.contentEquals(other.values)
            else -> super.equals(other)
        }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}
