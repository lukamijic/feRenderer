package hr.fer.zemris.renderer.scene

import hr.fer.zemris.math.matrix.Matrix
import hr.fer.zemris.math.transformations.identityMatrix
import hr.fer.zemris.renderer.lightning.Light
import java.lang.RuntimeException

class RootScene(
    sceneId: String
) : Scene(sceneId, identityMatrix()) {

    override var parent: Scene?
        get() = null
        set(value) { throw RuntimeException("Can't assign parent to root scene") }

    override var modelViewMatrix: Matrix
        get() = identityMatrix()
        set(value) { throw RuntimeException("Can't assign modelViewMatrix to rootscene")}

    val lights: List<Light>
        get() = lightsInternal

    private val lightsInternal = mutableListOf<Light>()

    fun addLight(light: Light) {
        lightsInternal.add(light)
    }

    fun light(id: String) : Light? = lightsInternal.firstOrNull { it.id == id }
}