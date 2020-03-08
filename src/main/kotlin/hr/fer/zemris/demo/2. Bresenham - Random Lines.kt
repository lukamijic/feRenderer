package hr.fer.zemris.demo

import hr.fer.zemris.color.Color
import hr.fer.zemris.display.Display
import hr.fer.zemris.model.Point
import kotlin.random.Random
import kotlin.random.nextInt

private val random = Random(System.currentTimeMillis())

private val pointRange = -1200..1200

private val colors = listOf(
    Color.WHITE,
    Color.RED,
    Color.GREEN,
    Color.BLUE,
    Color.MAGENTA,
    Color.YELLOW,
    Color.CYAN
)

fun main() {

    val display = Display(1024, 1024, "Random Lines")
    val canvas = display.canvas

    val lineCount = 25

    while (true) {
        canvas.clear(Color.BLACK)

        (0..lineCount).forEach {
            canvas.drawLine(
                randomPoint(),
                randomPoint(),
                randomColor()
            )
        }

        display.swapBuffers()
    }
}

fun randomPoint(): Point =
    Point(
        random.nextInt(pointRange),
        random.nextInt(pointRange)
    )

fun randomColor(): Color =
    colors[random.nextInt(colors.size - 1)]