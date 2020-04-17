package hr.fer.zemris.demo

import hr.fer.zemris.color.Color
import hr.fer.zemris.demo.util.cubeVertices
import hr.fer.zemris.demo.util.diamondVertices
import hr.fer.zemris.demo.util.pyramidVertices
import hr.fer.zemris.display.Canvas
import hr.fer.zemris.display.Display
import hr.fer.zemris.display.primitives.UnfilledTrianglePrimitive
import hr.fer.zemris.geometry.model.Point2i
import hr.fer.zemris.geometry.model.Triangle2i
import hr.fer.zemris.math.matrix.Matrix
import hr.fer.zemris.math.transformations.*
import hr.fer.zemris.math.vector.Vector
import hr.fer.zemris.renderer.projection.FovPerspectiveProjection
import hr.fer.zemris.renderer.viewport.ScreenSpaceTransform
import kotlin.math.ceil


fun main() {
    val display = Display(1600, 900, "Perspective")
    val canvas = display.canvas

    val viewPort = ScreenSpaceTransform(display.width, display.height)
    val fovPerspectiveProjection =
        FovPerspectiveProjection(Math.toRadians(12.0), display.width.toDouble() / display.height.toDouble(), 0.1, 500.0)

    val cubeMesh = cubeVertices
    val pyramidMesh = pyramidVertices
    val diamondMesh = diamondVertices

    var deltaRotCube1 = 0.02
    var deltaRotCube2 = 1.0
    var deltaRotCube3 = 0.0
    var deltaRotPyramid1 = 0.2
    var deltaRotPyramid2 = 0.0
    var deltaRotDiamond = 0.0
    while (true) {
        canvas.clear(Color.BLACK)
        canvas.clearDepth()

        val cubeModelMatrix1 =
            scaleMatrix(9.0) * rotateXMatrix(deltaRotCube1) * rotateYMatrix(deltaRotCube1) * rotateZMatrix(deltaRotCube1) * translateMatrix(
                15.0,
                0.0,
                -150.0
            )
        val cubeModelMatrix2 =
            scaleMatrix(5.0) * rotateYMatrix(deltaRotCube2) * rotateZMatrix(deltaRotCube2) * translateMatrix(
                5.0,
                1.0,
                -150.0
            ) * rotateZMatrix(deltaRotCube2 / 2)
        val cubeModelMatrix3 =
            scaleXMatrix(5.0) * scaleYMatrix(2.0) * scaleMatrix(5.0) * rotateYMatrix(deltaRotCube3) * translateMatrix(
                -20.0,
                5.0,
                -250.0
            )
        val pyramidModelMatrix1 =
            scaleMatrix(5.0) * rotateXMatrix(deltaRotPyramid1 + 0.5) * rotateYMatrix(deltaRotPyramid1) * translateMatrix(
                0.0,
                0.0,
                -150.0
            )
        val pyramidModelMatrix2 =
            scaleMatrix(5.0) * rotateYMatrix(deltaRotPyramid2) * translateMatrix(-10.0, -10.0, -150.0) * rotateZMatrix(
                deltaRotPyramid2
            )
        val diamondModelMatrix =
            scaleYMatrix(2.0) * scaleMatrix(4.0) * rotateYMatrix(deltaRotDiamond) * rotateZMatrix(deltaRotDiamond) * translateMatrix(
                20.0,
                -12.0,
                -200.0
            )


        deltaRotCube1 += 0.01
        deltaRotCube2 += 0.05
        deltaRotCube3 += 0.005
        deltaRotPyramid1 += 0.02
        deltaRotPyramid2 += 0.02
        deltaRotDiamond += 0.05

        renderMesh(
            cubeMesh,
            canvas,
            Color.MAGENTA,
            cubeModelMatrix1,
            fovPerspectiveProjection.projectionMatrix,
            viewPort.viewPortMatrix
        )
        renderMesh(
            cubeMesh,
            canvas,
            Color.WHITE,
            cubeModelMatrix2,
            fovPerspectiveProjection.projectionMatrix,
            viewPort.viewPortMatrix
        )
        renderMesh(
            cubeMesh,
            canvas,
            Color.GREEN,
            cubeModelMatrix3,
            fovPerspectiveProjection.projectionMatrix,
            viewPort.viewPortMatrix
        )
        renderMesh(
            pyramidMesh,
            canvas,
            Color.RED,
            pyramidModelMatrix1,
            fovPerspectiveProjection.projectionMatrix,
            viewPort.viewPortMatrix
        )
        renderMesh(
            pyramidMesh,
            canvas,
            Color.YELLOW,
            pyramidModelMatrix2,
            fovPerspectiveProjection.projectionMatrix,
            viewPort.viewPortMatrix
        )
        renderMesh(
            diamondMesh,
            canvas,
            Color.CYAN,
            diamondModelMatrix,
            fovPerspectiveProjection.projectionMatrix,
            viewPort.viewPortMatrix
        )
        display.swapBuffers()
    }
}

private fun renderMesh(
    vertices: List<Vector>,
    canvas: Canvas,
    color: Color,
    modelMatrix: Matrix,
    perspective: Matrix,
    viewPort: Matrix
) {
    vertices.chunked(3) {
        UnfilledTrianglePrimitive(
            Triangle2i(
                vectorToPoint(it[0], modelMatrix, perspective, viewPort),
                vectorToPoint(it[1], modelMatrix, perspective, viewPort),
                vectorToPoint(it[2], modelMatrix, perspective, viewPort)
            ), color
        ).draw(canvas)
    }
}

private fun vectorToPoint(v1: Vector, modelMatrix: Matrix, perspective: Matrix, viewPort: Matrix): Point2i =
    ((v1.toMatrix(Vector.ToMatrix.ROW) * modelMatrix * perspective).let { it * (1.0 / it[0, 3]) } * viewPort).toVector().let {
        Point2i(
            ceil(it[0]).toInt(),
            ceil(it[1]).toInt()
        )
    }