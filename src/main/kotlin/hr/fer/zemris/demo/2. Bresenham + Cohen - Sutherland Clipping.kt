package hr.fer.zemris.demo

import hr.fer.zemris.color.Color
import hr.fer.zemris.display.Canvas
import hr.fer.zemris.display.Display
import hr.fer.zemris.display.primitives.LinePrimitive
import hr.fer.zemris.geometry.model.Point2i
import hr.fer.zemris.graphicsAlgorithms.clipping.line.CohenSutherlandLineClippingAlgorithm

private val rectangle = listOf(
    LinePrimitive(Point2i(200, 200), Point2i(800, 200), Color.WHITE),
    LinePrimitive(Point2i(800, 200), Point2i(800, 800), Color.WHITE),
    LinePrimitive(Point2i(800, 800), Point2i(200, 800), Color.WHITE),
    LinePrimitive(Point2i(200, 800), Point2i(200, 200), Color.WHITE)
)

private val lines = listOf(
    LinePrimitive(Point2i(150, 500), Point2i(850, 500), Color.RED),
    LinePrimitive(Point2i(-1, 500), Point2i(500, -1), Color.GREEN),
    LinePrimitive(Point2i(199, 801), Point2i(801, 199), Color.GREEN),
    LinePrimitive(Point2i(150, 150), Point2i(850, 850), Color.RED)
)

fun main() {
    val display = Display(1024, 1024, "Cohen Sutherland Clipping", Canvas(1024, 1024,
        CohenSutherlandLineClippingAlgorithm(
            200,
            800,
            200,
            800
        )
    ))
    val canvas = display.canvas
    while (true) {
        canvas.clear(Color.BLACK)
        canvas.clearDepth()

        rectangle.forEach { it.draw(canvas) }

        lines.forEach { it.draw(canvas) }

        display.swapBuffers()
    }
}

