package hr.fer.zemris.graphicsAlgorithms.util

import hr.fer.zemris.math.util.vector
import hr.fer.zemris.math.vector.Vector

private const val DIMEN_WITHOUT_HOMOGENEOUS_COORDINATE = 3
private const val DIMEN_WITH_HOMOGENEOUS_COORDINATE = 4

fun Vector.addHomogeneousCoordinate(): Vector {
    if (dimension != DIMEN_WITHOUT_HOMOGENEOUS_COORDINATE) {
        throw IllegalArgumentException("Vector must have dimension 3 to add homogeneous coordinate. It had $dimension")
    }

    return vector(this[0], this[1], this[2], 1)
}

fun Vector.removeHomogeneousCoordinate(): Vector {
    if (dimension != DIMEN_WITH_HOMOGENEOUS_COORDINATE) {
        throw IllegalArgumentException("Vector must have dimension 4 to remove homogeneous coordinate. It had $dimension")
    }

    return this.let { it * (1f / it[3]) }.let { vector(it[0], it[1], it[2]) }
}