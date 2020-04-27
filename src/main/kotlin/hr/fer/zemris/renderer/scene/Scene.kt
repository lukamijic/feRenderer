package hr.fer.zemris.renderer.scene

import hr.fer.zemris.math.matrix.Matrix
import hr.fer.zemris.math.transformations.identityMatrix
import hr.fer.zemris.renderer.RenderObject
import java.lang.IllegalArgumentException

open class Scene(
    val sceneId: String,
    modelViewMatrixInitial: Matrix = identityMatrix()
) {

    open var parent: Scene? = null
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

    open var modelViewMatrix = modelViewMatrixInitial

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

    fun addRenderObject(renderObject: RenderObject) {
        renderObjectsInternal.add(renderObject)
    }

    fun child(sceneId: String): Scene? {
        val child = childrenInternal.find { it.sceneId == sceneId }

        if (child != null) {
            return child
        } else {
            for (c in childrenInternal) {
                val findChild = c.child(sceneId)
                if (findChild != null) {
                    return findChild
                }
            }
        }

        return null
    }

    fun renderObject(renderObjectId: String): RenderObject? {
        val renderObject = renderObjectsInternal.find { it.id == renderObjectId }

        if (renderObject != null) {
            return renderObject
        } else {
            for (c in childrenInternal) {
                val ro = c.renderObject(renderObjectId)
                if (ro != null) {
                    return ro
                }
            }
        }

        return null
    }
}