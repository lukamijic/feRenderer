package hr.fer.zemris.demo

import hr.fer.zemris.color.Color
import hr.fer.zemris.display.Display
import hr.fer.zemris.math.transformations.identityMatrix
import hr.fer.zemris.math.transformations.rotateYMatrix
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
        Display(1600, 900, "Object Loading Temple"),
        CameraImpl(position = vector(0, 0, 20), target = vector(0, 0, -20f)),
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

    val temple = MeshRenderObject(
        "temple",
        ObjLoader.load("src/main/resources/obj/temple.obj"),
        Color.WHITE,
        identityMatrix(),
        true
    )

    var deltaRot = 0.0

    var fpsCounter = 0
    var time = System.currentTimeMillis()
    while (true) {
        renderer.clearDisplay()
        renderer.processKeyEvents()

        temple.modelViewTransform = scaleMatrix(20.0) * rotateYMatrix(deltaRot) * translateMatrix(0.0, 0.0, -20.0)
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