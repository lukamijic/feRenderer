package hr.fer.zemris.graphicsAlgorithms.draw

import hr.fer.zemris.geometry.model.Point
import hr.fer.zemris.geometry.model.Triangle
import hr.fer.zemris.graphicsAlgorithms.BarycentricCoordinates
import hr.fer.zemris.graphicsAlgorithms.BarycentricCoordinatesCalculator
import hr.fer.zemris.graphicsAlgorithms.util.BoundingBox
import hr.fer.zemris.math.util.Handedness
import kotlin.math.ceil

class TrianglePointsProcessor(
    private val triangle: Triangle,
    private val boundingBox: BoundingBox
) {

    private val handedness: Handedness
    private val topToBottom: TriangleEdgeYToX
    private val topToMiddle: TriangleEdgeYToX
    private val middleToBottom: TriangleEdgeYToX

    init {
        var minY = triangle.p1
        var midY = triangle.p2
        var maxY = triangle.p3

        if (maxY.y < midY.y) {
            val temp = maxY
            maxY = midY
            midY = temp
        }

        if (midY.y < minY.y) {
            val temp = midY
            midY = minY
            minY = temp
        }

        if (maxY.y < midY.y) {
            val temp = maxY
            maxY = midY
            midY = temp
        }

        handedness = Handedness.calculateHandiness(minY, maxY, midY)

        topToBottom = TriangleEdgeYToX(minY, maxY)
        topToMiddle = TriangleEdgeYToX(minY, midY)
        middleToBottom = TriangleEdgeYToX(midY, maxY)
    }

    fun processPoints(pointProcessor: (Int, Int, Double) -> Unit) {
        scanTriangleEdges(topToBottom, topToMiddle, handedness, pointProcessor)
        scanTriangleEdges(topToBottom, middleToBottom, handedness, pointProcessor)
    }

    private fun scanTriangleEdges(
        longEdge: TriangleEdgeYToX,
        shortEdge: TriangleEdgeYToX,
        handedness: Handedness,
        pointProcessor: (Int, Int, Double) -> Unit
    ) {
        val left = if (handedness == Handedness.LEFT) longEdge else shortEdge
        val right = if (handedness == Handedness.LEFT) shortEdge else longEdge

        for (y in shortEdge.yStart until shortEdge.yEnd) {
            scanLine(left[y], right[y], y, pointProcessor)
        }
    }

    /**
     * This methods works correctly if startX is before endX
     */
    private fun scanLine(startX: Int, endX: Int, y: Int, pointProcessor: (Int, Int, Double) -> Unit) {
        if ((y in boundingBox.minY..boundingBox.maxY).not()) return
        val xs = if (startX < boundingBox.minX) boundingBox.minX else startX
        val xe = if (endX > boundingBox.maxX) boundingBox.maxX else endX

        (xs..xe).forEach { x ->
            val barycentricCoordinates = BarycentricCoordinatesCalculator.calculateBarycentricCoordinate(triangle, x, y)
            pointProcessor(x, y, interpolateZ(triangle.p1.z, triangle.p2.z, triangle.p3.z, barycentricCoordinates))
        }
    }

    private fun interpolateZ(z1: Double, z2: Double, z3: Double, barycentricCoordinates: BarycentricCoordinates) = with(barycentricCoordinates) {
        z1 * w1 + z2 * w2 + z3 * w3
    }

    private class TriangleEdgeYToX(
        minY: Point,
        maxY: Point
    ) {
        val yStart = minY.y
        val yEnd = maxY.y
        private val anchorX = minY.x.toDouble()
        private val xStep = (maxY.x - minY.x).toDouble() / (maxY.y - minY.y).toDouble()

        operator fun get(y: Int): Int {
            if (y !in yStart until yEnd) throw IllegalArgumentException("y = $y should be in range [$yStart, $yEnd>")
            return ceil(anchorX + (y - yStart) * xStep).toInt()
        }
    }
}