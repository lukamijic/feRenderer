package hr.fer.zemris.demo

import hr.fer.zemris.color.Color
import hr.fer.zemris.display.Display
import hr.fer.zemris.math.transformations.*
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
        Display(1600, 900, "Camera"),
        CameraImpl(position = vector(0, 0, 20), target = vector(0, 0, -50f)),
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

    val cubeMesh = ObjLoader.load("src/main/resources/obj/simplecube.obj")
    val cubeNormalsMesh = ObjLoader.load("src/main/resources/obj/normalscube.obj")

    var deltaRot = 0.0

    val cubeObject = MeshRenderObject("cube", cubeMesh,  Color.RED)
    val cubeNormalsObject = MeshRenderObject("cubeNormals", cubeNormalsMesh,  Color.GREEN)

    while (true) {
        renderer.clearDisplay()
        renderer.processKeyEvents()

        cubeObject.modelViewTransform = scaleMatrix(5.0) * scaleYMatrix(2.0) * scaleZMatrix(3.0) *
                rotateYMatrix(deltaRot) * rotateXMatrix(deltaRot) * rotateZMatrix(deltaRot) * translateMatrix(0.0, 0.0, -50.0)
        cubeNormalsObject.modelViewTransform = scaleMatrix(5.0) * scaleYMatrix(2.0) * scaleZMatrix(3.0) *
                rotateYMatrix(deltaRot) * rotateXMatrix(deltaRot) * rotateZMatrix(deltaRot) * translateMatrix(10.0, 0.0, -50.0)
        deltaRot += 0.01

        renderer.render(cubeNormalsObject)
        renderer.render(cubeObject)
        renderer.swapBuffers()
    }

}