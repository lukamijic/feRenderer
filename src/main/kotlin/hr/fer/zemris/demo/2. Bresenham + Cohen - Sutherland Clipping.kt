package hr.fer.zemris.demo

import hr.fer.zemris.color.Color
import hr.fer.zemris.demo.util.LineData
import hr.fer.zemris.display.Display
import hr.fer.zemris.geometry.model.Point
import hr.fer.zemris.graphicsAlgorithms.lineclipping.CohenSutherlandLineClippingAlgorithm

private val rectangle = listOf(
    LineData(Point(200, 200), Point(800, 200), Color.WHITE),
    LineData(Point(800, 200), Point(800, 800), Color.WHITE),
    LineData(Point(800, 800), Point(200, 800), Color.WHITE),
    LineData(Point(200, 800), Point(200, 200), Color.WHITE)
)

private val lines = listOf(
    LineData(Point(150, 500), Point(850, 500), Color.RED),
    LineData(Point(-1, 500), Point(500, -1), Color.GREEN),
    LineData(Point(199, 801), Point(801, 199), Color.GREEN),
    LineData(Point(150, 150), Point(850, 850), Color.RED)
)

fun main() {
    val display = Display(1024, 1024, "Cohen Sutherland Clipping")
    val canvas = display.canvas
    canvas.lineClipping = CohenSutherlandLineClippingAlgorithm(200, 800, 200, 800)
    while (true) {
        canvas.clear(Color.BLACK)

        rectangle.forEach {
            canvas.drawLine(it.p1, it.p2, it.color)
        }

        lines.forEach {
            canvas.drawLine(it.p1, it.p2, it.color)
        }

        display.swapBuffers()
    }
}

