package hr.fer.zemris.math.transformations

import hr.fer.zemris.math.matrix.Matrix
import hr.fer.zemris.math.util.matrix
import hr.fer.zemris.math.util.row
import kotlin.math.cos
import kotlin.math.sin

private val identityMatrixTransform =
    matrix(
        row(1, 0, 0, 0),
        row(0, 1, 0, 0),
        row(0, 0, 1, 0),
        row(0, 0, 0, 1)
    )

fun translateMatrix(deltaX: Double, deltaY: Double, deltaZ: Double) =
    matrix(
        row(1, 0, 0, 0),
        row(0, 1, 0, 0),
        row(0, 0, 1, 0),
        row(deltaX, deltaY, deltaZ, 1)
    )

fun rotateXMatrix(angle: Double): Matrix {
    val sin = sin(angle)
    val cos = cos(angle)

    return matrix(
        row(1, 0, 0, 0),
        row(0, cos, sin, 0),
        row(0, -sin, cos, 0),
        row(0, 0, 0, 1)
    )
}

fun rotateYMatrix(angle: Double): Matrix {
    val sin = sin(angle)
    val cos = cos(angle)

    return matrix(
        row(cos, 0, -sin, 0),
        row(0, 1, 0, 0),
        row(sin, 0, cos, 0),
        row(0, 0, 0, 1)
    )
}

fun rotateZMatrix(angle: Double): Matrix {
    val sin = sin(angle)
    val cos = cos(angle)

    return matrix(
        row(cos, sin, 0, 0),
        row(-sin, cos, 0, 0),
        row(0, 0, 1, 0),
        row(0, 0, 0, 1)
    )
}

fun scaleMatrix(scale: Double): Matrix =
    matrix(
        row(1, 0, 0, 0),
        row(0, 1, 0, 0),
        row(0, 0, 1, 0),
        row(0, 0, 0, 1f / scale)
    )

fun scaleMatrix(scaleX: Double, scaleY: Double, scaleZ: Double): Matrix =
    matrix(
        row(scaleX, 0, 0, 0),
        row(0, scaleY, 0, 0),
        row(0, 0, scaleZ, 0),
        row(0, 0, 0, 1)
    )

fun scaleXMatrix(scale: Double): Matrix = scaleMatrix(scale, 1.0, 1.0)

fun scaleYMatrix(scale: Double): Matrix = scaleMatrix(1.0, scale, 1.0)

fun scaleZMatrix(scale: Double): Matrix = scaleMatrix(1.0, 1.0, scale)

fun identityMatrix(): Matrix = identityMatrixTransform
