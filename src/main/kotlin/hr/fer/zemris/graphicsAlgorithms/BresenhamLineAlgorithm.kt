package hr.fer.zemris.graphicsAlgorithms

import hr.fer.zemris.geometry.model.Point2i
import java.util.*

object BresenhamLineAlgorithm {

    fun bresenhamCalculateLine(start: Point2i, end: Point2i): List<Point2i> =
        when {
            start.x <= end.x ->
                when {
                    start.y <= end.y -> bresenhamCalculateLine0To90(start, end)
                    else -> bresenhamCalculateLinePoints0ToMinus90(start, end)
                }
            else ->
                when {
                    start.y >= end.y -> bresenhamCalculateLine0To90(end, start)
                    else -> bresenhamCalculateLinePoints0ToMinus90(end, start)
                }
        }

    /**
     * direction coef - (e.y - s,y) / (e.x - s.x)
     * a - direction coef multiplied by 2 * (e.x - s.x)
     */
    private fun bresenhamCalculateLine0To90(s: Point2i, e: Point2i): List<Point2i> =
        if (e.y - s.y <= e.x - s.x) {
            val (xs, ys) = s
            val (xe, ye) = e

            bresenhamCalculateLinePoints0To90(xs, ys, xe, ye, false)
        } else {
            val (ys, xs) = s
            val (ye, xe) = e

            bresenhamCalculateLinePoints0To90(xs, ys, xe, ye, true)
        }

    private fun bresenhamCalculateLinePoints0To90(
        xs: Int,
        ys: Int,
        xe: Int,
        ye: Int,
        shouldSwap: Boolean
    ): List<Point2i> =
        LinkedList<Point2i>().apply {
            val a = 2 * (ye - ys)
            val correction = -2 * (xe - xs)
            var currentY = ys
            var errorY = -(xe - xs)

            for (x in xs..xe) {
                if (!shouldSwap) add(Point2i(x, currentY)) else add(
                    Point2i(currentY, x)
                )
                errorY += a
                if (errorY >= 0) {
                    errorY += correction
                    currentY++
                }
            }
        }

    private fun bresenhamCalculateLinePoints0ToMinus90(s: Point2i, e: Point2i): List<Point2i> =
        if (-(e.y - s.y) <= e.x - s.x) {
            val (xs, ys) = s
            val (xe, ye) = e

            bresenhamCalculateLinePoints0ToMinus90(xs, ys, xe, ye, false)
        } else {
            val (ys, xs) = e
            val (ye, xe) = s

            bresenhamCalculateLinePoints0ToMinus90(xs, ys, xe, ye, true)
        }

    private fun bresenhamCalculateLinePoints0ToMinus90(
        xs: Int,
        ys: Int,
        xe: Int,
        ye: Int,
        shouldSwap: Boolean
    ): List<Point2i> =
        LinkedList<Point2i>().apply {
            val a = 2 * (ye - ys)
            val correction = 2 * (xe - xs)
            var currentY = ys
            var errorY = xe - xs

            for (x in xs..xe) {
                if (!shouldSwap) add(Point2i(x, currentY)) else add(
                    Point2i(currentY, x)
                )
                errorY += a
                if (errorY <= 0) {
                    errorY += correction
                    currentY--
                }
            }
        }
}
