package hr.fer.zemris.renderer.objectRenderers

import hr.fer.zemris.display.primitives.Primitive
import hr.fer.zemris.display.primitives.Triangle1cShadingPrimitive
import hr.fer.zemris.geometry.model.Triangle
import hr.fer.zemris.graphicsAlgorithms.util.removeHomogeneousCoordinate
import hr.fer.zemris.math.extensions.times
import hr.fer.zemris.math.matrix.Matrix
import hr.fer.zemris.math.transformations.identityMatrix
import hr.fer.zemris.math.util.vector
import hr.fer.zemris.math.vector.Vector
import hr.fer.zemris.renderer.GouraudShadingFillRenderObject
import hr.fer.zemris.renderer.camera.Camera
import hr.fer.zemris.renderer.lightning.Intensity
import hr.fer.zemris.renderer.lightning.Light
import hr.fer.zemris.renderer.projection.Projection
import hr.fer.zemris.renderer.viewport.ViewPort
import kotlin.math.max
import kotlin.math.pow

class GouraudShadingFillObjectRenderer(
    renderObject: GouraudShadingFillRenderObject,
    camera: Camera,
    projection: Projection,
    viewPort: ViewPort,
    sceneModelMatrix: Matrix = identityMatrix(),
    lights: List<Light>
) : GouraudShadingObjectRenderer<GouraudShadingFillRenderObject>(renderObject, camera, projection, viewPort, sceneModelMatrix, lights) {

    override fun toPrimitive(index: Int): Primitive {
        val p1 = points[vertexIndices[index]]
        val p2 = points[vertexIndices[index + 1]]
        val p3 = points[vertexIndices[index + 2]]


        return Triangle1cShadingPrimitive(
            Triangle(p1, p2, p3),
            calculateIntensityForVertex(
                modelViewTransformedVertices[vertexIndices[index]],
                lightNormals.getValue(vertexIndices[index])
            ),
            calculateIntensityForVertex(
                modelViewTransformedVertices[vertexIndices[index + 1]],
                lightNormals.getValue(vertexIndices[index + 1])
            ),
            calculateIntensityForVertex(
                modelViewTransformedVertices[vertexIndices[index + 2]],
                lightNormals.getValue(vertexIndices[index + 2])
            ),
            renderObject.color
        )
    }
}