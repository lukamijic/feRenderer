package hr.fer.zemris.renderer.projection

import hr.fer.zemris.math.matrix.Matrix
import hr.fer.zemris.math.util.Radians
import hr.fer.zemris.math.util.matrix
import hr.fer.zemris.math.util.row
import kotlin.math.tan

class FovPerspectiveProjection(
    fov: Radians,
    aspectRatio: Double,
    zNear: Double,
    zFar: Double
) : Projection {

    override val projectionMatrix: Matrix

    init {
        val cotanHalfFov = 1 / tan(fov * 0.5)
        val zRange = zFar - zNear
        val oneOverAspectRatio = 1.0 / aspectRatio
        projectionMatrix = matrix(
            row(cotanHalfFov * oneOverAspectRatio, 0, 0, 0),
            row(0, cotanHalfFov, 0, 0),
            row(0, 0, -zFar / zRange, -1),
            row(0, 0, -(zFar * zNear) / zRange, 0)
        )
    }
}