package hr.fer.zemris.math.exceptions

import hr.fer.zemris.math.matrix.Matrix
import java.lang.RuntimeException

sealed class MathException(message: String) : RuntimeException(message)

class VectorsCannotHaveZeroDimension()
    : MathException("Vector must have more than 1 dimension")

class IncompatibleVectorsException(dimension1: Int, dimension2: Int)
    : MathException("Vectors with dimensions $dimension1 and $dimension2 are not compatible")

class CrossProductIncompatibilityException(dimension1: Int, dimension2: Int)
    : MathException("To calculate cross product both vectors must have dimension three, given vectors had $dimension1 and $dimension2")

class MatrixCreationException(message: String)
    : MathException("All rows must be equal size")

class MatricesAreNotTheSameDimensionException(m1: Matrix, m2: Matrix)
    : MathException("Matrices (${m1.rows}, ${m1.columns}) and (${m2.rows}, ${m2.columns}) are not the same dimension.")

class MatricesAreNotCompatibleForMultiplicationException(m1: Matrix, m2: Matrix)
    : MathException("Matrices (${m1.rows}, ${m1.columns}) and (${m2.rows}, ${m2.columns}) are compatible for multiplication.")