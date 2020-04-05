package hr.fer.zemris.graphicsAlgorithms

import hr.fer.zemris.math.vector.Vector

private const val REQUIRED_DIMENS = 3

object NormalCalculator {

    /** Returns and requires 3D vector */
    fun calculate(v1: Vector, v2: Vector, v3: Vector): Vector {
        if (v1.dimension !=  REQUIRED_DIMENS || v2.dimension != REQUIRED_DIMENS || v3.dimension != REQUIRED_DIMENS) {
            throw IllegalArgumentException("All vectors must have dimension 3")
        }
        return ((v2 - v1) x (v3 - v1)).normalize()
    }
}