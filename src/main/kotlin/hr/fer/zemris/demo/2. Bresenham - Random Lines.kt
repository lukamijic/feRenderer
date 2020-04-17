package hr.fer.zemris.demo

import hr.fer.zemris.color.Color
import hr.fer.zemris.demo.util.randomColor
import hr.fer.zemris.demo.util.randomPoint
import hr.fer.zemris.demo.util.randomPoint2i
import hr.fer.zemris.display.Display
import hr.fer.zemris.display.primitives.LinePrimitive

private val pointRange = -1200..1200

fun main() {

    val display = Display(1024, 1024, "Random Lines")
    val canvas = display.canvas

    val lineCount = 25

    while (true) {
        canvas.clear(Color.BLACK)
        canvas.clearDepth()

        (0..lineCount).forEach {
            LinePrimitive(
                randomPoint2i(pointRange),
                randomPoint2i(pointRange),
                randomColor()
            ).draw(canvas)
        }

        display.swapBuffers()
    }
}