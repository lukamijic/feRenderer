package hr.fer.zemris.demo

import hr.fer.zemris.color.Color
import hr.fer.zemris.display.Display
import hr.fer.zemris.display.primitives.LinePrimitive
import hr.fer.zemris.geometry.model.Point2i

private val linePrimitives = listOf(
    // 90 degrees RED
    LinePrimitive(
        Point2i(512, 512),
        Point2i(512, 1025), Color.RED
    ),
    LinePrimitive(
        Point2i(512, 512),
        Point2i(512, -2), Color.RED
    ),
    LinePrimitive(
        Point2i(512, 512),
        Point2i(-2, 512), Color.RED
    ),
    LinePrimitive(
        Point2i(512, 512),
        Point2i(1025, 512), Color.RED
    ),
    // 45 degrees YELLOW
    LinePrimitive(
        Point2i(512, 512),
        Point2i(-2, -2), Color.YELLOW
    ),
    LinePrimitive(
        Point2i(512, 512),
        Point2i(-2, 1025), Color.YELLOW
    ),
    LinePrimitive(
        Point2i(512, 512),
        Point2i(1025, -2), Color.YELLOW
    ),
    LinePrimitive(
        Point2i(512, 512),
        Point2i(1025, 1025), Color.YELLOW
    ),
    // 0-45 degrees GREEN
    LinePrimitive(
        Point2i(512, 512),
        Point2i(1025, 256), Color.GREEN
    ),
    LinePrimitive(
        Point2i(512, 512),
        Point2i(1025, 768), Color.GREEN
    ),
    LinePrimitive(
        Point2i(512, 512),
        Point2i(-2, 256), Color.GREEN
    ),
    LinePrimitive(
        Point2i(512, 512),
        Point2i(-2, 768), Color.GREEN
    ),
    // 45-90 degrees MAGENTA
    LinePrimitive(
        Point2i(512, 512),
        Point2i(768, -2), Color.MAGENTA
    ),
    LinePrimitive(
        Point2i(512, 512),
        Point2i(768, 1025), Color.MAGENTA
    ),
    LinePrimitive(
        Point2i(512, 512),
        Point2i(256, 1025), Color.MAGENTA
    ),
    LinePrimitive(
        Point2i(512, 512),
        Point2i(256, -2), Color.MAGENTA
    )
)

fun main() {

    val display = Display(1024, 1024, "Static Lines")
    val canvas = display.canvas

    while (true) {
        canvas.clear(Color.BLACK)
        canvas.clearDepth()

        linePrimitives.forEach { it.draw(canvas) }

        display.swapBuffers()
    }
}
