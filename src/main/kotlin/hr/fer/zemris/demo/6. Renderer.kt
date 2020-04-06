package hr.fer.zemris.demo

import hr.fer.zemris.color.Color
import hr.fer.zemris.display.Display
import hr.fer.zemris.math.transformations.identityMatrix
import hr.fer.zemris.math.transformations.scaleMatrix
import hr.fer.zemris.math.transformations.translateMatrix
import hr.fer.zemris.math.util.vector
import hr.fer.zemris.renderer.FeRenderer
import hr.fer.zemris.renderer.RenderObject
import hr.fer.zemris.renderer.RenderType
import hr.fer.zemris.renderer.camera.CameraImpl
import hr.fer.zemris.renderer.projection.FovPerspectiveProjection
import hr.fer.zemris.renderer.viewport.ScreenSpaceTransform
import hr.fer.zemris.resources.loader.ObjLoader
import java.awt.event.KeyEvent

fun main() {
    val renderer = FeRenderer(
        Display(1600, 900, "Renderer"),
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

    val axisMesh = ObjLoader.load("src/main/resources/obj/simplecube.obj")

    val centerCubeTransform = scaleMatrix(2f) * translateMatrix(-1f, -1f ,-1f)

    val xAxisObject = RenderObject(
        axisMesh,
        centerCubeTransform * scaleMatrix(20f, 0.5f, 2f) * translateMatrix(0f, 0f, -200f),
        true,
        Color.GREEN,
        RenderType.DRAW
    )

    val yAxisObject = RenderObject(
        axisMesh,
        centerCubeTransform * scaleMatrix(0.5f, 20f, 2f) * translateMatrix(0f, 0f, -200f),
        true,
        Color.WHITE,
        RenderType.DRAW
    )

    val zAxisObject = RenderObject(
        axisMesh,
        centerCubeTransform * scaleMatrix(2f, 0.5f, 20f) * translateMatrix(0f, 0f, -200f),
        true,
        Color.YELLOW,
        RenderType.DRAW
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