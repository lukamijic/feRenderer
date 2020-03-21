package hr.fer.zemris.demo

import hr.fer.zemris.color.Color
import hr.fer.zemris.display.Display
import hr.fer.zemris.geometry.model.Point

private val lineData = listOf(
    // 90 degrees RED
    LineData(
        Point(512, 512),
        Point(512, 1025), Color.RED),
    LineData(
        Point(512, 512),
        Point(512, -2), Color.RED),
    LineData(
        Point(512, 512),
        Point(-2, 512), Color.RED),
    LineData(
        Point(512, 512),
        Point(1025, 512), Color.RED),
    // 45 degrees YELLOW
    LineData(
        Point(512, 512),
        Point(-2, -2), Color.YELLOW),
    LineData(
        Point(512, 512),
        Point(-2, 1025), Color.YELLOW),
    LineData(
        Point(512, 512),
        Point(1025, -2), Color.YELLOW),
    LineData(
        Point(512, 512),
        Point(1025, 1025), Color.YELLOW),
    // 0-45 degrees GREEN
    LineData(
        Point(512, 512),
        Point(1025, 256), Color.GREEN),
    LineData(
        Point(512, 512),
        Point(1025, 768), Color.GREEN),
    LineData(
        Point(512, 512),
        Point(-2, 256), Color.GREEN),
    LineData(
        Point(512, 512),
        Point(-2, 768), Color.GREEN),
    // 45-90 degrees MAGENTA
    LineData(
        Point(512, 512),
        Point(768, -2), Color.MAGENTA),
    LineData(
        Point(512, 512),
        Point(768, 1025), Color.MAGENTA),
    LineData(
        Point(512, 512),
        Point(256, 1025), Color.MAGENTA),
    LineData(
        Point(512, 512),
        Point(256, -2), Color.MAGENTA)
)

fun main() {

    val display = Display(1024, 1024, "Static Lines")
    val canvas = display.canvas

    while (true) {
        canvas.clear(Color.BLACK)

        lineData.forEach {
            canvas.drawLine(it.p1, it.p2, it.color)
        }

        display.swapBuffers()
    }
}

private data class LineData(
    val p1: Point,
    val p2: Point,
    val color: Color
)