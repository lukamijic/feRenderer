package hr.fer.zemris.math.matrix

import hr.fer.zemris.math.exceptions.MatricesAreNotCompatibleForMultiplicationException
import hr.fer.zemris.math.exceptions.MatricesAreNotTheSameDimensionException
import hr.fer.zemris.math.exceptions.MatrixCreationException
import hr.fer.zemris.math.extensions.times
import hr.fer.zemris.math.util.matrix
import hr.fer.zemris.math.util.row
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.function.Executable
import java.lang.IndexOutOfBoundsException

private const val DELTA = 0.01

class MatrixTests {

    @Test
    fun `matrix must have more than 0 rows`() {
        assertThrows(MatrixCreationException::class.java) { Matrix(emptyArray()) }
    }

    @Test
    fun `matrix must have all rows of same size`() {
        assertThrows(MatrixCreationException::class.java) {
            matrix(
                row(1, 2, 3),
                row(1, 2, 3),
                row(1)
            )
        }
    }

    @Test
    fun `matrix rows must have atleast one value`() {
        assertThrows(MatrixCreationException::class.java) {
            Matrix(
                arrayOf(
                    doubleArrayOf(1.0, 3.0, 2.32),
                    doubleArrayOf(1.0, 1.2, 2.1),
                    doubleArrayOf()
                )
            )
        }
    }

    @Test
    fun `matrix constructor creation`() {
        assertDoesNotThrow {
            Matrix(
                arrayOf(
                    doubleArrayOf(1.0, 3.0, 2.32),
                    doubleArrayOf(1.0, 1.2, 2.1),
                    doubleArrayOf(12.23, 23.0, 32.0)
                )
            )
        }
    }

    @Test
    fun `matrix helper function creation`() {
        assertDoesNotThrow {
            matrix(
                row(1, 3),
                row(1.32, 3),
                row(1.04, 1),
                row(1, 2.3),
                row(1, 1)
            )
        }
    }

    @Test
    fun `should get access for normal matrix construction`() {
        val m = Matrix(
            arrayOf(
                doubleArrayOf(1.0, 3.0, 4.23),
                doubleArrayOf(1.3, 3.3, 4.23)
            )
        )
        assertAll(
            Executable { assertEquals(1.0, m[0, 0], DELTA) },
            Executable { assertEquals(3.0, m[0, 1], DELTA) },
            Executable { assertEquals(4.23, m[0, 2], DELTA) },
            Executable { assertEquals(1.3, m[1, 0], DELTA) },
            Executable { assertEquals(3.3, m[1, 1], DELTA) },
            Executable { assertEquals(4.23, m[1, 2], DELTA) }
        )
    }

    @Test
    fun `should get access for matrix(vararg values) matrix construction`() {
        val m = matrix(
            row(1, 3, 4.23),
            row(1.3, 3.3, 4.23)
        )
        assertAll(
            Executable { assertEquals(1.0, m[0, 0], DELTA) },
            Executable { assertEquals(3.0, m[0, 1], DELTA) },
            Executable { assertEquals(4.23, m[0, 2], DELTA) },
            Executable { assertEquals(1.3, m[1, 0], DELTA) },
            Executable { assertEquals(3.3, m[1, 1], DELTA) },
            Executable { assertEquals(4.23, m[1, 2], DELTA) }
        )
    }

    @Test
    fun `should get matrix access IndexOutOfBoundsException for row negative index`() {
        val m = matrix(
            row(1, 3, 4.23),
            row(1.3, 3.3, 4.23)
        )

        assertThrows(IndexOutOfBoundsException::class.java) { m[-1, 0] }
    }

    @Test
    fun `should get matrix access IndexOutOfBoundsException for column negative index`() {
        val m = matrix(
            row(1, 3, 4.23),
            row(1.3, 3.3, 4.23)
        )

        assertThrows(IndexOutOfBoundsException::class.java) { m[0, -1] }
    }

    @Test
    fun `should get matrix access IndexOutOfBoundsException for row and column negative index`() {
        val m = matrix(
            row(1, 3, 4.23),
            row(1.3, 3.3, 4.23)
        )

        assertThrows(IndexOutOfBoundsException::class.java) { m[-1, -1] }
    }

    @Test
    fun `should get matrix access IndexOutOfBoundsException where row index is higher than row dimension`() {
        val m = matrix(
            row(1, 3, 4.23),
            row(1.3, 3.3, 4.23)
        )

        assertThrows(IndexOutOfBoundsException::class.java) { m[2, 0] }
    }

    @Test
    fun `should get matrix access IndexOutOfBoundsException where column index is higher than column dimension`() {
        val m = matrix(
            row(1, 3, 4.23),
            row(1.3, 3.3, 4.23)
        )

        assertThrows(IndexOutOfBoundsException::class.java) { m[0, 3] }
    }

    @Test
    fun `should get matrix access IndexOutOfBoundsException where both row and columns are out of bounds`() {
        val m = matrix(
            row(1, 3, 4.23),
            row(1.3, 3.3, 4.23)
        )

        assertThrows(IndexOutOfBoundsException::class.java) { m[2, 3] }
    }

    @Test
    fun `should throw MatricesAreNotTheSameDimensionException when adding two matrices of different dimension`() {
        val m1 = matrix(
            row(1, 3, 4.23),
            row(1.3, 3.3, 4.23)
        )

        val m2 = matrix(
            row(1, 3, 4.23),
            row(1.3, 3.3, 4.23),
            row(1.3, 3.3, 4.23)
        )

        assertThrows(MatricesAreNotTheSameDimensionException::class.java) { m1 + m2}

        val m3 = matrix(
            row(1, 2, 3),
            row(1, 2, 3)
        )

        val m4 = matrix(
            row(1, 2),
            row(1, 2)
        )

        assertThrows(MatricesAreNotTheSameDimensionException::class.java) { m3 + m4}
    }

    @Test
    fun `should throw MatricesAreNotTheSameDimensionException when subbing two matrices of different dimension`(){
        val m1 = matrix(
            row(1, 3, 4.23),
            row(1.3, 3.3, 4.23)
        )

        val m2 = matrix(
            row(1, 3, 4.23),
            row(1.3, 3.3, 4.23),
            row(1.3, 3.3, 4.23)
        )

        assertThrows(MatricesAreNotTheSameDimensionException::class.java) { m1 - m2 }

        val m3 = matrix(
            row(1, 2, 3),
            row(1, 2, 3)
        )

        val m4 = matrix(
            row(1, 2),
            row(1, 2)
        )

        assertThrows(MatricesAreNotTheSameDimensionException::class.java) { m3 - m4 }
    }

    @Test
    fun `should add two matrices`() {
        val m1 = matrix(
            row(1, 3, 4.23),
            row(1.3, 3.3, 4.23)
        )

        val m2 = matrix(
            row(1, 3, 4.23),
            row(1.3, 3.3, 4.23)
        )

        val m = m1 + m2
        assertAll(
            Executable { assertArrayEquals(doubleArrayOf(2.0, 6.0, 8.46), m.extractMatrixRow(0), DELTA) },
            Executable { assertArrayEquals(doubleArrayOf(2.6, 6.6, 8.46), m.extractMatrixRow(1), DELTA) }
        )
    }

    @Test
    fun `should sub two matrices`() {
        val m1 = matrix(
            row(1, 3, 4.23),
            row(1.3, 3.3, 4.23)
        )

        val m2 = matrix(
            row(1, 3, 4.23),
            row(1.3, 3.3, 4.23)
        )

        val m = m1 - m2
        assertAll(
            Executable { assertArrayEquals(doubleArrayOf(0.0, 0.0, 0.0), m.extractMatrixRow(0), DELTA) },
            Executable { assertArrayEquals(doubleArrayOf(0.0, 0.0, 0.0), m.extractMatrixRow(1), DELTA) }
        )
    }

    @Test
    fun `should multiply matrix with factor`() {
        val m1 = matrix(
            row(1, 3, 4.23),
            row(1.3, 3.3, 4.23)
        )

        val m = m1 * 2.0
        assertAll(
            Executable { assertArrayEquals(doubleArrayOf(2.0, 6.0, 8.46), m.extractMatrixRow(0), DELTA) },
            Executable { assertArrayEquals(doubleArrayOf(2.6, 6.6, 8.46), m.extractMatrixRow(1), DELTA) }
        )
    }

    @Test
    fun `should multiply factor with vector`() {
        val m1 = matrix(
            row(1, 3, 4.23),
            row(1.3, 3.3, 4.23)
        )

        val m = 2.0 * m1
        assertAll(
            Executable { assertArrayEquals(doubleArrayOf(2.0, 6.0, 8.46), m.extractMatrixRow(0), DELTA) },
            Executable { assertArrayEquals(doubleArrayOf(2.6, 6.6, 8.46), m.extractMatrixRow(1), DELTA) }
        )
    }

    @Test
    fun `should do correct transpose`() {
        val m = matrix(
            row(1, 3, 4.23),
            row(1.3, 3.3, 4.23)
        )

        val mt = m.transpose()
        assertAll(
            Executable { assertArrayEquals(m.extractMatrixColumn(0), mt.extractMatrixRow(0), DELTA) },
            Executable { assertArrayEquals(m.extractMatrixColumn(1), mt.extractMatrixRow(1), DELTA) }
        )
    }

    @Test
    fun `should throw MatricesAreNotCompatibleForMultiplicationException when multiplying matrices with not compatible dimens`() {
        val m1 = matrix(
            row(1, 2, 3),
            row(1, 2, 3),
            row(1, 2, 3),
            row(1, 2, 3)
        )

        val m2 = matrix(
            row(1, 2, 3),
            row(1, 2, 3)
        )

        assertThrows(MatricesAreNotCompatibleForMultiplicationException::class.java) { m1 * m2}
        assertThrows(MatricesAreNotCompatibleForMultiplicationException::class.java) { m2 * m1}
    }

    @Test
    fun `should do correct matrix multiplication`() {
        val m1 = matrix(
            row(1, 2, 2.5, 4.5),
            row(1.5, 2, 2.2, 3.3),
            row(2, 1.3, 2.2, 1.2)
        )

        val m2 = matrix(
            row(2.2, 1.3),
            row(3.22, 2.4),
            row(4.5, 0.3),
            row(3.5, 1.3)
        )

        val m = m1 * m2
        assertAll(
            Executable { assertArrayEquals(row(35.64, 12.7), m.extractMatrixRow(0), DELTA) },
            Executable { assertArrayEquals(row(31.19, 11.7), m.extractMatrixRow(1), DELTA) },
            Executable { assertArrayEquals(row(22.686, 7.94), m.extractMatrixRow(2), DELTA) }
        )
    }
}

private fun Matrix.extractMatrixRow(rowIndex: Int) =
    DoubleArray(columns) { j -> this[rowIndex, j] }


private fun Matrix.extractMatrixColumn(columnIndex: Int) =
    DoubleArray(rows) { i -> this[i, columnIndex] }
