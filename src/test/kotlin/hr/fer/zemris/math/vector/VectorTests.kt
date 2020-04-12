package hr.fer.zemris.math.vector

import hr.fer.zemris.math.exceptions.CrossProductIncompatibilityException
import hr.fer.zemris.math.exceptions.IncompatibleVectorsException
import hr.fer.zemris.math.exceptions.VectorsCannotHaveZeroDimension
import hr.fer.zemris.math.util.vector
import hr.fer.zemris.math.extensions.times
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.function.Executable
import java.lang.IndexOutOfBoundsException

private const val DELTA = 0.01

class VectorTests {

    @Test
    fun `vector can't have zero values`() {
        assertThrows(VectorsCannotHaveZeroDimension::class.java) { Vector(doubleArrayOf()) }
    }

    @Test
    fun `vector constructor`() {
        assertDoesNotThrow { Vector(doubleArrayOf(1.0, 2.0, 4.0)) }
        assertDoesNotThrow { Vector(doubleArrayOf(1.0)) }
        assertDoesNotThrow { Vector(doubleArrayOf(1.0, 25.0, 312.0, 3212.0, 23.0)) }
    }

    @Test
    fun `vector helper function creation`() {
        assertDoesNotThrow { vector(1, 2, 4) }
        assertDoesNotThrow { vector(1) }
        assertDoesNotThrow { vector(1, 2, 4, 32, 31) }
    }

    @Test
    fun `should get access for normal vector construction`() {
        val v = Vector(doubleArrayOf(1.0, 0.5, 0.01, 0.0))
        assertAll(
            Executable { assertEquals(1.0, v[0], DELTA) },
            Executable { assertEquals(0.5, v[1], DELTA) },
            Executable { assertEquals(0.01, v[2], DELTA) },
            Executable { assertEquals(0.0, v[3], DELTA) }
        )
    }

    @Test
    fun `should get access for vector(vararg values) construction`() {
        val v = vector(1, 0.5, 0.01, 0)
        assertAll(
            Executable { assertEquals(1.0, v[0], DELTA) },
            Executable { assertEquals(0.5, v[1], DELTA) },
            Executable { assertEquals(0.01, v[2], DELTA) },
            Executable { assertEquals(0.0, v[3], DELTA) }
        )
    }

    @Test
    fun `should get vector access IndexOutOfBoundsException for negative index`() {
        val v = vector(1, 2.5, 3.23)
        assertThrows(IndexOutOfBoundsException::class.java) { v[-1] }
    }

    @Test
    fun `should get vector access IndexOutOfBoundsException where index is the same as dimension`() {
        val v = vector(1, 2.5, 3.23)
        assertThrows(IndexOutOfBoundsException::class.java) { v[v.dimension] }
    }

    @Test
    fun `should get vector access IndexOutOfBoundsException where index is the higher than dimension`() {
        val v = vector(1, 2.5, 3.23)
        assertThrows(IndexOutOfBoundsException::class.java) { v[v.dimension + 1] }
    }

    @Test
    fun `should throw IncompatibleVectorsException when adding two vectors of different size`() {
        assertThrows(IncompatibleVectorsException::class.java) {
            val v1 = vector(1, 2, 23)
            val v2 = vector(1, 3, 4, 2)
            v1 + v2
        }
    }

    @Test
    fun `should add two vectors`() {
        val v1 = vector(-1, 2.05, 3.5)
        val v2 = Vector(doubleArrayOf(3.0, 2.1, 2.5))

        val vSum = v1 + v2
        assertArrayEquals(
            doubleArrayOf(2.0, 4.15, 6.0),
            doubleArrayOf(vSum[0], vSum[1], vSum[2]),
            DELTA
        )
    }

    @Test
    fun `should throw IncompatibleVectorsException when subbing two vectors of different size`() {
        assertThrows(IncompatibleVectorsException::class.java) {
            val v1 = vector(1, 2, 23)
            val v2 = vector(1, 3, 4, 2)
            v1 - v2
        }
    }

    @Test
    fun `should sub two vectors`() {
        val v1 = vector(1, 2.05, 3.5)
        val v2 = Vector(doubleArrayOf(-3.0, 2.1, 2.5))

        val vSum = v1 - v2
        assertArrayEquals(
            doubleArrayOf(4.0, -0.05, 1.0),
            doubleArrayOf(vSum[0], vSum[1], vSum[2]),
            DELTA
        )
    }

    @Test
    fun `should multiply vector with factor`() {
        val v = vector(2.3, -1.5, 0.5) * 2.5
        assertArrayEquals(
            doubleArrayOf(5.75, -3.75, 1.25),
            doubleArrayOf(v[0], v[1], v[2]),
            DELTA
        )
    }

    @Test
    fun `should multiply factor with vector`() {
        val v = 2.5 * vector(2.3, -1.5, 0.5)
        assertArrayEquals(
            doubleArrayOf(5.75, -3.75, 1.25),
            doubleArrayOf(v[0], v[1], v[2]),
            DELTA
        )
    }

    @Test
    fun `should throw IncompatibleVectorsException when multiplying two vectors of different size`() {
        val v1 = vector(1, 2.5, 3.2)
        val v2 = vector(1, 2.5)
        assertThrows(IncompatibleVectorsException::class.java) { v1 * v2 }
    }

    @Test
    fun `should multiply vectors`() {
        val v1 = vector(1, 2.5, 3.2)
        val v2 = vector(-0.44, 2.5, 2.321)
        assertEquals(13.2372, v1 * v2, DELTA)
    }

    @Test
    fun `should calculate correct norm for vector with one dimension`() {
        assertEquals(1.02, vector(1.02).norm, DELTA)
    }

    @Test
    fun `should calculate correct norm for vector with two dimensions and all positive values`() {
        assertEquals(
            2.709,
            vector(1.532, 2.233).norm,
            DELTA
        )
    }

    @Test
    fun `should calculate correct norm for vector with multiple dimensions and all positive values`() {
        assertEquals(
            5.45922,
            vector(1.23, 0.27, 5.312).norm,
            DELTA
        )
    }

    @Test
    fun `should calculate correct norm for vector with two dimensions and all negative values`() {
        assertEquals(
            2.709,
            vector(-1.532, -2.233).norm,
            DELTA
        )
    }

    @Test
    fun `should calculate correct norm for vector with multiple dimensions and all negative values`() {
        assertEquals(
            5.45922,
            vector(-1.23, -0.27, -5.312).norm,
            DELTA
        )
    }

    @Test
    fun `should calculate correct norm for vector with two dimensions and mixed values`() {
        assertEquals(
            2.709,
            vector(-1.532, 2.233).norm,
            DELTA
        )
    }

    @Test
    fun `should calculate correct norm for vector with multiple dimensions and mixed values`() {
        assertEquals(
            5.45922,
            vector(-1.23, 0.27, -5.312).norm,
            DELTA
        )
    }

    @Test
    fun `should calculate correct normalized vector for positive values`() {
        val v = vector(1.23, 0.27, 5.312).normalize()
        assertArrayEquals(
            doubleArrayOf(0.2253, 0.04945, 0.9730),
            doubleArrayOf(v[0], v[1], v[2]),
            DELTA
        )
    }

    @Test
    fun `should calculate correct normalized vector for negative values`() {
        val v = vector(-1.23, -0.27, -5.312).normalize()
        assertArrayEquals(
            doubleArrayOf(-0.2253, -0.04945, -0.9730),
            doubleArrayOf(v[0], v[1], v[2]),
            DELTA
        )
    }

    @Test
    fun `should calculate correct normalized vector for mixed values`() {
        val v = vector(1.23, -0.27, 5.312).normalize()
        assertArrayEquals(
            doubleArrayOf(0.2253, -0.04945, 0.9730),
            doubleArrayOf(v[0], v[1], v[2]),
            DELTA
        )
    }

    @Test
    fun `should throw CrossProductIncompatibilityException when both don't have dimension 3`() {
        val v1 = vector(1, 2)
        val v2 = vector(1, 2, 4, 5)
        assertThrows(CrossProductIncompatibilityException::class.java) { v1 x v2 }
    }

    @Test
    fun `should throw CrossProductIncompatibilityException when first vector isn't dimension 3`() {
        val v1 = vector(1, 2)
        val v2 = vector(1, 2, 4)
        assertThrows(CrossProductIncompatibilityException::class.java) { v1 x v2 }
    }

    @Test
    fun `should throw CrossProductIncompatibilityException when second vector isn't dimension 3`() {
        val v1 = vector(1, 2, 4)
        val v2 = vector(1, 2)
        assertThrows(CrossProductIncompatibilityException::class.java) { v1 x v2 }
    }

    @Test
    fun `should calculate correct cross product`() {
        val v1 = vector(2.23, 0.0, -3.213)
        val v2 = vector(-0.233, 32.423, 0)
        val v = v1 x v2
        assertArrayEquals(
            doubleArrayOf(104.175099, 0.748629, 72.30329),
            doubleArrayOf(v[0], v[1], v[2]),
            DELTA
        )
    }

    @Test
    fun `should throw IncompatibleVectorsException when trying to calculate cos between vectors of different dimensions`() {
        val v1 = vector(2.23, 0.0)
        val v2 = vector(-0.233, 32.423, 0)
        assertThrows(IncompatibleVectorsException::class.java) { v1 cos v2 }
        assertThrows(IncompatibleVectorsException::class.java) { v2 cos v1 }
    }

    @Test
    fun `should calculate correct cos for two vectors`() {
        val v1 = vector(2.23, 0.0, -3.213)
        val v2 = vector(-0.233, 32.423, 0)
        assertEquals(-0.004097355, v1 cos v2, DELTA)
    }
}