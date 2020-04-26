package hr.fer.zemris.renderer.objectRenderers

import hr.fer.zemris.display.Canvas
import hr.fer.zemris.display.primitives.Primitive
import hr.fer.zemris.geometry.model.Point
import hr.fer.zemris.graphicsAlgorithms.NormalCalculator
import hr.fer.zemris.graphicsAlgorithms.culling.Culling
import hr.fer.zemris.graphicsAlgorithms.util.removeHomogeneousCoordinate
import hr.fer.zemris.math.matrix.Matrix
import hr.fer.zemris.math.transformations.identityMatrix
import hr.fer.zemris.math.vector.Vector
import hr.fer.zemris.renderer.RenderObject
import hr.fer.zemris.renderer.camera.Camera
import hr.fer.zemris.renderer.projection.Projection
import hr.fer.zemris.renderer.viewport.ViewPort
import hr.fer.zemris.resources.mesh.Mesh
import hr.fer.zemris.util.IndexedList
import kotlin.math.ceil

private const val POLYGON_VERTICES = 3

abstract class ObjectRenderer<T : RenderObject>(
    val renderObject: T,
    private val camera: Camera,
    private val projection: Projection,
    private val viewPort: ViewPort,
    private val sceneModelMatrix: Matrix = identityMatrix()
) {

    val modelViewTransformMatrix = renderObject.modelViewTransform * sceneModelMatrix

    val modelViewTransformedVertices: List<Vector>
    val vertexIndices: List<Int>

    val modelTransformedNormals: List<Vector>
    val normalIndices: List<Int>

    val textureProjections = mutableListOf<Double>()
    val points : List<Point>

    init {
        modelViewTransformedVertices =
            renderObject
                .mesh
                .vertices
                .values.map { vector -> vector.toMatrix(Vector.ToMatrix.ROW) * modelViewTransformMatrix }
                .map(Matrix::toVector)

        vertexIndices = renderObject.mesh.vertices.indices

        val indexedTransformedNormals =  transformedNormals(renderObject.mesh, modelViewTransformMatrix)
            ?: calculateNormalsFromVertices(modelViewTransformedVertices, vertexIndices)

        modelTransformedNormals = indexedTransformedNormals.values
        normalIndices = indexedTransformedNormals.indices

        val cameraAndProjectionMatrix = camera.cameraMatrix * projection.projectionMatrix
        points = modelViewTransformedVertices
            .map { it.toMatrix(Vector.ToMatrix.ROW) }
            .map { it * cameraAndProjectionMatrix }
            .map {
                textureProjections.add(it[0, 3])
                it * (1f / it[0, 3])
            }
            .map { it * viewPort.viewPortMatrix }
            .map { Point(ceil(it[0, 0]).toInt(), ceil(it[0, 1]).toInt(), it[0, 2]) }
    }

    fun render(canvas: Canvas) {
        for (i in vertexIndices.indices step 3) {
            val normal = modelTransformedNormals[normalIndices[i]]
            if (renderObject.enableCulling) {
                val v = modelViewTransformedVertices[vertexIndices[i]].removeHomogeneousCoordinate()

                if (!Culling.cull(normal, camera.cameraPosition - v)) {
                    continue
                }
            }

            toPrimitive(i).draw(canvas)
        }
    }

    abstract fun toPrimitive(index: Int) : Primitive

    private fun transformedNormals(mesh: Mesh, modelViewTransformMatrix: Matrix) = mesh.normals?.let {
        val normalTransform =
            Matrix(Array(3) { i -> DoubleArray(3) { j -> modelViewTransformMatrix[i, j] } }).inverse().transpose()
        IndexedList(transformNormals(it.values, normalTransform), it.indices)
    }

    private fun transformNormals(normals: List<Vector>, transformMatrix: Matrix): List<Vector> =
        normals.asSequence()
            .map(Vector::normalize)
            .map { vector -> vector.toMatrix(Vector.ToMatrix.ROW) * transformMatrix }
            .map(Matrix::toVector)
            .map(Vector::normalize)
            .toList()

    private fun calculateNormalsFromVertices(vertices: List<Vector>, vertexIndices: List<Int>): IndexedList<Vector> =
        IndexedList(
            vertexIndices.chunked(POLYGON_VERTICES) {
                val v1 = vertices[it[0]].removeHomogeneousCoordinate()
                val v2 = vertices[it[1]].removeHomogeneousCoordinate()
                val v3 = vertices[it[2]].removeHomogeneousCoordinate()

                NormalCalculator.calculate(v1, v2, v3)
            },
            vertexIndices.indices.map { i -> i / POLYGON_VERTICES }
        )

}