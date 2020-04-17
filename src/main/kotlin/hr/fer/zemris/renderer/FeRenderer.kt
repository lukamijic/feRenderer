package hr.fer.zemris.renderer

import hr.fer.zemris.color.Color
import hr.fer.zemris.display.Display
import hr.fer.zemris.display.primitives.Primitive
import hr.fer.zemris.display.primitives.Triangle1cPrimitive
import hr.fer.zemris.display.primitives.UnfilledTrianglePrimitive
import hr.fer.zemris.geometry.model.Point
import hr.fer.zemris.geometry.model.Point2i
import hr.fer.zemris.geometry.model.Triangle
import hr.fer.zemris.geometry.model.Triangle2i
import hr.fer.zemris.graphicsAlgorithms.NormalCalculator
import hr.fer.zemris.graphicsAlgorithms.culling.Culling
import hr.fer.zemris.graphicsAlgorithms.util.removeHomogeneousCoordinate
import hr.fer.zemris.math.matrix.Matrix
import hr.fer.zemris.math.vector.Vector
import hr.fer.zemris.renderer.camera.Camera
import hr.fer.zemris.renderer.input.KeyEventStorage
import hr.fer.zemris.renderer.input.KeyListenerAdapter
import hr.fer.zemris.renderer.projection.Projection
import hr.fer.zemris.renderer.viewport.ViewPort
import hr.fer.zemris.util.IndexedList
import java.awt.event.KeyEvent
import kotlin.math.ceil

private const val POLYGON_VERTICES = 3

class FeRenderer(
    var display: Display,
    val camera: Camera,
    val projection: Projection,
    val viewPort: ViewPort
) {

    var clearColor = Color.BLACK

    var keyEventProcessor: ((KeyEvent) -> Unit)? = null

    private val keyEventStorage = KeyEventStorage()

    init {
        display.addKeyListener(
            object : KeyListenerAdapter() {
                override fun keyPressed(e: KeyEvent?) {
                    e?.run { keyEventStorage.add(this) }
                }
            }
        )
    }

    fun render(renderObject: RenderObject): Unit = renderObject.run {
        val modelTransformVertices =
            mesh.vertices.values.map { vector -> vector.toMatrix(Vector.ToMatrix.ROW) * modelViewTransform }
        val vertexIndices = mesh.vertices.indices

        val (modelTransformedNormals, normalIndices) = mesh.normals?.let {
            val normalTransform =
                Matrix(Array(3) { i -> DoubleArray(3) { j -> modelViewTransform[i, j] } }).inverse().transpose()
            IndexedList(transformNormals(it.values, normalTransform), it.indices)
        } ?: calculateNormals(modelTransformVertices, vertexIndices)

        val cameraAndProjectionMatrix = camera.cameraMatrix * projection.projectionMatrix
        val points = modelTransformVertices.asSequence()
            .map { it * cameraAndProjectionMatrix }
            .map { it * (1f / it[0, 3]) }
            .map { it * viewPort.viewPortMatrix }
            .map { Point(ceil(it[0, 0]).toInt(), ceil(it[0, 1]).toInt(), it[0, 2]) }
            .toList()

        for (i in vertexIndices.indices step 3) {
            val p1 = points[vertexIndices[i]]
            val p2 = points[vertexIndices[i + 1]]
            val p3 = points[vertexIndices[i + 2]]

            val normal = modelTransformedNormals[normalIndices[i]]
            if (enableCulling) {
                val v = modelTransformVertices[vertexIndices[i]].toVector().removeHomogeneousCoordinate()

                if (Culling.cull(normal, camera.cameraPosition - v)) {
                    toRenderingPrimitive(p1, p2, p3, color, renderType).draw(display.canvas)
                }
            } else {
                toRenderingPrimitive(p1, p2, p3, color, renderType).draw(display.canvas)
            }
        }
    }

    fun processKeyEvents() {
        keyEventProcessor?.let {
            keyEventStorage.takeEvents().forEach(it)
        }
    }

    fun clearDisplay() {
        display.canvas.clearDepth()
        display.canvas.clear(clearColor)
    }

    fun swapBuffers() = display.swapBuffers()

    private fun transformNormals(normals: List<Vector>, transformMatrix: Matrix): List<Vector> =
        normals.asSequence()
            .map(Vector::normalize)
            .map { vector -> vector.toMatrix(Vector.ToMatrix.ROW) * transformMatrix }
            .map(Matrix::toVector)
            .map(Vector::normalize)
            .toList()

    private fun calculateNormals(vertices: List<Matrix>, vertexIndices: List<Int>): IndexedList<Vector> =
        IndexedList(
            vertexIndices.chunked(POLYGON_VERTICES) {
                val v1 = vertices[it[0]].toVector().removeHomogeneousCoordinate()
                val v2 = vertices[it[1]].toVector().removeHomogeneousCoordinate()
                val v3 = vertices[it[2]].toVector().removeHomogeneousCoordinate()

                NormalCalculator.calculate(v1, v2, v3)
            },
            vertexIndices.indices.map { i -> i / POLYGON_VERTICES }
        )

    private fun toRenderingPrimitive(p1: Point, p2: Point, p3: Point, color: Color, renderType: RenderType): Primitive =
        when (renderType) {
            RenderType.DRAW -> {
                UnfilledTrianglePrimitive(
                    Triangle2i(
                        Point2i(p1.x, p1.y),
                        Point2i(p2.x, p2.y),
                        Point2i(p3.x, p3.y)
                    ), color
                )
            }
            RenderType.FILL ->
                Triangle1cPrimitive(Triangle(p1, p2, p3), color)
        }
}