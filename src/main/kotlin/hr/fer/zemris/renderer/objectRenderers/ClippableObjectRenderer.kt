package hr.fer.zemris.renderer.objectRenderers

import hr.fer.zemris.display.Canvas
import hr.fer.zemris.display.primitives.Primitive
import hr.fer.zemris.geometry.model.Point
import hr.fer.zemris.geometry.model.Triangle
import hr.fer.zemris.graphicsAlgorithms.BarycentricCoordinatesCalculator
import hr.fer.zemris.graphicsAlgorithms.clipping.polygon.SutherlandHodgmanClipping
import hr.fer.zemris.graphicsAlgorithms.culling.Culling
import hr.fer.zemris.graphicsAlgorithms.interpolate.Interpolate
import hr.fer.zemris.graphicsAlgorithms.util.removeHomogeneousCoordinate
import hr.fer.zemris.math.matrix.Matrix
import hr.fer.zemris.math.transformations.identityMatrix
import hr.fer.zemris.math.util.vector
import hr.fer.zemris.math.vector.Vector
import hr.fer.zemris.renderer.RenderObject
import hr.fer.zemris.renderer.camera.Camera
import hr.fer.zemris.renderer.projection.Projection
import hr.fer.zemris.renderer.viewport.ViewPort
import kotlin.math.abs

abstract class ClippableObjectRenderer<T : RenderObject>(
    renderObject: T,
    camera: Camera,
    projection: Projection,
    viewPort: ViewPort,
    sceneModelMatrix: Matrix = identityMatrix()
) : ObjectRenderer<T>(renderObject, camera, projection, viewPort, sceneModelMatrix) {

    override fun render(canvas: Canvas) {
        for (i in vertexIndices.indices step 3) {
            val normal = modelTransformedNormals[normalIndices[i]]
            if (renderObject.enableCulling) {
                val v = modelViewTransformedVertices[vertexIndices[i]].removeHomogeneousCoordinate()

                if (!Culling.cull(normal, camera.cameraPosition - v)) {
                    continue
                }
            }

            val v1Inside = clippingSpaceVertices[vertexIndices[i]].isInsideViewFrustum()
            val v2Inside = clippingSpaceVertices[vertexIndices[i + 1]].isInsideViewFrustum()
            val v3Inside = clippingSpaceVertices[vertexIndices[i + 2]].isInsideViewFrustum()

            if(v1Inside && v2Inside && v3Inside) {
                toPrimitive(i).draw(canvas)
            } else {
                val clippedVertices = SutherlandHodgmanClipping.clip(
                    listOf(
                        clippingSpaceVertices[vertexIndices[i]],
                        clippingSpaceVertices[vertexIndices[i + 1]],
                        clippingSpaceVertices[vertexIndices[i + 2]]
                    )
                )
                toPrimitives(i, clippedVertices).forEach { it.draw(canvas) }
            }
        }
    }

    abstract fun toPrimitives(index: Int, clippedVertices: List<Vector>) : List<Primitive>

    protected fun calculateTexel(sourceTriangle: Triangle, t1: Vector, t2: Vector, t3: Vector, newPoint: Point): Vector {
        val weight = BarycentricCoordinatesCalculator.calculateBarycentricCoordinate(sourceTriangle, newPoint.x, newPoint.y)
        return vector(
            Interpolate.interpolateDouble(t1[0], t2[0], t3[0], weight),
            Interpolate.interpolateDouble(t1[1], t2[1], t3[1], weight),
            Interpolate.interpolateDouble(t1[2], t2[2], t3[2], weight)
        )
    }

    protected fun calculateW(sourceTriangle: Triangle, w1: Double, w2: Double, w3: Double, newPoint: Point): Double {
        val weight = BarycentricCoordinatesCalculator.calculateBarycentricCoordinate(sourceTriangle, newPoint.x, newPoint.y)
        return Interpolate.interpolateDouble(w1, w2, w3, weight)
    }

    private fun Vector.isInsideViewFrustum() =
        abs(this[0]) <= abs(this[3]) &&
                abs(this[1]) <= abs(this[3]) &&
                (this[2] >= 0 && this[2] <= this[3])
}