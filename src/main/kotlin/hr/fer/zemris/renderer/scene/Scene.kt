package hr.fer.zemris.renderer.scene

import hr.fer.zemris.math.matrix.Matrix
import hr.fer.zemris.math.transformations.identityMatrix
import hr.fer.zemris.renderer.RenderObject
import java.lang.IllegalArgumentException

class Scene(
    val sceneId: String,
    var modelViewMatrix: Matrix = identityMatrix()
) {

    var parent: Scene? = null
        set(value) {
            if (value == null) {
                field = value
            } else {
                if (field != null) {
                    throw IllegalArgumentException("Scene already has a parent")
                } else {
                    field = value
                }
            }
        }

    val globalModelViewMatrix: Matrix
        get() {
            var matrix = modelViewMatrix
            var prevScene = parent
            while (prevScene != null) {
                matrix *= prevScene.modelViewMatrix
                prevScene = prevScene.parent
            }

            return matrix
        }

    val children: List<Scene>
        get() = childrenInternal

    val renderObjects: List<RenderObject>
        get() = renderObjectsInternal

    private val childrenInternal: MutableList<Scene> = mutableListOf()
    private val renderObjectsInternal: MutableList<RenderObject> = mutableListOf()

    fun addChild(scene: Scene) {
        scene.parent = this
        childrenInternal.add(scene)
    }

    fun addRenderObject(renderObject: RenderObject) = renderObjectsInternal.add(renderObject)

    fun findChild(sceneId: String): Scene? {
        val child = childrenInternal.find { it.sceneId == sceneId }

        if (child != null) {
            return child
        } else {
            for (c in childrenInternal) {
                val findChild = c.findChild(sceneId)
                if (findChild != null) {
                    return findChild
                }
            }
        }

        return null
    }

    fun findRenderObject(renderObjectId: String): RenderObject? {
        val renderObject = renderObjectsInternal.find { it.id == renderObjectId }

        if (renderObject != null) {
            return renderObject
        } else {
            for (c in childrenInternal) {
                val ro = c.findRenderObject(renderObjectId)
                if (ro != null) {
                    return ro
                }
            }
        }

        return null
    }
}