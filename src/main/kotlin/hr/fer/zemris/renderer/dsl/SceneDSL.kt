package hr.fer.zemris.renderer.dsl

import hr.fer.zemris.math.transformations.identityMatrix
import hr.fer.zemris.renderer.RenderObject
import hr.fer.zemris.renderer.scene.Scene

fun scene(block: SceneBuilder.() -> Unit) : Scene = SceneBuilder().apply(block).build()

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

class Scenes: ArrayList<Scene>() {

    fun scene(block: SceneBuilder.() -> Unit) {
        add(SceneBuilder().apply(block).build())
    }
}

class RenderObjects : ArrayList<RenderObject>() {

    fun meshRenderObject(block: RenderObjectBuilder.() -> Unit) = add(RenderObjectBuilder().apply(block).buildMeshRenderObject())
    fun fillRenderObject(block: RenderObjectBuilder.() -> Unit) = add(RenderObjectBuilder().apply(block).buildFillRenderObject())
    fun bitmapRenderObject(block: RenderObjectBuilder.() -> Unit) = add(RenderObjectBuilder().apply(block).buildBitmapRenderObject())
}