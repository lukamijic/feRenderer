package hr.fer.zemris.math.extensions

import hr.fer.zemris.math.vector.Vector

operator fun Float.times(vector: Vector) =
    vector * this