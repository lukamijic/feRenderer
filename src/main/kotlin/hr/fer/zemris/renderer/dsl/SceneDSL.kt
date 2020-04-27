package hr.fer.zemris.renderer.dsl

import hr.fer.zemris.math.transformations.identityMatrix
import hr.fer.zemris.renderer.RenderObject
import hr.fer.zemris.renderer.lightning.Light
import hr.fer.zemris.renderer.scene.RootScene
import hr.fer.zemris.renderer.scene.Scene

fun scene(block: SceneBuilder.() -> Unit) : Scene = SceneBuilder().apply(block).build()

fun rootScene(block: RootSceneBuilder.() -> Unit) : RootScene = RootSceneBuilder().apply(block).build()

class SceneBuilder {

    var id: String? = null
    var modelViewMatrix = identityMatrix()

    private val scenes = Scenes()
    private val builderRenderObjects = RenderObjects()

    fun scenes(block: Scenes.() -> Unit) {
        scenes.addAll(Scenes().apply(block))
    }

    fun renderObjects(block: RenderObjects.() -> Unit) {
        builderRenderObjects.addAll(RenderObjects().apply(block))
    }

    fun build() : Scene = Scene(id!!, modelViewMatrix).apply {
        scenes.forEach { addChild(it) }
        builderRenderObjects.forEach { addRenderObject(it) }
    }
}

class RootSceneBuilder {

    var id: String? = null
    private val scenes = Scenes()
    private val builderRenderObjects = RenderObjects()
    private val builderLights = Lights()

    fun scenes(block: Scenes.() -> Unit) {
        scenes.addAll(Scenes().apply(block))
    }

    fun renderObjects(block: RenderObjects.() -> Unit) {
        builderRenderObjects.addAll(RenderObjects().apply(block))
    }

    fun lights(block: Lights.() -> Unit) {
        builderLights.addAll(Lights().apply(block))
    }

    fun build(): RootScene = RootScene(id!!).apply {
        scenes.forEach { addChild(it) }
        builderRenderObjects.forEach { addRenderObject(it) }
        builderLights.forEach { addLight(it) }
    }
}

class Scenes: ArrayList<Scene>() {

    fun scene(block: SceneBuilder.() -> Unit) {
        add(SceneBuilder().apply(block).build())
    }
}

class Lights: ArrayList<Light>() {

    fun light(block: LightBuilder.() -> Unit) {
        add(LightBuilder().apply(block).build())
    }
}

class RenderObjects : ArrayList<RenderObject>() {

    fun meshRenderObject(block: MeshRenderObjectBuilder.() -> Unit) =
        add(MeshRenderObjectBuilder().apply(block).build())

    fun fillRenderObject(block: FillRenderObjectBuilder.() -> Unit) =
        add(FillRenderObjectBuilder().apply(block).build())

    fun bitmapRenderObject(block: BitmapRenderObjectBuilder.() -> Unit) =
        add(BitmapRenderObjectBuilder().apply(block).build())

    fun gouraudShadingFillRenderObject(block: GouraudShadingFillRenderObjectBuilder.() -> Unit) =
        add(GouraudShadingFillRenderObjectBuilder().apply(block).build())

    fun gouraudShadingBitmapRenderObject(block: GouraudShadingBitmapRenderObjectBuilder.() -> Unit) =
        add(GouraudShadingBitmapRenderObjectBuilder().apply(block).build())
}