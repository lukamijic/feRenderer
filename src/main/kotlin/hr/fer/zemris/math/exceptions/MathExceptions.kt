package hr.fer.zemris.math.exceptions

import java.lang.RuntimeException

sealed class MathException(message: String) : RuntimeException(message)

class IncompatibleVectorsException(dimension1: Int, dimension2: Int)
    : MathException("Vectors with dimensions $dimension1 and $dimension2 are not compatible")

class CrossProductIncompatibilityException(dimension1: Int, dimension2: Int)
    : MathException("To calculate cross product both vectors must have dimension three, given vectors had $dimension1 and $dimension2")