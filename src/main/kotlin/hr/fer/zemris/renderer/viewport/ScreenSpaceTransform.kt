package hr.fer.zemris.renderer.viewport

import hr.fer.zemris.math.matrix.Matrix
import hr.fer.zemris.math.util.matrix
import hr.fer.zemris.math.util.row

class ScreenSpaceTransform(
    width: Int,
    height: Int
) : ViewPort {

    override val viewPortMatrix: Matrix

    init {
        val halfWidth = width.toFloat() * 0.5f
        val halfHeight = height.toFloat() * 0.5f

        viewPortMatrix =
            matrix(
                row(halfWidth, 0, 0, 0),
                row(0, -halfHeight, 0, 0),
                row(0, 0, 1, 0),
                row(halfWidth, halfHeight, 0, 1)
            )
    }
}