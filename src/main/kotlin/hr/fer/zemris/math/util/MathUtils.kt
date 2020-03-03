package hr.fer.zemris.math.util

import hr.fer.zemris.math.matrix.Matrix
import hr.fer.zemris.math.vector.Vector

fun vector(vararg values: Float) = Vector(floatArrayOf(*values))

fun vector(vararg values: Number) = Vector(values.map(Number::toFloat).toFloatArray())

fun matrix(vararg rows: FloatArray) = Matrix(arrayOf(*rows))

fun row(vararg values: Float) = floatArrayOf(*values)

fun row(vararg values: Number) = values.map(Number::toFloat).toFloatArray()
