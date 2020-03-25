package hr.fer.zemris.demo

import hr.fer.zemris.color.Color
import hr.fer.zemris.display.Canvas
import hr.fer.zemris.display.Display
import hr.fer.zemris.geometry.model.Point
import hr.fer.zemris.geometry.model.Triangle
import hr.fer.zemris.math.matrix.Matrix
import hr.fer.zemris.math.transformations.*
import hr.fer.zemris.math.vector.Vector
import hr.fer.zemris.renderer.projection.FovPerspectiveProjection
import hr.fer.zemris.renderer.viewport.ScreenSpaceTransform
import hr.fer.zemris.resources.mesh.Mesh
import hr.fer.zemris.resources.mesh.meshes.CubeMesh
import hr.fer.zemris.resources.mesh.meshes.DiamondMesh
import hr.fer.zemris.resources.mesh.meshes.PyramidMesh
import kotlin.math.ceil


fun main() {
    val display = Display(1600, 900, "Perspective")
    val canvas = display.canvas

    val viewPort = ScreenSpaceTransform(display.width, display.height)
    val fovPerspectiveProjection = FovPerspectiveProjection(Math.toRadians(12.0).toFloat(), display.width.toFloat() / display.height.toFloat(), 0.1f, 500f)

    val cube = CubeMesh()
    val pyramidMesh = PyramidMesh()
    val diamondMesh = DiamondMesh()

    var deltaRotCube1 = 0.02f
    var deltaRotCube2 = 1f
    var deltaRotCube3 = 0f
    var deltaRotPyramid1 = 0.2f
    var deltaRotPyramid2 = 0f
    var deltaRotDiamond = 0f
    while (true) {
        canvas.clear(Color.BLACK)

        val cubeModelMatrix1 = scaleMatrix(10f) * rotateXMatrix(deltaRotCube1) * rotateYMatrix(deltaRotCube1) * rotateZMatrix(deltaRotCube1) * translateMatrix(0f, 0f, 150f)
        val cubeModelMatrix2 = scaleMatrix(5f) * rotateYMatrix(deltaRotCube2) * rotateZMatrix(deltaRotCube2) * translateMatrix(0f, 0f, 150f)
        val cubeModelMatrix3 = scaleXMatrix(5f) * scaleYMatrix(2f) * scaleMatrix(5f) * rotateYMatrix(deltaRotCube3) * translateMatrix(10f, 10f, 250f)
        val pyramidModelMatrix1 = scaleMatrix(5f) * rotateXMatrix(deltaRotPyramid1 + 0.5f) * rotateYMatrix(deltaRotPyramid1) * translateMatrix(0f, 0f, 150f)
        val pyramidModelMatrix2 = scaleMatrix(5f) * rotateYMatrix(deltaRotPyramid2) * translateMatrix(-10f, -10f, 150f)
        val diamondModelMatrix = scaleYMatrix(2f) * scaleMatrix(4f) * rotateYMatrix(deltaRotDiamond) * rotateZMatrix(deltaRotDiamond) * translateMatrix(10f, -10f, 200f)


        deltaRotCube1 += 0.01f
        deltaRotCube2 += 0.05f
        deltaRotCube3 += 0.005f
        deltaRotPyramid1 += 0.02f
        deltaRotPyramid2 += 0.02f
        deltaRotDiamond += 0.05f

        renderMesh(cube, canvas, Color.MAGENTA, cubeModelMatrix1, fovPerspectiveProjection.projectionMatrix, viewPort.viewPortMatrix)
        renderMesh(cube, canvas, Color.WHITE, cubeModelMatrix2, fovPerspectiveProjection.projectionMatrix, viewPort.viewPortMatrix)
        renderMesh(cube, canvas, Color.GREEN, cubeModelMatrix3, fovPerspectiveProjection.projectionMatrix, viewPort.viewPortMatrix)
        renderMesh(pyramidMesh, canvas, Color.RED, pyramidModelMatrix1, fovPerspectiveProjection.projectionMatrix, viewPort.viewPortMatrix)
        renderMesh(pyramidMesh, canvas, Color.YELLOW, pyramidModelMatrix2, fovPerspectiveProjection.projectionMatrix, viewPort.viewPortMatrix)
        renderMesh(diamondMesh, canvas, Color.CYAN, diamondModelMatrix, fovPerspectiveProjection.projectionMatrix, viewPort.viewPortMatrix)
        display.swapBuffers()
    }
}

fun renderMesh(mesh: Mesh, canvas : Canvas, color: Color, modelMatrix: Matrix, perspective: Matrix, viewPort: Matrix) {
    for(i in mesh.vertices.indices step 3) {
        canvas.drawTriangle(
            Triangle(
                vectorToPoint(mesh.vertices[i], modelMatrix, perspective, viewPort),
                vectorToPoint(mesh.vertices[i + 1], modelMatrix, perspective, viewPort),
                vectorToPoint(mesh.vertices[i + 2], modelMatrix, perspective, viewPort)
            ),
            color
        )
    }
}

fun vectorToPoint(v1: Vector, modelMatrix: Matrix,perspective: Matrix, viewPort: Matrix): Point =
    ((v1.toMatrix(Vector.ToMatrix.ROW) * modelMatrix * perspective).let { it * (1f / it[0, 3]) } * viewPort).toVector().let { Point(ceil(it[0]).toInt(), ceil(it[1]).toInt()) }