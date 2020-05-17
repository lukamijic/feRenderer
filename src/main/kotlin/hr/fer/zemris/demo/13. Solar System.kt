package hr.fer.zemris.demo

import hr.fer.zemris.display.Display
import hr.fer.zemris.math.transformations.rotateYMatrix
import hr.fer.zemris.math.transformations.scaleMatrix
import hr.fer.zemris.math.transformations.translateMatrix
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

private const val WIDTH = 1600
private const val HEIGHT = 900

fun main() {
    val renderer = FeRenderer(
        Display(WIDTH, HEIGHT, "Solar System"),
        CameraImpl(position = vector(0, 100, 0), target = vector(0, 0, -350)),
        FovPerspectiveProjection(Math.toRadians(45.0), WIDTH.toDouble() / HEIGHT.toDouble(), 0.1, 2000.0),
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

    var deltaRot = 0.0
    while (true) {
        renderer.clearDisplay()
        renderer.processKeyEvents()

        deltaRot+=0.005

        solarSystem.renderObject("sun")?.apply { modelViewTransform = rotateYMatrix(deltaRot / 2.0) }
        solarSystem.renderObject("mercury")?.apply { modelViewTransform = scaleMatrix(0.1) * rotateYMatrix(deltaRot * 4) }
        solarSystem.renderObject("venus")?.apply { modelViewTransform = scaleMatrix(0.2) * rotateYMatrix(deltaRot * 3) }
        solarSystem.renderObject("earth")?.apply { modelViewTransform = scaleMatrix(0.2) * rotateYMatrix(deltaRot * 2) }
        solarSystem.renderObject("moon")?.apply { modelViewTransform = scaleMatrix(0.05) * rotateYMatrix(deltaRot * 2.5) }
        solarSystem.renderObject("mars")?.apply { modelViewTransform = scaleMatrix(0.175) * rotateYMatrix(deltaRot * 2.5) }
        solarSystem.renderObject("jupiter")?.apply { modelViewTransform = scaleMatrix(0.5) * rotateYMatrix(deltaRot * 2) }

        solarSystem.child("mercuryScene")?.apply { modelViewMatrix = translateMatrix(-35.0, 0.0, 0.0) * rotateYMatrix(deltaRot * 5)  }
        solarSystem.child("venusScene")?.apply { modelViewMatrix = translateMatrix(-60.0, 0.0, 0.0) * rotateYMatrix(deltaRot * 4.5)  }
        solarSystem.child("earthScene")?.apply { modelViewMatrix = translateMatrix(-90.0, 0.0, 0.0) * rotateYMatrix(deltaRot * 4)  }
        solarSystem.child("moonScene")?.apply { modelViewMatrix = translateMatrix(-10.0, 0.0, 0.0) * rotateYMatrix(deltaRot * 10)  }
        solarSystem.child("marsScene")?.apply { modelViewMatrix = translateMatrix(-120.0, 0.0, 0.0) * rotateYMatrix(deltaRot * 3.5)  }
        solarSystem.child("jupiterScene")?.apply { modelViewMatrix = translateMatrix(-150.0, 0.0, 0.0) * rotateYMatrix(deltaRot * 2.0)  }
        renderer.render(solarSystem)

        renderer.swapBuffers()
    }
}

private val sphereMesh = ObjLoader.load("src/main/resources/obj/spherehighres.obj")

private val solarSystem = rootScene {
    id = "rootScene"
    lights {
        light {
            id = "light"
            position = vector(0.0, 0.0, -350.0)
            ambientIntensity = Intensity(500.0)
            intensity = Intensity(1500.0)
        }
    }
    scenes {
        scene {
            id = "sunScene"
            modelViewMatrix = translateMatrix(0.0, 0.0, -350.0)
            renderObjects {
                bitmapRenderObject {
                    id = "sun"
                    mesh = sphereMesh
                    bitmapRes = "src/main/resources/texture/sun.jpg"
                }
                bitmapRenderObject {
                    id = "skybox"
                    meshRes = "src/main/resources/obj/cube.obj"
                    bitmapRes = "src/main/resources/texture/space.jpg"
                    enableCulling = false
                    modelViewMatrix = scaleMatrix(1000.0)
                }
            }
            scenes {
                scene {
                    id = "mercuryScene"
                    modelViewMatrix = translateMatrix(-35.0, 0.0, 0.0)
                    renderObjects {
                        gouraudShadingBitmapRenderObject {
                            id = "mercury"
                            mesh = sphereMesh
                            bitmapRes = "src/main/resources/texture/mercury.jpg"
                            modelViewMatrix = scaleMatrix(0.1)
                            lightsCoefs = LightCoefs(0.2, 0.7, 0.3, 1.0, 0.2)
                        }
                    }
                }
                scene {
                    id = "venusScene"
                    modelViewMatrix = translateMatrix(-60.0, 0.0, 0.0)
                    renderObjects {
                        gouraudShadingBitmapRenderObject {
                            id = "venus"
                            mesh = sphereMesh
                            bitmapRes = "src/main/resources/texture/venus.jpg"
                            modelViewMatrix = scaleMatrix(0.2)
                            lightsCoefs = LightCoefs(0.2, 0.7, 0.3, 1.0, 0.1)
                        }
                    }
                }
                scene {
                    id = "earthScene"
                    modelViewMatrix = translateMatrix(-90.0, 0.0, 0.0)
                    renderObjects {
                        gouraudShadingBitmapRenderObject {
                            id = "earth"
                            mesh = sphereMesh
                            bitmapRes = "src/main/resources/texture/earth.jpg"
                            modelViewMatrix = scaleMatrix(0.2)
                            lightsCoefs = LightCoefs(0.2, 0.7, 0.3, 1.0, 0.05)
                        }
                    }
                    scenes {
                        scene {
                            id = "moonScene"
                            modelViewMatrix = translateMatrix(-10.0, 0.0, 0.0)
                            renderObjects {
                                gouraudShadingBitmapRenderObject {
                                    id = "moon"
                                    mesh = sphereMesh
                                    bitmapRes = "src/main/resources/texture/moon.jpg"
                                    modelViewMatrix = scaleMatrix(0.05)
                                    lightsCoefs = LightCoefs(0.2, 0.7, 0.3, 1.0, 0.05)
                                }
                            }
                        }
                    }
                }
                scene {
                    id = "marsScene"
                    modelViewMatrix = translateMatrix(-120.0, 0.0, 0.0)
                    renderObjects {
                        gouraudShadingBitmapRenderObject {
                            id = "mars"
                            mesh = sphereMesh
                            bitmapRes = "src/main/resources/texture/mars.jpg"
                            modelViewMatrix = scaleMatrix(0.175)
                            lightsCoefs = LightCoefs(0.2, 0.7, 0.3, 1.0, 0.025)
                        }
                    }
                }
                scene {
                    id = "jupiterScene"
                    modelViewMatrix = translateMatrix(-150.0, 0.0, 0.0)
                    renderObjects {
                        gouraudShadingBitmapRenderObject {
                            id = "jupiter"
                            mesh = sphereMesh
                            bitmapRes = "src/main/resources/texture/jupiter.jpg"
                            modelViewMatrix = scaleMatrix(0.5)
                            lightsCoefs = LightCoefs(0.2, 0.7, 0.3, 1.0, 0.0175)
                        }
                    }
                }
            }
        }
    }
}