package hr.fer.zemris.renderer.objectRenderers

import hr.fer.zemris.display.primitives.Primitive
import hr.fer.zemris.graphicsAlgorithms.util.removeHomogeneousCoordinate
import hr.fer.zemris.math.extensions.times
import hr.fer.zemris.math.matrix.Matrix
import hr.fer.zemris.math.transformations.identityMatrix
import hr.fer.zemris.math.util.vector
import hr.fer.zemris.math.vector.Vector
import hr.fer.zemris.renderer.GouardShadingRenderObject
import hr.fer.zemris.renderer.camera.Camera
import hr.fer.zemris.renderer.lightning.Intensity
import hr.fer.zemris.renderer.lightning.Light
import hr.fer.zemris.renderer.projection.Projection
import hr.fer.zemris.renderer.viewport.ViewPort
import kotlin.math.max
import kotlin.math.pow

private const val SMALLEST_DIST = 0.01

abstract class GouraudShadingObjectRenderer<T: GouardShadingRenderObject>(
    renderObject: T,
    camera: Camera,
    projection: Projection,
    viewPort: ViewPort,
    sceneModelMatrix: Matrix = identityMatrix(),
    val lights: List<Light>
) : ObjectRenderer<T>(renderObject, camera, projection, viewPort, sceneModelMatrix) {

    val lightNormals: Map<Int, Vector>

    init {
        val vertexIndicesToNormalsIndices = mutableMapOf<Int, MutableList<Int>>()
        for (i in vertexIndices.indices) {
            val vertexIndex = vertexIndices[i]
            if (vertexIndicesToNormalsIndices[vertexIndex] == null) {
                vertexIndicesToNormalsIndices[vertexIndex] = mutableListOf(normalIndices[i])
            } else {
                vertexIndicesToNormalsIndices[vertexIndex]!!.add(normalIndices[i])
            }
        }

        lightNormals = vertexIndicesToNormalsIndices.mapValues {
            (it.value.fold(vector(0, 0, 0)) { value, normalIndex -> value + modelTransformedNormals[normalIndex] } * (1.0 / it.value.size)).normalize()
        }
    }

    fun calculateIntensityForVertex(vertex: Vector, normal: Vector): Intensity =
        Intensity(0.0, 0.0, 0.0).apply {
            val vertexPosition = vertex.removeHomogeneousCoordinate()
            lights.forEach {
                val toSourceBeforeNormalization = (it.position - vertexPosition)
                val toSource = toSourceBeforeNormalization.normalize()
                val diffuseAngle = max((toSource * normal), 0.0)

                val toEye = (camera.cameraPosition - vertexPosition).normalize()
                val distance = toSourceBeforeNormalization.norm
                val reflected = toSource + (2.0 * (normal - toSource)).normalize()
                val mirrorAngleToN = (max((reflected * toEye), 0.0)).pow(renderObject.lightsCoefs.mirrorN)

                val intensity = calculateIntensity(it, diffuseAngle, mirrorAngleToN, distance)

                r += intensity.r
                g += intensity.g
                b += intensity.b
            }
        }

    private fun calculateIntensity(
        light: Light,
        diffuseAngle: Double,
        mirrorAngleToN: Double,
        distance: Double
    ): Intensity = renderObject.lightsCoefs.let {
        Intensity(
            light.ambientIntensity.r * it.ambientR + light.intensity.r * (it.diffuseR * diffuseAngle + it.mirror * mirrorAngleToN) / (distance * it.distanceFactor + SMALLEST_DIST),
            light.ambientIntensity.g * it.ambientG + light.intensity.g * (it.diffuseG * diffuseAngle + it.mirror * mirrorAngleToN) / (distance * it.distanceFactor + SMALLEST_DIST),
            light.ambientIntensity.b * it.ambientB + light.intensity.b * (it.diffuseB * diffuseAngle + it.mirror * mirrorAngleToN) / (distance * it.distanceFactor + SMALLEST_DIST)
        )
    }
}