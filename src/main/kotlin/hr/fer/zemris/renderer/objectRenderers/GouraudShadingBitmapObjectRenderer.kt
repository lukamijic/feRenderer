package hr.fer.zemris.renderer.objectRenderers

import hr.fer.zemris.display.primitives.BitmapShadingTrianglePrimitive
import hr.fer.zemris.display.primitives.Primitive
import hr.fer.zemris.display.primitives.Triangle1cShadingPrimitive
import hr.fer.zemris.geometry.model.Point3d
import hr.fer.zemris.geometry.model.Triangle
import hr.fer.zemris.math.matrix.Matrix
import hr.fer.zemris.math.transformations.identityMatrix
import hr.fer.zemris.renderer.GouraudShadingBitmapRenderObject
import hr.fer.zemris.renderer.camera.Camera
import hr.fer.zemris.renderer.lightning.Light
import hr.fer.zemris.renderer.projection.Projection
import hr.fer.zemris.renderer.viewport.ViewPort

class GouraudShadingBitmapObjectRenderer(
    renderObject: GouraudShadingBitmapRenderObject,
    camera: Camera,
    projection: Projection,
    viewPort: ViewPort,
    sceneModelMatrix: Matrix = identityMatrix(),
    lights: List<Light>
) : GouraudShadingObjectRenderer<GouraudShadingBitmapRenderObject>(renderObject, camera, projection, viewPort, sceneModelMatrix, lights) {

    private val texels = renderObject.mesh.texels!!.values
    private val texelsIndices = renderObject.mesh.texels!!.indices

    override fun toPrimitive(index: Int): Primitive {
        val p1 = points[vertexIndices[index]]
        val p2 = points[vertexIndices[index + 1]]
        val p3 = points[vertexIndices[index + 2]]

        val t1 = texels[texelsIndices[index]] * (1.0 / textureProjections[vertexIndices[index]])
        val t2 = texels[texelsIndices[index + 1]] * (1.0 / textureProjections[vertexIndices[index + 1]])
        val t3 = texels[texelsIndices[index + 2]] * (1.0 / textureProjections[vertexIndices[index + 2]])

        return BitmapShadingTrianglePrimitive(
            Triangle(p1, p2, p3),
            Point3d(t1[0], t1[1], (1.0 / textureProjections[vertexIndices[index]])),
            Point3d(t2[0], t2[1], (1.0 / textureProjections[vertexIndices[index + 1]])),
            Point3d(t3[0], t3[1], (1.0 / textureProjections[vertexIndices[index + 2]])),
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
            renderObject.bitmap
        )
    }
}