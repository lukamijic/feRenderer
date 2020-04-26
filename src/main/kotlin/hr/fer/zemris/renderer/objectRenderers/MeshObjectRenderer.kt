package hr.fer.zemris.renderer.objectRenderers

import hr.fer.zemris.display.primitives.Primitive
import hr.fer.zemris.display.primitives.UnfilledTrianglePrimitive
import hr.fer.zemris.geometry.model.Point2i
import hr.fer.zemris.geometry.model.Triangle2i
import hr.fer.zemris.math.matrix.Matrix
import hr.fer.zemris.math.transformations.identityMatrix
import hr.fer.zemris.renderer.MeshRenderObject
import hr.fer.zemris.renderer.camera.Camera
import hr.fer.zemris.renderer.projection.Projection
import hr.fer.zemris.renderer.viewport.ViewPort

class MeshObjectRenderer(
    renderObject: MeshRenderObject,
    camera: Camera,
    projection: Projection,
    viewPort: ViewPort,
    sceneModelMatrix: Matrix = identityMatrix()
) : ObjectRenderer<MeshRenderObject>(renderObject, camera, projection, viewPort, sceneModelMatrix) {

    override fun toPrimitive(index: Int): Primitive {
        val p1 = points[vertexIndices[index]].let { Point2i(it.x, it.y) }
        val p2 = points[vertexIndices[index + 1]].let { Point2i(it.x, it.y) }
        val p3 = points[vertexIndices[index + 2]].let { Point2i(it.x, it.y) }
        return UnfilledTrianglePrimitive(
            Triangle2i(p1, p2, p3), renderObject.color
        )
    }
}