package hr.fer.zemris.demo

import hr.fer.zemris.color.Color
import hr.fer.zemris.display.Display
import hr.fer.zemris.math.transformations.scaleMatrix
import hr.fer.zemris.math.transformations.translateMatrix
import hr.fer.zemris.math.util.vector
import hr.fer.zemris.renderer.FeRenderer
import hr.fer.zemris.renderer.GouraudShadingFillRenderObject
import hr.fer.zemris.renderer.camera.CameraImpl
import hr.fer.zemris.renderer.dsl.rootScene
import hr.fer.zemris.renderer.lightning.Intensity
import hr.fer.zemris.renderer.lightning.LightCoefs
import hr.fer.zemris.renderer.projection.FovPerspectiveProjection
import hr.fer.zemris.renderer.viewport.ScreenSpaceTransform
import hr.fer.zemris.resources.loader.ObjLoader
import java.awt.event.KeyEvent

fun main() {
    val renderer = FeRenderer(
        Display(2000, 1500, "Lighting - Teddy"),
        CameraImpl(position = vector(0, 0, 0), target = vector(0, 0, -10f)),
        FovPerspectiveProjection(Math.toRadians(45.0), 2000.0 / 1500.0, 0.1, 500.0),
        ScreenSpaceTransform(2000, 1500)
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

    while (true) {
        renderer.clearDisplay()
        renderer.processKeyEvents()

        renderer.render(rootScene)

        renderer.swapBuffers()
    }
}

private val rootScene = rootScene {
    id = "rootScene"
    lights {
        light {
            id = "light"
            position = vector(0, 0, 0)
            ambientIntensity = Intensity(100.0)
            intensity = Intensity(200.0)
        }
    }
    renderObjects {
        gouraudShadingFillRenderObject {
            id = "teddy"
            meshRes = "src/main/resources/obj/teddy.obj"
            color = Color.RED
            lightsCoefs = LightCoefs(0.15, 0.7, 0.5, 3.0, 0.1)
            modelViewMatrix = scaleMatrix(0.2) * translateMatrix(0.0, 0.0, -10.0)
        }
    }
}