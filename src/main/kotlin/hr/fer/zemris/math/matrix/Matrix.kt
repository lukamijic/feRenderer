package hr.fer.zemris.math.matrix

import hr.fer.zemris.math.exceptions.MatricesAreNotCompatibleForMultiplicationException
import hr.fer.zemris.math.exceptions.MatricesAreNotTheSameDimensionException
import hr.fer.zemris.math.exceptions.MatrixCannotBeTranformedIntoAVector
import hr.fer.zemris.math.exceptions.MatrixCreationException
import hr.fer.zemris.math.util.vector
import hr.fer.zemris.math.vector.Vector
import org.la4j.matrix.Matrices
import org.la4j.matrix.dense.Basic2DMatrix
import java.lang.StringBuilder

typealias La4jMatrix = org.la4j.matrix.Matrix

data class Matrix(
    private val values: Array<DoubleArray>
) {

    init {
        if (values.isEmpty()) throw MatrixCreationException("Number of rows can't be 0")

        val areRowsSameSize = values.asSequence().map(DoubleArray::lastIndex).distinct().count() == 1
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
                Array(rows) { i -> DoubleArray(columns) { j -> this[i, j] + other[i, j] } }
            )
        }

    operator fun minus(other: Matrix) =
        if (!areMatrixTheSameDimension(this, other)) {
            throw MatricesAreNotTheSameDimensionException(this, other)
        } else {
            Matrix(
                Array(rows) { i -> DoubleArray(columns) { j -> this[i, j] - other[i, j] } }
            )
        }

    operator fun times(factor: Double) =
        Matrix(
            Array(rows) { i -> DoubleArray(columns) { j -> factor * this[i, j] } }
        )

    operator fun times(other: Matrix) =
        if (columns != other.rows) {
            throw MatricesAreNotCompatibleForMultiplicationException(this, other)
        } else {
            val sharedDim = columns
            Matrix(
                Array(rows) { i ->
                    DoubleArray(other.columns) { j ->
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
                DoubleArray(rows) { j ->
                    this[j, i]
                }
            }
        )

    fun inverse(): Matrix {
        val la4jMatrix: La4jMatrix =
            Basic2DMatrix((Array(rows) { i -> DoubleArray(columns) { j -> values[i][j] } })).inverse(Matrices.DEFAULT_INVERTOR)

        return Matrix(
            Array(rows) {i -> DoubleArray(columns) {j -> la4jMatrix.get(i, j)} }
        )
    }

    fun toVector() =
        when {
            rows == 1 -> vector(*values[0])
            columns == 1 -> Vector(DoubleArray(columns) { i -> this[i, 0] })
            else -> throw MatrixCannotBeTranformedIntoAVector(this)
        }

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
    (0 until sharedDim).fold(0.0) { accumulated, currentIndex ->
        accumulated + (m1[row, currentIndex] * m2[currentIndex, column])
    }

private fun areMatrixTheSameDimension(m1: Matrix, m2: Matrix) =
    m1.rows == m2.rows && m1.columns == m2.columns