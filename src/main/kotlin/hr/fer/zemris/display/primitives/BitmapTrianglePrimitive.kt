package hr.fer.zemris.display.primitives

import hr.fer.zemris.display.Canvas
import hr.fer.zemris.geometry.model.Point
import hr.fer.zemris.geometry.model.Point3d
import hr.fer.zemris.geometry.model.Triangle
import hr.fer.zemris.graphicsAlgorithms.BarycentricCoordinates
import hr.fer.zemris.graphicsAlgorithms.BarycentricCoordinatesCalculator
import hr.fer.zemris.math.util.Handedness
import hr.fer.zemris.resources.bitmap.Bitmap
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min

class BitmapTrianglePrimitive(
    triangle: Triangle,
    texel1: Point3d,
    texel2: Point3d,
    texel3: Point3d,
    private val bitmap: Bitmap
) : Primitive {

    private val handedness: Handedness
    private val topToBottom: TriangleEdgeYToX
    private val topToMiddle: TriangleEdgeYToX
    private val middleToBottom: TriangleEdgeYToX

    private var minY: PointTexel
    private var midY: PointTexel
    private var maxY: PointTexel

    init {
        minY = PointTexel(triangle.p1, texel1)
        midY = PointTexel(triangle.p2, texel2)
        maxY = PointTexel(triangle.p3, texel3)

        if (maxY.point.y < midY.point.y) {
            val temp = maxY
            maxY = midY
            midY = temp
        }

        if (midY.point.y < minY.point.y) {
            val temp = midY
            midY = minY
            minY = temp
        }

        if (maxY.point.y < midY.point.y) {
            val temp = maxY
            maxY = midY
            midY = temp
        }

        handedness = Handedness.calculateHandiness(minY.point, maxY.point, midY.point)
        topToBottom = TriangleEdgeYToX(minY, maxY)
        topToMiddle = TriangleEdgeYToX(minY, midY)
        middleToBottom = TriangleEdgeYToX(midY, maxY)
    }

    override fun draw(canvas: Canvas) {
        scanTriangleEdges(topToBottom, topToMiddle, handedness, canvas)
        scanTriangleEdges(topToBottom, middleToBottom, handedness, canvas)
    }

    private fun scanTriangleEdges(
        longEdge: TriangleEdgeYToX,
        shortEdge: TriangleEdgeYToX,
        handedness: Handedness,
        canvas: Canvas
    ) {
        val left = if (handedness == Handedness.LEFT) longEdge else shortEdge
        val right = if (handedness == Handedness.LEFT) shortEdge else longEdge

        for (y in shortEdge.yStart until shortEdge.yEnd) {
            scanLine(left[y], right[y], y, canvas)
        }
    }

    /**
     * This methods works correctly if startX is before endX
     */
    private fun scanLine(startX: Int, endX: Int, y: Int, canvas: Canvas) {
        val boundingBox = canvas.canvasBoundingBox
        if ((y in boundingBox.minY..boundingBox.maxY).not()) return

        val startLoopX = if (startX < boundingBox.minX) boundingBox.minX else startX
        val endLoopX = if (endX > boundingBox.maxX) boundingBox.maxX else endX

        (startLoopX..endLoopX).forEach { x ->
            val bc = BarycentricCoordinatesCalculator.calculateBarycentricCoordinate(
                Triangle(
                    minY.point,
                    midY.point,
                    maxY.point
                ), x, y
            )
            val w = interpolateDouble(minY.texel.w, midY.texel.w, maxY.texel.w, bc)
            val u = interpolateDouble(minY.texel.u, midY.texel.u, maxY.texel.u, bc) / w
            val v = 1.0 - interpolateDouble(minY.texel.v, midY.texel.v, maxY.texel.v, bc) / w
            val z = interpolateDouble(minY.point.z, midY.point.z, maxY.point.z, bc)
            val texture = bitmap[toBitmapRange(u), toBitmapRange(v)]

            //TODO: REVISIT WHEN IMPLEMENTED CLIPPING
            texture?.let { canvas.drawPixel(x, y, z, it) }
        }
    }

    private fun interpolateDouble(d1: Double, d2: Double, d3: Double, barycentricCoordinates: BarycentricCoordinates) =
        with(barycentricCoordinates) {
            d1 * w1 + d2 * w2 + d3 * w3
        }

    private fun toBitmapRange(d: Double) = min(max(d, 0.0), 1.0)

    private class TriangleEdgeYToX(
        minY: PointTexel,
        maxY: PointTexel
    ) {

        val yStart = minY.point.y
        val yEnd = maxY.point.y

        private val anchorX = minY.point.x.toDouble()
        private val xStep = (maxY.point.x - minY.point.x).toDouble() / (maxY.point.y - minY.point.y).toDouble()

        operator fun get(y: Int): Int {
            if (y !in yStart until yEnd) throw IllegalArgumentException("y = $y should be in range [$yStart, $yEnd>")
            return ceil(anchorX + (y - yStart) * xStep).toInt()
        }
    }

    private data class PointTexel(
        val point: Point,
        val texel: Point3d
    )
}