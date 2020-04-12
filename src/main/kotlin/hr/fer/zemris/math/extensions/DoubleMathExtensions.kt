package hr.fer.zemris.math.extensions

import hr.fer.zemris.math.matrix.Matrix
import hr.fer.zemris.math.vector.Vector

operator fun Double.times(vector: Vector) =
    vector * this

operator fun Double.times(matrix: Matrix) =
    matrix * this