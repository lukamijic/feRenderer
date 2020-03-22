package hr.fer.zemris.renderer.viewport

import hr.fer.zemris.math.matrix.Matrix
import hr.fer.zemris.math.util.matrix
import hr.fer.zemris.math.util.row

class ScreenSpaceTransform(
    width: Float,
    height: Float
) : ViewPort {

    private val viewPortMatrix: Matrix

    init {
        val halfWidth = width / 2f
        val halfHeight = height / 2f

        viewPortMatrix =
            matrix(
                row(halfWidth, 0, 0 , 0),
                row(0, -halfHeight, 0, 0),
                row(0, 0, 1, 0),
                row(halfWidth, halfWidth, 0, 1)
            )
    }

    override fun viewPortMatrix(): Matrix = viewPortMatrix
}