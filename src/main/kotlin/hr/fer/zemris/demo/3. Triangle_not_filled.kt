package hr.fer.zemris.demo

import hr.fer.zemris.color.Color
import hr.fer.zemris.demo.util.randomColor
import hr.fer.zemris.demo.util.randomPoint
import hr.fer.zemris.display.Display
import hr.fer.zemris.model.Point

private val pointRange = 0..1024

fun main() {

    val display = Display(1024, 1024, "Unfilled Triangles")
    val canvas = display.canvas

    while (true) {
        canvas.clear(Color.BLACK)

        canvas.drawTriangle(Point(-2, 512), Point(1025, 512), Point(512, 0), Color.RED)
        canvas.drawTriangle(Point(-2, 512), Point(1025, 512), Point(512, 1025), Color.MAGENTA)
        canvas.drawTriangle(Point(256, 500), Point(768, 500), Point(512, 300), Color.YELLOW)
        canvas.drawTriangle(Point(15, 564), Point(232, 412), Point(76, 754), Color.GREEN)
        canvas.drawTriangle(Point(900, 700), Point(850, 300), Point(600, 950), Color.CYAN)

        (0..5).forEach {
            canvas.drawTriangle(
                randomPoint(pointRange),
                randomPoint(pointRange),
                randomPoint(pointRange),
                randomColor()
            )
        }


        display.swapBuffers()
    }
}