package hr.fer.zemris.demo

import hr.fer.zemris.color.Color
import hr.fer.zemris.demo.util.cubeVertices
import hr.fer.zemris.display.Canvas
import hr.fer.zemris.display.Display
import hr.fer.zemris.display.primitives.UnfilledTrianglePrimitive
import hr.fer.zemris.geometry.model.Point2i
import hr.fer.zemris.geometry.model.Triangle
import hr.fer.zemris.geometry.model.Triangle2i
import hr.fer.zemris.math.matrix.Matrix
import hr.fer.zemris.math.transformations.scaleMatrix
import hr.fer.zemris.math.transformations.translateMatrix
import hr.fer.zemris.math.util.vector
import hr.fer.zemris.math.vector.Vector
import hr.fer.zemris.renderer.camera.CameraImpl
import hr.fer.zemris.renderer.projection.FovPerspectiveProjection
import hr.fer.zemris.renderer.viewport.ScreenSpaceTransform
import kotlin.math.ceil


fun main() {
    val display = Display(1600, 900, "Camera")
    val canvas = display.canvas

    val viewPort = ScreenSpaceTransform(display.width, display.height)
    val fovPerspectiveProjection =
        FovPerspectiveProjection(Math.toRadians(12.0), display.width.toDouble() / display.height.toDouble(), 0.1, 500.0)
    val camera = CameraImpl(position = vector(50, 50, 20), target = vector(0, 0, -200))

    val axisMesh = cubeVertices

    while (true) {
        canvas.clear(Color.BLACK)
        canvas.clearDepth()

        val centerCubeTransform = scaleMatrix(2.0) * translateMatrix(-1.0, -1.0, -1.0)

        val xAxisTransform = centerCubeTransform * scaleMatrix(20.0, 0.5, 2.0) * translateMatrix(0.0, 0.0, -200.0)
        val yAxisTransform = centerCubeTransform * scaleMatrix(0.5, 20.0, 2.0) * translateMatrix(0.0, 0.0, -200.0)
        val zAxisTransform = centerCubeTransform * scaleMatrix(2.0, 0.5, 20.0) * translateMatrix(0.0, 0.0, -200.0)

        renderMesh(
            axisMesh,
            canvas,
            Color.WHITE,
            xAxisTransform,
            camera.cameraMatrix,
            fovPerspectiveProjection.projectionMatrix,
            viewPort.viewPortMatrix
        )
        renderMesh(
            axisMesh,
            canvas,
            Color.RED,
            yAxisTransform,
            camera.cameraMatrix,
            fovPerspectiveProjection.projectionMatrix,
            viewPort.viewPortMatrix
        )
        renderMesh(
            axisMesh,
            canvas,
            Color.GREEN,
            zAxisTransform,
            camera.cameraMatrix,
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
    cameraMatrix: Matrix,
    perspective: Matrix,
    viewPort: Matrix
) {
    vertices.chunked(3) {
        UnfilledTrianglePrimitive(
            Triangle2i(
                vectorToPoint(it[0], modelMatrix, cameraMatrix, perspective, viewPort),
                vectorToPoint(it[1], modelMatrix, cameraMatrix, perspective, viewPort),
                vectorToPoint(it[2], modelMatrix, cameraMatrix, perspective, viewPort)
            ),
            color
        ).draw(canvas)
    }
}

private fun vectorToPoint(
    v1: Vector,
    modelMatrix: Matrix,
    cameraMatrix: Matrix,
    perspective: Matrix,
    viewPort: Matrix
): Point2i =
    ((v1.toMatrix(Vector.ToMatrix.ROW) * modelMatrix * cameraMatrix * perspective).let { it * (1.0 / it[0, 3]) } * viewPort).toVector().let {
        Point2i(
            ceil(it[0]).toInt(),
            ceil(it[1]).toInt()
        )
    }