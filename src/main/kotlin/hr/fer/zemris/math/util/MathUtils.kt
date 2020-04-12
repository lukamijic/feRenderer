package hr.fer.zemris.math.util

import hr.fer.zemris.math.matrix.Matrix
import hr.fer.zemris.math.vector.Vector
import kotlin.math.round

fun vector(vararg values: Double) = Vector(doubleArrayOf(*values))

fun vector(vararg values: Number) = Vector(values.map(Number::toDouble).toDoubleArray())

fun matrix(vararg rows: DoubleArray) = Matrix(arrayOf(*rows))

fun row(vararg values: Double) = doubleArrayOf(*values)

fun row(vararg values: Number) = values.map(Number::toDouble).toDoubleArray()

fun roundToInt(float: Double) = round(float).toInt()

fun roundToLong(float: Double) = round(float).toLong()
