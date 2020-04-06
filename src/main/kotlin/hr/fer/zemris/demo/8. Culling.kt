package hr.fer.zemris.demo

import hr.fer.zemris.color.Color
import hr.fer.zemris.display.Display
import hr.fer.zemris.math.transformations.*
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
        Display(1600, 900, "Camera"),
        CameraImpl(position = vector(0, 0, 20), target = vector(0, 0, -50f)),
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

    val cubeMesh = ObjLoader.load("src/main/resources/obj/simplecube.obj")
    val cubeNormalsMesh = ObjLoader.load("src/main/resources/obj/normalscube.obj")

    var deltaRot = 0f

    val cubeObject = RenderObject(cubeMesh, identityMatrix(), true, Color.RED, RenderType.DRAW)
    val cubeNormalsObject = RenderObject(cubeNormalsMesh, identityMatrix(), true, Color.GREEN, RenderType.DRAW)

    while (true) {
        renderer.clearDisplay()
        renderer.processKeyEvents()

        cubeObject.modelViewTransform = scaleMatrix(5f) * scaleYMatrix(2f) * scaleZMatrix(3f) * rotateYMatrix(deltaRot) * rotateXMatrix(deltaRot) * rotateZMatrix(deltaRot) * translateMatrix(0f, 0f, -50f)
        cubeNormalsObject.modelViewTransform = scaleMatrix(5f) * scaleYMatrix(2f) * scaleZMatrix(3f) * rotateYMatrix(deltaRot) * rotateXMatrix(deltaRot) * rotateZMatrix(deltaRot) * translateMatrix(10f, 0f, -50f)
        deltaRot += 0.01f

        renderer.render(cubeNormalsObject)
        renderer.render(cubeObject)
        renderer.swapBuffers()
    }

}