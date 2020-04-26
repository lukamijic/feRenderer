package hr.fer.zemris.renderer

import hr.fer.zemris.color.Color
import hr.fer.zemris.display.Display
import hr.fer.zemris.math.matrix.Matrix
import hr.fer.zemris.math.transformations.identityMatrix
import hr.fer.zemris.renderer.camera.Camera
import hr.fer.zemris.renderer.input.KeyEventStorage
import hr.fer.zemris.renderer.input.KeyListenerAdapter
import hr.fer.zemris.renderer.objectRenderers.BitmapObjectRenderer
import hr.fer.zemris.renderer.objectRenderers.FillObjectRenderer
import hr.fer.zemris.renderer.objectRenderers.MeshObjectRenderer
import hr.fer.zemris.renderer.projection.Projection
import hr.fer.zemris.renderer.scene.Scene
import hr.fer.zemris.renderer.viewport.ViewPort
import java.awt.event.KeyEvent

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

    fun render(scene: Scene) {
        scene.renderObjects.forEach { render(it, scene.globalModelViewMatrix) }
        scene.children.forEach { render(it) }
    }

    fun render(renderObject: RenderObject, sceneModelMatrix: Matrix = identityMatrix()): Unit =
        when(renderObject) {
            is MeshRenderObject -> MeshObjectRenderer(renderObject, camera, projection, viewPort, sceneModelMatrix)
            is FillRenderObject -> FillObjectRenderer(renderObject, camera, projection, viewPort, sceneModelMatrix)
            is BitmapRenderObject -> BitmapObjectRenderer(renderObject, camera, projection, viewPort, sceneModelMatrix)
        }.render(display.canvas)

    fun processKeyEvents() {
        keyEventProcessor?.let {
            keyEventStorage.takeEvents().forEach(it)
        }
    }

    fun clearDisplay() {
        display.canvas.clearDepth()
        display.canvas.clear(clearColor)
    }

    fun swapBuffers() = display.swapBuffers()
}