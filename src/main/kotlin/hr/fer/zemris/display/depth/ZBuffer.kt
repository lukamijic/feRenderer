package hr.fer.zemris.display.depth

import java.util.*

private const val MIN_Z_DEPTH = 0.0
private const val MAX_Z_DEPTH = 1.0
private val Z_DEPTH_RANGE = MIN_Z_DEPTH..MAX_Z_DEPTH

class ZBuffer(
    width: Int,
    height: Int
) {

    private val depthBuffer = DoubleArray(width * height) { MAX_Z_DEPTH }

    fun set(index: Int, depth: Double): Boolean =
        if (depth !in Z_DEPTH_RANGE) {
            false
        } else {
            if (depthBuffer[index] <= depth) {
                false
            } else {
                depthBuffer[index] = depth
                true
            }
        }


    fun reset() = Arrays.fill(depthBuffer, MAX_Z_DEPTH)
}