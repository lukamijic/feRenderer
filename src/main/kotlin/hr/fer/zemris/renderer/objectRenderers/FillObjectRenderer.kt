package hr.fer.zemris.renderer.objectRenderers

import hr.fer.zemris.display.primitives.Primitive
import hr.fer.zemris.display.primitives.Triangle1cPrimitive
import hr.fer.zemris.geometry.model.Triangle
import hr.fer.zemris.math.matrix.Matrix
import hr.fer.zemris.math.transformations.identityMatrix
import hr.fer.zemris.math.vector.Vector
import hr.fer.zemris.renderer.FillRenderObject
import hr.fer.zemris.renderer.camera.Camera
import hr.fer.zemris.renderer.projection.Projection
import hr.fer.zemris.renderer.viewport.ViewPort

class FillObjectRenderer(
    renderObject: FillRenderObject,
    camera: Camera,
    projection: Projection,
    viewPort: ViewPort,
    sceneModelMatrix: Matrix = identityMatrix()
) : ClippableObjectRenderer<FillRenderObject>(renderObject, camera, projection, viewPort, sceneModelMatrix) {

    override fun toPrimitive(index: Int): Primitive {
        val p1 = points[vertexIndices[index]]
        val p2 = points[vertexIndices[index + 1]]
        val p3 = points[vertexIndices[index + 2]]
        return Triangle1cPrimitive(
            Triangle(p1, p2, p3), renderObject.color
        )
    }

    override fun toPrimitives(index: Int, clippedVertices: List<Vector>): List<Primitive> {
        if (clippedVertices.isEmpty()) return emptyList()
        val primitives = mutableListOf<Primitive>()
        val clippedPoints = perspectiveDivideAndViewPortTransform(clippedVertices)
        val initialPoint = clippedPoints[0]
        for (i in 1 until (clippedPoints.size - 1)) {
            primitives.add(Triangle1cPrimitive(Triangle(initialPoint, clippedPoints[i], clippedPoints[i + 1]), renderObject.color))
        }

        return primitives
    }
}