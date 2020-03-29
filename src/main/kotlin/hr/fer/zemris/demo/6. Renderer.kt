package hr.fer.zemris.demo

import hr.fer.zemris.color.Color
import hr.fer.zemris.display.Display
import hr.fer.zemris.math.transformations.scaleMatrix
import hr.fer.zemris.math.transformations.translateMatrix
import hr.fer.zemris.math.util.vector
import hr.fer.zemris.renderer.FeRenderer
import hr.fer.zemris.renderer.camera.CameraImpl
import hr.fer.zemris.renderer.projection.FovPerspectiveProjection
import hr.fer.zemris.renderer.viewport.ScreenSpaceTransform
import hr.fer.zemris.resources.mesh.meshes.CubeMesh
import java.awt.event.KeyEvent

fun main() {
    val renderer = FeRenderer(
        Display(1600, 900, "Camera"),
        CameraImpl(position = vector(0, 0, 20), target = vector(0, 0, -200)),
        FovPerspectiveProjection(Math.toRadians(45.0).toFloat(), 16f/9f, 0.1f, 500f),
        ScreenSpaceTransform(1600, 900)
    ).apply {
        keyEventProcessor = { keyEvent ->
            when(keyEvent.keyCode) {
                KeyEvent.VK_W -> {
                    camera.cameraPosition -= camera.forward
                }
                KeyEvent.VK_S -> {
                    camera.cameraPosition += camera.forward
                }
                KeyEvent.VK_A -> {
                    camera.cameraPosition -= camera.right
                }
                KeyEvent.VK_D -> {
                    camera.cameraPosition += camera.right
                }
                KeyEvent.VK_SPACE -> {
                    camera.cameraPosition += camera.up
                }
                KeyEvent.VK_CONTROL -> {
                    camera.cameraPosition -=  camera.up
                }
            }
        }
    }

    val axisMesh = CubeMesh()

    while (true) {
        renderer.clearDisplay()
        renderer.processKeyEvents()

        val centerCubeTransform = scaleMatrix(2f) * translateMatrix(-1f, -1f ,-1f)

        val xAxisTransform = centerCubeTransform * scaleMatrix(20f, 0.5f, 2f) * translateMatrix(0f, 0f, -200f)
        val yAxisTransform = centerCubeTransform * scaleMatrix(0.5f, 20f, 2f) * translateMatrix(0f, 0f, -200f)
        val zAxisTransform = centerCubeTransform * scaleMatrix(2f, 0.5f, 20f) * translateMatrix(0f, 0f, -200f)

        renderer.renderMesh(axisMesh, xAxisTransform, Color.WHITE)
        renderer.renderMesh(axisMesh, yAxisTransform, Color.GREEN)
        renderer.renderMesh(axisMesh, zAxisTransform, Color.YELLOW)

        renderer.swapBuffers()
    }

}