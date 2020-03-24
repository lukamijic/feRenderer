package hr.fer.zemris.graphicsAlgorithms.lineclipping

import hr.fer.zemris.geometry.model.Point

private const val INSIDE = 0 // 0000
private const val LEFT = 1 // 0001
private const val RIGHT = 2 // 0010
private const val BOTTOM = 4 // 0100
private const val TOP = 8 // 1000

class CohenSutherlandLineClippingAlgorithm(
    private val xMin: Int,
    private val xMax: Int,
    private val yMin: Int,
    private val yMax: Int
) : LineClipping {

    /**
     * Clips line into visible area.
     * If line is not visible return null otherwise
     * return [Pair] of [Point]s where first one will be new start
     * and the other one will be new end.
     */
    override fun clip(p1: Point, p2: Point ): Pair<Point, Point>? {
        var (x1, y1) = p1
        var (x2, y2) = p2

        var areaCode1 = pointToAreaCode(x1, y1)
        var areaCode2 = pointToAreaCode(x2, y2)

        while (true) {
            if (areaCode1 == INSIDE && areaCode2 == INSIDE) {
                return Pair(Point(x1, y1), Point(x2, y2))
            } else if (areaCode1 and areaCode2 != INSIDE) {
                return null
            }

            var x = 0
            var y = 0
            val codeToCheck = if (areaCode1 > areaCode2) areaCode1 else areaCode2

            when {
                codeToCheck and TOP != INSIDE -> {
                    x = calculateX(p1.x.toFloat(), p2.x.toFloat(),p1.y.toFloat(), p2.y.toFloat(), yMax.toFloat())
                    y = yMax
                }
                codeToCheck and BOTTOM != INSIDE -> {
                    x = calculateX(p1.x.toFloat(), p2.x.toFloat(), p1.y.toFloat(), p2.y.toFloat(), yMin.toFloat())
                    y = yMin
                }
                codeToCheck and RIGHT != INSIDE -> {
                    y = calculateY(p1.x.toFloat(), p2.x.toFloat(), p1.y.toFloat(), p2.y.toFloat(), xMax.toFloat())
                    x = xMax
                }
                codeToCheck and LEFT != INSIDE -> {
                    y = calculateY(p1.x.toFloat(), p2.x.toFloat(), p1.y.toFloat(), p2.y.toFloat(), xMin.toFloat())
                    x = xMin
                }
            }

            if (codeToCheck == areaCode1) {
                x1 = x
                y1 = y
                areaCode1 = pointToAreaCode(x, y)
            } else {
                x2 = x
                y2 = y
                areaCode2 = pointToAreaCode(x, y)
            }
        }
    }

    private fun pointToAreaCode(x: Int, y: Int): Int {
        var code = INSIDE

        code = code or if (x < xMin) LEFT else if (x > xMax) RIGHT else INSIDE
        code = code or if (y < yMin) BOTTOM else if (y > yMax) TOP else INSIDE

        return code
    }

    private fun calculateX(x1: Float, x2: Float, y1: Float, y2: Float, y: Float): Int =
        (x1 + (x2 - x1) * (y - y1) / (y2 - y1)).toInt()

    private fun calculateY(x1: Float, x2: Float, y1: Float, y2: Float, x: Float): Int =
        (y1 + (y2 - y1) * (x - x1) / (x2 - x1)).toInt()
}