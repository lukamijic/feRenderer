package hr.fer.zemris.demo

import hr.fer.zemris.color.Color
import hr.fer.zemris.color.RGB
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
        Display(1600, 900, "Z buffer"),
        CameraImpl(position = vector(0, 0, 0), target = vector(0, 0, -10f)),
        FovPerspectiveProjection(Math.toRadians(45.0), 16.0f / 9.0, 0.1, 100.0),
        ScreenSpaceTransform(1600, 900)
    ).apply {
        keyEventProcessor = { keyEvent ->
            when (keyEvent.keyCode) {
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
                    camera.cameraPosition -= camera.up
                }
            }
        }
    }

    val sphereMesh = ObjLoader.load("src/main/resources/obj/sphere.obj")
    val cubeMesh = ObjLoader.load("src/main/resources/obj/simplecube.obj")
    val teddyMesh = ObjLoader.load("src/main/resources/obj/teddy.obj")
    val foxMesh = ObjLoader.load("src/main/resources/obj/fox.obj")

    val sphereObject = RenderObject(
        sphereMesh,
        scaleMatrix(0.75) * scaleXMatrix(3.0) * translateMatrix(1.0, 0.0, -12.0),
        true,
        Color.RED,
        RenderType.FILL
    )

    val cubeObject = RenderObject(
        cubeMesh,
        scaleMatrix(1.5) * translateMatrix(-1.0, 0.0, -8.0),
        true,
        Color.GREEN,
        RenderType.FILL
    )

    val teddyObject = RenderObject(
        teddyMesh,
        identityMatrix(),
        true,
        Color.YELLOW,
        RenderType.FILL
    )

    val foxObject = RenderObject(
        foxMesh,
        identityMatrix(),
        true,
        RGB(255.toByte(), 127.toByte(), 0.toByte()).toColor(),
        RenderType.FILL
    )



    var deltaZ = 0.05
    var counter = 0

    var rotate = 0.0
    val deltaRot = 0.01
    while (true) {
        renderer.clearDisplay()
        renderer.processKeyEvents()

        counter++

        sphereObject.modelViewTransform = sphereObject.modelViewTransform * translateMatrix(0.0, 0.0, deltaZ)
        cubeObject.modelViewTransform = cubeObject.modelViewTransform * translateMatrix(0.0, 0.0, -deltaZ)
        teddyObject.modelViewTransform = rotateYMatrix(rotate) * scaleMatrix(0.05) * translateMatrix(2.0, 0.0, -10.0)
        foxObject.modelViewTransform = rotateXMatrix(rotate) * scaleMatrix(0.05) * translateMatrix(-1.0, -1.0, -12.0)

        rotate += deltaRot
        if (counter > 80) {
            counter = 0
            deltaZ *= -1
        }

        renderer.render(cubeObject)
        renderer.render(sphereObject)
        renderer.render(teddyObject)
        renderer.render(foxObject)

        renderer.swapBuffers()
    }

}