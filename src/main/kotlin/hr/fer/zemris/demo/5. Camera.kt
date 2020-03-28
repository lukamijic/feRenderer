package hr.fer.zemris.demo

import hr.fer.zemris.color.Color
import hr.fer.zemris.display.Canvas
import hr.fer.zemris.display.Display
import hr.fer.zemris.geometry.model.Point
import hr.fer.zemris.geometry.model.Triangle
import hr.fer.zemris.math.matrix.Matrix
import hr.fer.zemris.math.transformations.scaleMatrix
import hr.fer.zemris.math.transformations.translateMatrix
import hr.fer.zemris.math.util.vector
import hr.fer.zemris.math.vector.Vector
import hr.fer.zemris.renderer.camera.CameraImpl
import hr.fer.zemris.renderer.projection.FovPerspectiveProjection
import hr.fer.zemris.renderer.viewport.ScreenSpaceTransform
import hr.fer.zemris.resources.mesh.Mesh
import hr.fer.zemris.resources.mesh.meshes.CubeMesh
import kotlin.math.ceil


fun main() {
    val display = Display(1600, 900, "Perspective")
    val canvas = display.canvas

    val viewPort = ScreenSpaceTransform(display.width, display.height)
    val fovPerspectiveProjection = FovPerspectiveProjection(Math.toRadians(12.0).toFloat(), display.width.toFloat() / display.height.toFloat(), 0.1f, 500f)
    val camera = CameraImpl(position = vector(50, 50, 20), target = vector(0, 0, -200))

    val axisMesh = CubeMesh()

    while (true) {
        canvas.clear(Color.BLACK)

        val centerCube = scaleMatrix(2f) * translateMatrix(-1f, -1f ,-1f)

        val xAxisTransform = centerCube * scaleMatrix(20f, 0.5f, 2f) * translateMatrix(0f, 0f, -200f)
        val yAxisTransform = centerCube * scaleMatrix(0.5f, 20f, 2f) * translateMatrix(0f, 0f, -200f)
        val zAxisTransform = centerCube * scaleMatrix(2f, 0.5f, 20f) * translateMatrix(0f, 0f, -200f)

        renderMesh(axisMesh, canvas, Color.WHITE, xAxisTransform, camera.cameraMatrix, fovPerspectiveProjection.projectionMatrix, viewPort.viewPortMatrix)
        renderMesh(axisMesh, canvas, Color.RED, yAxisTransform, camera.cameraMatrix, fovPerspectiveProjection.projectionMatrix, viewPort.viewPortMatrix)
        renderMesh(axisMesh, canvas, Color.GREEN, zAxisTransform, camera.cameraMatrix, fovPerspectiveProjection.projectionMatrix, viewPort.viewPortMatrix)

        display.swapBuffers()
    }
}

private fun renderMesh(mesh: Mesh, canvas : Canvas, color: Color, modelMatrix: Matrix, cameraMatrix: Matrix, perspective: Matrix, viewPort: Matrix) {
    for(i in mesh.vertices.indices step 3) {
        canvas.drawTriangle(
            Triangle(
                vectorToPoint(mesh.vertices[i], modelMatrix, cameraMatrix, perspective, viewPort),
                vectorToPoint(mesh.vertices[i + 1], modelMatrix, cameraMatrix, perspective, viewPort),
                vectorToPoint(mesh.vertices[i + 2], modelMatrix, cameraMatrix, perspective, viewPort)
            ),
            color
        )
    }
}

private fun vectorToPoint(v1: Vector, modelMatrix: Matrix, cameraMatrix: Matrix, perspective: Matrix, viewPort: Matrix): Point =
    ((v1.toMatrix(Vector.ToMatrix.ROW) * modelMatrix * cameraMatrix * perspective).let { it * (1f / it[0, 3]) } * viewPort).toVector().let { Point(ceil(it[0]).toInt(), ceil(it[1]).toInt()) }