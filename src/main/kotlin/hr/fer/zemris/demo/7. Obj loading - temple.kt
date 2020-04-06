package hr.fer.zemris.demo

import hr.fer.zemris.color.Color
import hr.fer.zemris.display.Display
import hr.fer.zemris.math.transformations.identityMatrix
import hr.fer.zemris.math.transformations.rotateYMatrix
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
        Display(1600, 900, "Object Loading Temple"),
        CameraImpl(position = vector(0, 0, 20), target = vector(0, 0, -20f)),
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

    val temple = RenderObject(
        ObjLoader.load("src/main/resources/obj/temple.obj"),
        identityMatrix(),
        true,
        Color.WHITE,
        RenderType.DRAW
    )

    var deltaRot = 0f

    var fpsCounter = 0
    var time = System.currentTimeMillis()
    while (true) {
        renderer.clearDisplay()
        renderer.processKeyEvents()

        temple.modelViewTransform = scaleMatrix(20f) * rotateYMatrix(deltaRot) * translateMatrix(0f, 0f, -20f)
        deltaRot += 0.05f

        renderer.render(temple)

        renderer.swapBuffers()

        fpsCounter++
        if (System.currentTimeMillis() - time > 1000) {
            time = System.currentTimeMillis()
            println(fpsCounter)
            fpsCounter = 0
        }
    }

}