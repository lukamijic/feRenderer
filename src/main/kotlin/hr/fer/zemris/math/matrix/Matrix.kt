package hr.fer.zemris.math.matrix

import hr.fer.zemris.math.exceptions.MatricesAreNotCompatibleForMultiplicationException
import hr.fer.zemris.math.exceptions.MatricesAreNotTheSameDimensionException
import hr.fer.zemris.math.exceptions.MatrixCreationException
import java.lang.StringBuilder

data class Matrix(
    private val values: Array<FloatArray>
) {

    init {
        if (values.isEmpty()) throw MatrixCreationException("Number of rows can't be 0")

        val areRowsSameSize = values.asSequence().map(FloatArray::lastIndex).distinct().count() == 1
        if (!areRowsSameSize) throw MatrixCreationException("All rows must be equal size")

        if (values.first().isEmpty()) throw MatrixCreationException("Rows must have more than 0 value")
    }

    val rows = values.size

    val columns = values[0].size

    operator fun get(row: Int, column: Int) =
        if (row >= rows || column >= columns) {
            throw IndexOutOfBoundsException("Matrix position ($row, $column) is out of bounds ($rows, $columns)")
        } else {
            values[row][column]
        }

    operator fun plus(other: Matrix) =
        if (!areMatrixTheSameDimension(this, other)) {
            throw MatricesAreNotTheSameDimensionException(this, other)
        } else {
            Matrix(
                Array(rows) { i -> FloatArray(columns) { j -> this[i, j] + other[i, j] } }
            )
        }

    operator fun minus(other: Matrix) =
        if (!areMatrixTheSameDimension(this, other)) {
            throw MatricesAreNotTheSameDimensionException(this, other)
        } else {
            Matrix(
                Array(rows) { i -> FloatArray(columns) { j -> this[i, j] - other[i, j] } }
            )
        }

    operator fun times(factor: Float) =
        Matrix(
            Array(rows) { i -> FloatArray(columns) { j -> factor * this[i, j] } }
        )

    operator fun times(other: Matrix) =
        if (columns != other.rows) {
            throw MatricesAreNotCompatibleForMultiplicationException(this, other)
        } else {
            val sharedDim = columns
            Matrix(
                Array(rows) { i ->
                    FloatArray(other.columns) { j ->
                        calculateMultiplicationCell(
                            i,
                            j,
                            sharedDim,
                            this,
                            other
                        )
                    }
                }
            )
        }

    fun transpose() =
        Matrix(
            Array(columns) { i ->
                FloatArray(rows) { j ->
                    this[j, i]
                }
            }
        )


    override fun equals(other: Any?): Boolean =
        when (other) {
            is Matrix -> values.contentEquals(other.values)
            else -> super.equals(other)
        }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    override fun toString(): String =
        StringBuilder().apply {
            values.forEach { columns ->
                columns.forEach {
                    append("$it, ")
                }
                append("\n")
            }
        }.toString()
}

private fun calculateMultiplicationCell(row: Int, column: Int, sharedDim: Int, m1: Matrix, m2: Matrix) =
    (0 until sharedDim).fold(0f) { accumulated, currentIndex ->
        accumulated + (m1[row, currentIndex] * m2[currentIndex, column])
    }

private fun areMatrixTheSameDimension(m1: Matrix, m2: Matrix) =
    m1.rows == m2.rows && m1.columns == m2.columns