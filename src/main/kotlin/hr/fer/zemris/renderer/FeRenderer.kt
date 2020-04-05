package hr.fer.zemris.renderer

import hr.fer.zemris.color.Color
import hr.fer.zemris.display.Display
import hr.fer.zemris.geometry.model.Point
import hr.fer.zemris.geometry.model.Triangle
import hr.fer.zemris.graphicsAlgorithms.NormalCalculator
import hr.fer.zemris.graphicsAlgorithms.culling.Culling
import hr.fer.zemris.graphicsAlgorithms.util.removeHomogeneousCoordinate
import hr.fer.zemris.math.matrix.Matrix
import hr.fer.zemris.math.vector.Vector
import hr.fer.zemris.renderer.camera.Camera
import hr.fer.zemris.renderer.input.KeyEventStorage
import hr.fer.zemris.renderer.input.KeyListenerAdapter
import hr.fer.zemris.renderer.projection.Projection
import hr.fer.zemris.renderer.viewport.ViewPort
import hr.fer.zemris.resources.mesh.Mesh
import java.awt.event.KeyEvent
import kotlin.math.ceil

class FeRenderer(
    var display: Display,
    val camera: Camera,
    val projection: Projection,
    val viewPort: ViewPort
) {

    var clearColor = Color.BLACK

    var keyEventProcessor: ((KeyEvent) -> Unit)? = null

    private val keyEventStorage = KeyEventStorage()

    init {
        display.addKeyListener(
            object : KeyListenerAdapter() {
                override fun keyPressed(e: KeyEvent?) {
                    e?.run { keyEventStorage.add(this) }
                }
            }
        )
    }

    fun renderMesh(mesh: Mesh, modelTransformMatrix: Matrix, color: Color) {
        val transformed = mesh.vertices.values
            .map { vector -> vector.toMatrix(Vector.ToMatrix.ROW) * modelTransformMatrix }
        val points = transformed.asSequence()
            .map { it * camera.cameraMatrix * projection.projectionMatrix }
            .map { it * (1f / it[0, 3]) }
            .map { it * viewPort.viewPortMatrix }
            .map { Point(ceil(it[0, 0]).toInt(), ceil(it[0, 1]).toInt()) }
            .toList()

        mesh.vertices.indices.chunked(3) {
            val v1 = transformed[it[0]].toVector().removeHomogeneousCoordinate()
            val v2 = transformed[it[1]].toVector().removeHomogeneousCoordinate()
            val v3 = transformed[it[2]].toVector().removeHomogeneousCoordinate()
            val triangleCenter = (v1 + v2 + v3) * (1f / 3f)
            val normal = NormalCalculator.calculate(v1, v2, v3)

            if (Culling.cull(normal, camera.cameraPosition - triangleCenter)) {
                display.canvas.drawTriangle(Triangle(points[it[0]], points[it[1]], points[it[2]]), color)
            }
        }
    }

    fun processKeyEvents() {
        keyEventProcessor?.let {
            keyEventStorage.takeEvents().forEach(it)
        }
    }

    fun clearDisplay() = display.canvas.clear(clearColor)

    fun swapBuffers() = display.swapBuffers()
}