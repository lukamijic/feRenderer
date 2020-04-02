package hr.fer.zemris.renderer

import hr.fer.zemris.color.Color
import hr.fer.zemris.display.Display
import hr.fer.zemris.geometry.model.Point
import hr.fer.zemris.geometry.model.Triangle
import hr.fer.zemris.math.matrix.Matrix
import hr.fer.zemris.math.vector.Vector
import hr.fer.zemris.renderer.camera.Camera
import hr.fer.zemris.renderer.exceptions.InvalidNumberOfVertices
import hr.fer.zemris.renderer.exceptions.MeshMustHaveTexelsInformation
import hr.fer.zemris.renderer.input.KeyEventStorage
import hr.fer.zemris.renderer.input.KeyListenerAdapter
import hr.fer.zemris.renderer.projection.Projection
import hr.fer.zemris.renderer.viewport.ViewPort
import hr.fer.zemris.resources.bitmap.Bitmap
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

    fun renderMesh(mesh: Mesh, modelTransformMatrix: Matrix, color: Color) =
        mesh.vertices
            .map { vector -> vector.toMatrix(Vector.ToMatrix.ROW) }
            .map { it * modelTransformMatrix * camera.cameraMatrix * projection.projectionMatrix }
            .map { it * (1f / it[0, 3]) }
            .map { it * viewPort.viewPortMatrix }
            .map { Point(ceil(it[0, 0]).toInt(), ceil(it[0, 1]).toInt()) }
            .chunked(3) {
                if (it.size != 3) throw InvalidNumberOfVertices("Number of vertices must be dividable by 3 to form a triangle, number of vertices was ${mesh.vertices.size}")

                Triangle(it[0], it[1], it[2])
            }
            .forEach { display.canvas.drawTriangle(it, color) }

    fun processKeyEvents() {
        keyEventProcessor?.let {
            keyEventStorage.takeEvents().forEach(it)
        }
    }

    fun clearDisplay() = display.canvas.clear(clearColor)

    fun swapBuffers() = display.swapBuffers()
}