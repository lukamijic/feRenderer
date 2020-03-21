package hr.fer.zemris.math.transformations

import hr.fer.zemris.math.matrix.Matrix
import hr.fer.zemris.math.util.matrix
import hr.fer.zemris.math.util.row
import kotlin.math.cos
import kotlin.math.sin

fun translateMatrix(deltaX: Float, deltaY: Float, deltaZ: Float) =
    matrix(
        row(1, 0, 0, 0),
        row(0, 1, 0, 0),
        row(0, 0, 0, 1),
        row(deltaX, deltaY, deltaZ, 1)
    )

fun rotateXMatrix(angle: Float): Matrix {
    val sin = sin(angle)
    val cos = cos(angle)

    return matrix(
        row(1, 0, 0, 0),
        row(0, cos, sin, 0),
        row(0, -sin, cos, 0),
        row(0, 0, 0, 1)
    )
}

fun rotateYMatrix(angle: Float): Matrix {
    val sin = sin(angle)
    val cos = cos(angle)

    return matrix(
        row(cos, 0, -sin, 0),
        row(0, 1, 0, 0),
        row(sin, 0, cos, 0),
        row(0, 0, 0, 1)
    )
}

fun rotateZMatrix(angle: Float): Matrix {
    val sin = sin(angle)
    val cos = cos(angle)

    return matrix(
        row(cos, sin, 0, 0),
        row(-sin, cos, 0, 0),
        row(0, 0, 1, 0),
        row(0, 0, 0, 1)
    )
}

fun scaleMatrix(scale: Float): Matrix =
    matrix(
        row(1, 0, 0, 0),
        row(0, 1, 0, 0),
        row(0, 0, 1, 0),
        row(0, 0, 0, scale)
    )

fun scaleMatrix(scaleX: Float, scaleY: Float, scaleZ: Float): Matrix =
    matrix(
        row(scaleX, 0, 0, 0),
        row(0, scaleY, 0, 0),
        row(0, 0, scaleZ, 0),
        row(0, 0, 0, 1)
    )

fun scaleXMatrix(scale: Float): Matrix = scaleMatrix(scale, 1f, 1f)

fun scaleYMatrix(scale: Float): Matrix = scaleMatrix(1f, scale, 1f)

fun scaleZMatrix(scale: Float): Matrix = scaleMatrix(1f, 1f, scale)
