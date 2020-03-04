package hr.fer.zemris.math.vector

import hr.fer.zemris.math.exceptions.CrossProductIncompatibilityException
import hr.fer.zemris.math.exceptions.IncompatibleVectorsException
import hr.fer.zemris.math.exceptions.VectorsCannotHaveZeroDimension
import hr.fer.zemris.math.extensions.times
import hr.fer.zemris.math.util.vector
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.function.Executable
import java.lang.IndexOutOfBoundsException

private const val DELTA = 0.01f

class VectorTests {

    @Test
    fun `vector can't have zero values`() {
        assertThrows(VectorsCannotHaveZeroDimension::class.java) { Vector(floatArrayOf()) }
    }

    @Test
    fun `vector constructor`() {
        assertDoesNotThrow { Vector(floatArrayOf(1f, 2f, 4f)) }
        assertDoesNotThrow { Vector(floatArrayOf(1f)) }
        assertDoesNotThrow { Vector(floatArrayOf(1f, 25f, 312f, 3212f, 23f)) }
    }

    @Test
    fun `vector helper function creation`() {
        assertDoesNotThrow { vector(1, 2, 4f) }
        assertDoesNotThrow { vector(1f) }
        assertDoesNotThrow { vector(1, 2, 4f, 32, 31) }
    }

    @Test
    fun `should get access for normal vector construction`() {
        val v = Vector(floatArrayOf(1f, 0.5f, 0.01f, 0f))
        assertAll(
            Executable { assertEquals(1f, v[0], DELTA) },
            Executable { assertEquals(0.5f, v[1], DELTA) },
            Executable { assertEquals(0.01f, v[2], DELTA) },
            Executable { assertEquals(0.0f, v[3], DELTA) }
        )
    }

    @Test
    fun `should get access for vector(vararg values) construction`() {
        val v = vector(1, 0.5f, 0.01f, 0f)
        assertAll(
            Executable { assertEquals(1f, v[0], DELTA) },
            Executable { assertEquals(0.5f, v[1], DELTA) },
            Executable { assertEquals(0.01f, v[2], DELTA) },
            Executable { assertEquals(0.0f, v[3], DELTA) }
        )
    }

    @Test
    fun `should get vector access IndexOutOfBoundsException for negative index`() {
        val v = vector(1, 2.5f, 3.23f)
        assertThrows(IndexOutOfBoundsException::class.java) { v[-1] }
    }

    @Test
    fun `should get vector access IndexOutOfBoundsException where index is the same as dimension`() {
        val v = vector(1, 2.5f, 3.23f)
        assertThrows(IndexOutOfBoundsException::class.java) { v[v.dimension] }
    }

    @Test
    fun `should get vector access IndexOutOfBoundsException where index is the higher than dimension`() {
        val v = vector(1, 2.5f, 3.23f)
        assertThrows(IndexOutOfBoundsException::class.java) { v[v.dimension + 1] }
    }

    @Test
    fun `should throw IncompatibleVectorsException when adding two vectors of different size`() {
        assertThrows(IncompatibleVectorsException::class.java) {
            val v1 = vector(1, 2, 23)
            val v2 = vector(1f, 3f, 4, 2)
            v1 + v2
        }
    }

    @Test
    fun `should add two vectors`() {
        val v1 = vector(-1, 2.05f, 3.5)
        val v2 = Vector(floatArrayOf(3f, 2.1f, 2.5f))

        val vSum = v1 + v2
        assertArrayEquals(
            floatArrayOf(2f, 4.15f, 6f),
            floatArrayOf(vSum[0], vSum[1], vSum[2]),
            DELTA
        )
    }

    @Test
    fun `should throw IncompatibleVectorsException when subbing two vectors of different size`() {
        assertThrows(IncompatibleVectorsException::class.java) {
            val v1 = vector(1, 2, 23)
            val v2 = vector(1f, 3f, 4, 2)
            v1 - v2
        }
    }

    @Test
    fun `should sub two vectors`() {
        val v1 = vector(1, 2.05f, 3.5)
        val v2 = Vector(floatArrayOf(-3f, 2.1f, 2.5f))

        val vSum = v1 - v2
        assertArrayEquals(
            floatArrayOf(4f, -0.05f, 1f),
            floatArrayOf(vSum[0], vSum[1], vSum[2]),
            DELTA
        )
    }

    @Test
    fun `should multiply vector with factor`() {
        val v = vector(2.3f, -1.5f, 0.5f) * 2.5f
        assertArrayEquals(
            floatArrayOf(5.75f, -3.75f, 1.25f),
            floatArrayOf(v[0], v[1], v[2]),
            DELTA
        )
    }

    @Test
    fun `should multiply factor with vector`() {
        val v = 2.5f * vector(2.3f, -1.5f, 0.5f)
        assertArrayEquals(
            floatArrayOf(5.75f, -3.75f, 1.25f),
            floatArrayOf(v[0], v[1], v[2]),
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
        assertEquals(13.2372f, v1 * v2, DELTA)
    }

    @Test
    fun `should calculate correct norm for vector with one dimension`() {
        assertEquals(1.02f, vector(1.02f).norm, DELTA)
    }

    @Test
    fun `should calculate correct norm for vector with two dimensions and all positive values`() {
        assertEquals(
            2.709f,
            vector(1.532, 2.233).norm,
            DELTA
        )
    }

    @Test
    fun `should calculate correct norm for vector with multiple dimensions and all positive values`() {
        assertEquals(
            5.45922f,
            vector(1.23f, 0.27, 5.312).norm,
            DELTA
        )
    }

    @Test
    fun `should calculate correct norm for vector with two dimensions and all negative values`() {
        assertEquals(
            2.709f,
            vector(-1.532, -2.233).norm,
            DELTA
        )
    }

    @Test
    fun `should calculate correct norm for vector with multiple dimensions and all negative values`() {
        assertEquals(
            5.45922f,
            vector(-1.23f, -0.27, -5.312).norm,
            DELTA
        )
    }

    @Test
    fun `should calculate correct norm for vector with two dimensions and mixed values`() {
        assertEquals(
            2.709f,
            vector(-1.532, 2.233).norm,
            DELTA
        )
    }

    @Test
    fun `should calculate correct norm for vector with multiple dimensions and mixed values`() {
        assertEquals(
            5.45922f,
            vector(-1.23f, 0.27, -5.312).norm,
            DELTA
        )
    }

    @Test
    fun `should calculate correct normalized vector for positive values`() {
        val v = vector(1.23f, 0.27, 5.312).normalize()
        assertArrayEquals(
            floatArrayOf(0.2253f, 0.04945f, 0.9730f),
            floatArrayOf(v[0], v[1], v[2]),
            DELTA
        )
    }

    @Test
    fun `should calculate correct normalized vector for negative values`() {
        val v = vector(-1.23f, -0.27, -5.312).normalize()
        assertArrayEquals(
            floatArrayOf(-0.2253f, -0.04945f, -0.9730f),
            floatArrayOf(v[0], v[1], v[2]),
            DELTA
        )
    }

    @Test
    fun `should calculate correct normalized vector for mixed values`() {
        val v = vector(1.23f, -0.27, 5.312).normalize()
        assertArrayEquals(
            floatArrayOf(0.2253f, -0.04945f, 0.9730f),
            floatArrayOf(v[0], v[1], v[2]),
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
        val v1 = vector(2.23f, 0.0f, -3.213f)
        val v2 = vector(-0.233f, 32.423f, 0)
        val v = v1 x v2
        assertArrayEquals(
            floatArrayOf(104.175099f, 0.748629f, 72.30329f),
            floatArrayOf(v[0], v[1], v[2]),
            DELTA
        )
    }

    @Test
    fun `should throw IncompatibleVectorsException when trying to calculate cos between vectors of different dimensions`() {
        val v1 = vector(2.23f, 0.0f)
        val v2 = vector(-0.233f, 32.423f, 0)
        assertThrows(IncompatibleVectorsException::class.java) { v1 cos v2 }
        assertThrows(IncompatibleVectorsException::class.java) { v2 cos v1 }
    }

    @Test
    fun `should calculate correct cos for two vectors`() {
        val v1 = vector(2.23f, 0.0f, -3.213f)
        val v2 = vector(-0.233f, 32.423f, 0)
        assertEquals(-0.004097355f, v1 cos v2, DELTA)
    }
}