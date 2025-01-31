package hr.fer.zemris.demo

import hr.fer.zemris.display.Display
import hr.fer.zemris.math.transformations.*
import hr.fer.zemris.math.util.vector
import hr.fer.zemris.renderer.BitmapRenderObject
import hr.fer.zemris.renderer.FeRenderer
import hr.fer.zemris.renderer.camera.CameraImpl
import hr.fer.zemris.renderer.projection.FovPerspectiveProjection
import hr.fer.zemris.renderer.viewport.ScreenSpaceTransform
import hr.fer.zemris.resources.loader.BitmapLoader
import hr.fer.zemris.resources.loader.ObjLoader
import java.awt.event.KeyEvent

private const val WIDTH = 2000
private const val HEIGHT = 1125
fun main() {
    val renderer = FeRenderer(
        Display(WIDTH, HEIGHT, "Texture"),
        CameraImpl(position = vector(-1.88, 78.20, 75.65), target = vector(0, 0, -10f)),
        FovPerspectiveProjection(Math.toRadians(45.0), WIDTH.toDouble() / HEIGHT.toDouble(), 0.1, 500.0),
        ScreenSpaceTransform(WIDTH, HEIGHT)
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

    val cubeMesh = ObjLoader.load("src/main/resources/obj/cube.obj")
    val foxMesh = ObjLoader.load("src/main/resources/obj/fox.obj")
    val sphereMesh = ObjLoader.load("src/main/resources/obj/spherehighres.obj")

    val grass = BitmapRenderObject(
        "grass",
        cubeMesh,
        BitmapLoader.load("src/main/resources/texture/grass.jpg"),
        scaleYMatrix(0.1) * scaleXMatrix(20.0) * scaleZMatrix(20.0) * translateMatrix(0.0, -5.0, -10.0)
    )

    val fox = BitmapRenderObject(
        "fox",
        foxMesh,
        BitmapLoader.load("src/main/resources/texture/fox_texture.png"),
        scaleMatrix(0.1) * translateMatrix(0.0, -5.0, -10.0)
    )

    val earth = BitmapRenderObject(
        "earth",
        sphereMesh,
        BitmapLoader.load("src/main/resources/texture/earth.jpg"),
        identityMatrix()
    )

    val moon = BitmapRenderObject(
        "moon",
        sphereMesh,
        BitmapLoader.load("src/main/resources/texture/moon.jpg"),
        identityMatrix()
    )

    val sun = BitmapRenderObject(
        "sun",
        sphereMesh,
        BitmapLoader.load("src/main/resources/texture/sun.jpg"),
        identityMatrix()
    )

    var deltaRot = 0.0
    while (true) {
        renderer.clearDisplay()
        renderer.processKeyEvents()

        sun.modelViewTransform =
            scaleMatrix(0.5) * rotateYMatrix(deltaRot / 5.0) * translateMatrix(0.0, 20.0, -10.0)

        earth.modelViewTransform =
            scaleMatrix(0.2) * rotateYMatrix(deltaRot / 2.0) * translateMatrix(30.0, 0.0, 0.0) *
                    rotateYMatrix(deltaRot) * translateMatrix(0.0, 20.0, -10.0)

        moon.modelViewTransform =
            scaleMatrix(0.1) * rotateYMatrix(deltaRot / 5.0) * translateMatrix(10.0, 0.0, 0.0) *
                    rotateYMatrix(deltaRot * 2.0) *  translateMatrix(30.0, 0.0, 0.0) * rotateYMatrix(deltaRot) * translateMatrix(0.0, 20.0, -10.0)

        deltaRot+=0.01

        renderer.render(grass)
        renderer.render(fox)
        renderer.render(sun)
        renderer.render(earth)
        renderer.render(moon)

        renderer.swapBuffers()
    }

}