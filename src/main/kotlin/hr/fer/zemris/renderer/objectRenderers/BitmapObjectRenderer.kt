package hr.fer.zemris.renderer.objectRenderers

import hr.fer.zemris.display.primitives.BitmapTrianglePrimitive
import hr.fer.zemris.display.primitives.Primitive
import hr.fer.zemris.geometry.model.Point3d
import hr.fer.zemris.geometry.model.Triangle
import hr.fer.zemris.math.matrix.Matrix
import hr.fer.zemris.math.transformations.identityMatrix
import hr.fer.zemris.renderer.BitmapRenderObject
import hr.fer.zemris.renderer.camera.Camera
import hr.fer.zemris.renderer.projection.Projection
import hr.fer.zemris.renderer.viewport.ViewPort

class BitmapObjectRenderer (
    renderObject: BitmapRenderObject,
    camera: Camera,
    projection: Projection,
    viewPort: ViewPort,
    sceneModelMatrix: Matrix = identityMatrix()
) : ObjectRenderer<BitmapRenderObject>(renderObject, camera, projection, viewPort, sceneModelMatrix) {

    private val texels = renderObject.mesh.texels!!.values
    private val texelsIndices = renderObject.mesh.texels!!.indices

    override fun toPrimitive(index: Int): Primitive {
        val p1 = points[vertexIndices[index]]
        val p2 = points[vertexIndices[index + 1]]
        val p3 = points[vertexIndices[index + 2]]

        val t1 = texels[texelsIndices[index]] * (1.0 / textureProjections[vertexIndices[index]])
        val t2 = texels[texelsIndices[index + 1]] * (1.0 / textureProjections[vertexIndices[index + 1]])
        val t3 = texels[texelsIndices[index + 2]] * (1.0 / textureProjections[vertexIndices[index + 2]])

        return BitmapTrianglePrimitive(
            Triangle(p1, p2, p3),
            Point3d(t1[0], t1[1], (1.0 / textureProjections[vertexIndices[index]])),
            Point3d(t2[0], t2[1], (1.0 / textureProjections[vertexIndices[index + 1]])),
            Point3d(t3[0], t3[1], (1.0 / textureProjections[vertexIndices[index + 2]])),
            renderObject.bitmap
        )
    }
}