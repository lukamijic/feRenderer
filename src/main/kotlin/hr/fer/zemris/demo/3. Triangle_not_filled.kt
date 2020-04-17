package hr.fer.zemris.demo

import hr.fer.zemris.color.Color
import hr.fer.zemris.demo.util.randomColor
import hr.fer.zemris.demo.util.randomPoint2i
import hr.fer.zemris.display.Display
import hr.fer.zemris.display.primitives.UnfilledTrianglePrimitive
import hr.fer.zemris.geometry.model.Point2i
import hr.fer.zemris.geometry.model.Triangle2i

private val Point2iRange = 0..1024

fun main() {

    val display = Display(1024, 1024, "Unfilled Triangles")
    val canvas = display.canvas

    while (true) {
        canvas.clear(Color.BLACK)
        canvas.clearDepth()

        UnfilledTrianglePrimitive(
            Triangle2i(
                Point2i(-2, 512),
                Point2i(1025, 512),
                Point2i(512, 0)
            ),
            Color.RED
        ).draw(canvas)

        UnfilledTrianglePrimitive(
            Triangle2i(
                Point2i(-2, 512),
                Point2i(1025, 512),
                Point2i(512, 1025)
            ),
            Color.MAGENTA
        ).draw(canvas)

        UnfilledTrianglePrimitive(
            Triangle2i(
                Point2i(256, 500),
                Point2i(768, 500),
                Point2i(512, 300)
            ),
            Color.YELLOW
        ).draw(canvas)

        UnfilledTrianglePrimitive(
            Triangle2i(
                Point2i(15, 564),
                Point2i(232, 412),
                Point2i(76, 754)
            ),
            Color.GREEN
        ).draw(canvas)

        UnfilledTrianglePrimitive(
            Triangle2i(
                Point2i(900, 700),
                Point2i(850, 300),
                Point2i(600, 950)
            ),
            Color.CYAN
        ).draw(canvas)


        (0..2).forEach {
            UnfilledTrianglePrimitive(
                Triangle2i(
                    randomPoint2i(Point2iRange),
                    randomPoint2i(Point2iRange),
                    randomPoint2i(Point2iRange)
                ),
                randomColor()
            ).draw(canvas)
        }


        display.swapBuffers()
    }
}