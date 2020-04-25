package hr.fer.zemris.demo

import hr.fer.zemris.color.Color
import hr.fer.zemris.display.Display
import hr.fer.zemris.math.transformations.scaleMatrix
import hr.fer.zemris.math.transformations.translateMatrix
import hr.fer.zemris.math.util.vector
import hr.fer.zemris.renderer.FeRenderer
import hr.fer.zemris.renderer.MeshRenderObject
import hr.fer.zemris.renderer.camera.CameraImpl
import hr.fer.zemris.renderer.projection.FovPerspectiveProjection
import hr.fer.zemris.renderer.viewport.ScreenSpaceTransform
import hr.fer.zemris.resources.loader.ObjLoader
import java.awt.event.KeyEvent

fun main() {
    val renderer = FeRenderer(
        Display(1600, 900, "Renderer"),
        CameraImpl(position = vector(0, 0, 20), target = vector(0, 0, -200)),
        FovPerspectiveProjection(Math.toRadians(45.0), 16.0/9.0, 0.1, 500.0),
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

    val axisMesh = ObjLoader.load("src/main/resources/obj/simplecube.obj")

    val centerCubeTransform = scaleMatrix(2.0) * translateMatrix(-1.0, -1.0 ,-1.0)

    val xAxisObject = MeshRenderObject(
        "xAxis",
        axisMesh,
        Color.GREEN,
        centerCubeTransform * scaleMatrix(20.0, 0.5, 2.0) * translateMatrix(0.0, 0.0, -200.0),
        true
    )

    val yAxisObject = MeshRenderObject(
        "yAxis",
        axisMesh,
        Color.WHITE,
        centerCubeTransform * scaleMatrix(0.5, 20.0, 2.0) * translateMatrix(0.0, 0.0, -200.0),
        true
    )

    val zAxisObject = MeshRenderObject(
        "zAxis",
        axisMesh,
        Color.YELLOW,
        centerCubeTransform * scaleMatrix(2.0, 0.5, 20.0) * translateMatrix(0.0, 0.0, -200.0),
        true
    )

    while (true) {
        renderer.clearDisplay()
        renderer.processKeyEvents()

        renderer.render(xAxisObject)
        renderer.render(yAxisObject)
        renderer.render(zAxisObject)

        renderer.swapBuffers()
    }

}