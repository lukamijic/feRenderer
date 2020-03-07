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

private const val DELTA = 0.01f

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
                    floatArrayOf(1f, 3f, 2.32f),
                    floatArrayOf(1f, 1.2f, 2.1f),
                    floatArrayOf()
                )
            )
        }
    }

    @Test
    fun `matrix constructor creation`() {
        assertDoesNotThrow {
            Matrix(
                arrayOf(
                    floatArrayOf(1f, 3f, 2.32f),
                    floatArrayOf(1f, 1.2f, 2.1f),
                    floatArrayOf(12.23f, 23f, 32f)
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
                floatArrayOf(1f, 3f, 4.23f),
                floatArrayOf(1.3f, 3.3f, 4.23f)
            )
        )
        assertAll(
            Executable { assertEquals(1f, m[0, 0], DELTA) },
            Executable { assertEquals(3f, m[0, 1], DELTA) },
            Executable { assertEquals(4.23f, m[0, 2], DELTA) },
            Executable { assertEquals(1.3f, m[1, 0], DELTA) },
            Executable { assertEquals(3.3f, m[1, 1], DELTA) },
            Executable { assertEquals(4.23f, m[1, 2], DELTA) }
        )
    }

    @Test
    fun `should get access for matrix(vararg values) matrix construction`() {
        val m = matrix(
            row(1, 3, 4.23f),
            row(1.3f, 3.3f, 4.23f)
        )
        assertAll(
            Executable { assertEquals(1f, m[0, 0], DELTA) },
            Executable { assertEquals(3f, m[0, 1], DELTA) },
            Executable { assertEquals(4.23f, m[0, 2], DELTA) },
            Executable { assertEquals(1.3f, m[1, 0], DELTA) },
            Executable { assertEquals(3.3f, m[1, 1], DELTA) },
            Executable { assertEquals(4.23f, m[1, 2], DELTA) }
        )
    }

    @Test
    fun `should get matrix access IndexOutOfBoundsException for row negative index`() {
        val m = matrix(
            row(1, 3, 4.23f),
            row(1.3f, 3.3f, 4.23f)
        )

        assertThrows(IndexOutOfBoundsException::class.java) { m[-1, 0] }
    }

    @Test
    fun `should get matrix access IndexOutOfBoundsException for column negative index`() {
        val m = matrix(
            row(1, 3, 4.23f),
            row(1.3f, 3.3f, 4.23f)
        )

        assertThrows(IndexOutOfBoundsException::class.java) { m[0, -1] }
    }

    @Test
    fun `should get matrix access IndexOutOfBoundsException for row and column negative index`() {
        val m = matrix(
            row(1, 3, 4.23f),
            row(1.3f, 3.3f, 4.23f)
        )

        assertThrows(IndexOutOfBoundsException::class.java) { m[-1, -1] }
    }

    @Test
    fun `should get matrix access IndexOutOfBoundsException where row index is higher than row dimension`() {
        val m = matrix(
            row(1, 3, 4.23f),
            row(1.3f, 3.3f, 4.23f)
        )

        assertThrows(IndexOutOfBoundsException::class.java) { m[2, 0] }
    }

    @Test
    fun `should get matrix access IndexOutOfBoundsException where column index is higher than column dimension`() {
        val m = matrix(
            row(1, 3, 4.23f),
            row(1.3f, 3.3f, 4.23f)
        )

        assertThrows(IndexOutOfBoundsException::class.java) { m[0, 3] }
    }

    @Test
    fun `should get matrix access IndexOutOfBoundsException where both row and columns are out of bounds`() {
        val m = matrix(
            row(1, 3, 4.23f),
            row(1.3f, 3.3f, 4.23f)
        )

        assertThrows(IndexOutOfBoundsException::class.java) { m[2, 3] }
    }

    @Test
    fun `should throw MatricesAreNotTheSameDimensionException when adding two matrices of different dimension`() {
        val m1 = matrix(
            row(1, 3, 4.23f),
            row(1.3f, 3.3f, 4.23f)
        )

        val m2 = matrix(
            row(1, 3, 4.23f),
            row(1.3f, 3.3f, 4.23f),
            row(1.3f, 3.3f, 4.23f)
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
            row(1, 3, 4.23f),
            row(1.3f, 3.3f, 4.23f)
        )

        val m2 = matrix(
            row(1, 3, 4.23f),
            row(1.3f, 3.3f, 4.23f),
            row(1.3f, 3.3f, 4.23f)
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
            row(1, 3, 4.23f),
            row(1.3f, 3.3f, 4.23f)
        )

        val m2 = matrix(
            row(1, 3, 4.23f),
            row(1.3f, 3.3f, 4.23f)
        )

        val m = m1 + m2
        assertAll(
            Executable { assertArrayEquals(floatArrayOf(2f, 6f, 8.46f), m.extractMatrixRow(0), DELTA) },
            Executable { assertArrayEquals(floatArrayOf(2.6f, 6.6f, 8.46f), m.extractMatrixRow(1), DELTA) }
        )
    }

    @Test
    fun `should sub two matrices`() {
        val m1 = matrix(
            row(1, 3, 4.23f),
            row(1.3f, 3.3f, 4.23f)
        )

        val m2 = matrix(
            row(1, 3, 4.23f),
            row(1.3f, 3.3f, 4.23f)
        )

        val m = m1 - m2
        assertAll(
            Executable { assertArrayEquals(floatArrayOf(0f, 0f, 0f), m.extractMatrixRow(0), DELTA) },
            Executable { assertArrayEquals(floatArrayOf(0f, 0f, 0f), m.extractMatrixRow(1), DELTA) }
        )
    }

    @Test
    fun `should multiply matrix with factor`() {
        val m1 = matrix(
            row(1, 3, 4.23f),
            row(1.3f, 3.3f, 4.23f)
        )

        val m = m1 * 2f
        assertAll(
            Executable { assertArrayEquals(floatArrayOf(2f, 6f, 8.46f), m.extractMatrixRow(0), DELTA) },
            Executable { assertArrayEquals(floatArrayOf(2.6f, 6.6f, 8.46f), m.extractMatrixRow(1), DELTA) }
        )
    }

    @Test
    fun `should multiply factor with vector`() {
        val m1 = matrix(
            row(1, 3, 4.23f),
            row(1.3f, 3.3f, 4.23f)
        )

        val m = 2f * m1
        assertAll(
            Executable { assertArrayEquals(floatArrayOf(2f, 6f, 8.46f), m.extractMatrixRow(0), DELTA) },
            Executable { assertArrayEquals(floatArrayOf(2.6f, 6.6f, 8.46f), m.extractMatrixRow(1), DELTA) }
        )
    }

    @Test
    fun `should do correct transpose`() {
        val m = matrix(
            row(1, 3, 4.23f),
            row(1.3f, 3.3f, 4.23f)
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
    FloatArray(columns) { j -> this[rowIndex, j] }


private fun Matrix.extractMatrixColumn(columnIndex: Int) =
    FloatArray(rows) { i -> this[i, columnIndex] }
