package hr.fer.zemris.demo

import hr.fer.zemris.color.Color
import hr.fer.zemris.demo.util.cubeVertices
import hr.fer.zemris.demo.util.diamondVertices
import hr.fer.zemris.demo.util.pyramidVertices
import hr.fer.zemris.display.Canvas
import hr.fer.zemris.display.Display
import hr.fer.zemris.geometry.model.Point
import hr.fer.zemris.geometry.model.Triangle
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
    val fovPerspectiveProjection = FovPerspectiveProjection(Math.toRadians(12.0).toFloat(), display.width.toFloat() / display.height.toFloat(), 0.1f, 500f)

    val cubeMesh = cubeVertices
    val pyramidMesh = pyramidVertices
    val diamondMesh = diamondVertices

    var deltaRotCube1 = 0.02f
    var deltaRotCube2 = 1f
    var deltaRotCube3 = 0f
    var deltaRotPyramid1 = 0.2f
    var deltaRotPyramid2 = 0f
    var deltaRotDiamond = 0f
    while (true) {
        canvas.clear(Color.BLACK)

        val cubeModelMatrix1 = scaleMatrix(9f) * rotateXMatrix(deltaRotCube1) * rotateYMatrix(deltaRotCube1) * rotateZMatrix(deltaRotCube1) * translateMatrix(15f, 0f, -150f)
        val cubeModelMatrix2 = scaleMatrix(5f) * rotateYMatrix(deltaRotCube2) * rotateZMatrix(deltaRotCube2) * translateMatrix(5f, 1f, -150f) * rotateZMatrix(deltaRotCube2/2)
        val cubeModelMatrix3 = scaleXMatrix(5f) * scaleYMatrix(2f) * scaleMatrix(5f) * rotateYMatrix(deltaRotCube3) * translateMatrix(-20f, 5f, -250f)
        val pyramidModelMatrix1 = scaleMatrix(5f) * rotateXMatrix(deltaRotPyramid1 + 0.5f) * rotateYMatrix(deltaRotPyramid1) * translateMatrix(0f, 0f, -150f)
        val pyramidModelMatrix2 = scaleMatrix(5f) * rotateYMatrix(deltaRotPyramid2) * translateMatrix(-10f, -10f, -150f) * rotateZMatrix(deltaRotPyramid2)
        val diamondModelMatrix = scaleYMatrix(2f) * scaleMatrix(4f) * rotateYMatrix(deltaRotDiamond) * rotateZMatrix(deltaRotDiamond) * translateMatrix(20f, -12f, -200f)


        deltaRotCube1 += 0.01f
        deltaRotCube2 += 0.05f
        deltaRotCube3 += 0.005f
        deltaRotPyramid1 += 0.02f
        deltaRotPyramid2 += 0.02f
        deltaRotDiamond += 0.05f

        renderMesh(cubeMesh, canvas, Color.MAGENTA, cubeModelMatrix1, fovPerspectiveProjection.projectionMatrix, viewPort.viewPortMatrix)
        renderMesh(cubeMesh, canvas, Color.WHITE, cubeModelMatrix2, fovPerspectiveProjection.projectionMatrix, viewPort.viewPortMatrix)
        renderMesh(cubeMesh, canvas, Color.GREEN, cubeModelMatrix3, fovPerspectiveProjection.projectionMatrix, viewPort.viewPortMatrix)
        renderMesh(pyramidMesh, canvas, Color.RED, pyramidModelMatrix1, fovPerspectiveProjection.projectionMatrix, viewPort.viewPortMatrix)
        renderMesh(pyramidMesh, canvas, Color.YELLOW, pyramidModelMatrix2, fovPerspectiveProjection.projectionMatrix, viewPort.viewPortMatrix)
        renderMesh(diamondMesh, canvas, Color.CYAN, diamondModelMatrix, fovPerspectiveProjection.projectionMatrix, viewPort.viewPortMatrix)
        display.swapBuffers()
    }
}

private fun renderMesh(vertices: List<Vector>, canvas : Canvas, color: Color, modelMatrix: Matrix, perspective: Matrix, viewPort: Matrix) {
    vertices.chunked(3) {
        canvas.drawTriangle(
            Triangle(
                vectorToPoint(it[0], modelMatrix, perspective, viewPort),
                vectorToPoint(it[1], modelMatrix, perspective, viewPort),
                vectorToPoint(it[2], modelMatrix, perspective, viewPort)
            ),
            color
        )
    }
}

private fun vectorToPoint(v1: Vector, modelMatrix: Matrix, perspective: Matrix, viewPort: Matrix): Point =
    ((v1.toMatrix(Vector.ToMatrix.ROW) * modelMatrix * perspective).let { it * (1f / it[0, 3]) } * viewPort).toVector().let { Point(ceil(it[0]).toInt(), ceil(it[1]).toInt()) }