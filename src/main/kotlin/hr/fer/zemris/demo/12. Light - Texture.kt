package hr.fer.zemris.demo

import hr.fer.zemris.display.Display
import hr.fer.zemris.math.transformations.*
import hr.fer.zemris.math.util.vector
import hr.fer.zemris.renderer.FeRenderer
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
        Display(2000, 1500, "Lightning - Texture"),
        CameraImpl(position = vector(-1.88, 78.20, 75.65), target = vector(0, 0, -10f)),
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

    var deltaRot = 0.0
    while (true) {
        renderer.clearDisplay()
        renderer.processKeyEvents()

        deltaRot+=0.01

        rootScene.renderObject("sun")?.modelViewTransform =
            scaleMatrix(0.5) * rotateYMatrix(deltaRot / 5.0)

        rootScene.child("earthScene")?.modelViewMatrix = translateMatrix(30.0, 0.0, 0.0) * rotateYMatrix(deltaRot)
        rootScene.renderObject("earth")?.modelViewTransform =
            scaleMatrix(0.2) * rotateYMatrix(deltaRot / 2.0)

        rootScene.child("moonScene")?.modelViewMatrix = translateMatrix(10.0, 0.0, 0.0) * rotateYMatrix(deltaRot * 2.0)
        rootScene.renderObject("moon")?.modelViewTransform = scaleMatrix(0.1) * rotateYMatrix(deltaRot / 5.0)

        renderer.render(rootScene)

        renderer.swapBuffers()
    }
}

private val cubeMesh = ObjLoader.load("src/main/resources/obj/cube.obj")
private val foxMesh = ObjLoader.load("src/main/resources/obj/fox.obj")
private val sphereMesh = ObjLoader.load("src/main/resources/obj/spherehighres.obj")

private val rootScene = rootScene {
    id = "rootScene"
    lights {
        light {
            id = "light"
            position = vector(0.0, 20.0, -10.0)
            ambientIntensity = Intensity(100.0)
            intensity = Intensity(250.0)
        }
    }
    renderObjects {
        gouraudShadingBitmapRenderObject {
            id = "grass"
            mesh = cubeMesh
            bitmapRes = "src/main/resources/texture/grass.jpg"
            modelViewMatrix =
                scaleYMatrix(0.1) * scaleXMatrix(20.0) * scaleZMatrix(20.0) * translateMatrix(0.0, -5.0, -10.0)
            lightsCoefs = LightCoefs(0.2, 0.7, 0.1, 1.0, 0.01)
        }
        gouraudShadingBitmapRenderObject {
            id = "fox"
            mesh = foxMesh
            bitmapRes = "src/main/resources/texture/fox_texture.png"
            modelViewMatrix = scaleMatrix(0.1) * translateMatrix(0.0, -5.0, -10.0)
            lightsCoefs = LightCoefs(0.2, 0.7, 0.1, 1.0, 0.01)
        }
    }
    scenes {
        scene {
            id = "sunScene"
            modelViewMatrix = translateMatrix(0.0, 20.0, -10.0)
            renderObjects {
                bitmapRenderObject {
                    id = "sun"
                    mesh = sphereMesh
                    bitmapRes = "src/main/resources/texture/sun.jpg"
                }
            }
            scenes {
                scene {
                    id = "earthScene"
                    renderObjects {
                        gouraudShadingBitmapRenderObject {
                            id = "earth"
                            mesh = sphereMesh
                            bitmapRes = "src/main/resources/texture/earth.jpg"
                            lightsCoefs = LightCoefs(0.2, 0.7, 0.1, 1.0, 0.01)
                        }
                    }
                    scenes {
                        scene {
                            id = "moonScene"
                            renderObjects {
                                gouraudShadingBitmapRenderObject {
                                    id = "moon"
                                    mesh = sphereMesh
                                    bitmapRes = "src/main/resources/texture/moon.jpg"
                                    lightsCoefs = LightCoefs(0.2, 0.7, 0.1, 1.0, 0.01)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
