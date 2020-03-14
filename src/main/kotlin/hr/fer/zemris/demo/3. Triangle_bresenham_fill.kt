package hr.fer.zemris.demo

import hr.fer.zemris.color.Color
import hr.fer.zemris.demo.util.randomColor
import hr.fer.zemris.demo.util.randomPoint
import hr.fer.zemris.display.Display
import hr.fer.zemris.model.Triangle

fun main() {

    val display = Display(1024, 1024, "Bresenham Filled Triangles")
    val canvas = display.canvas


    var coloredTriangle = ColoredTriangle(
        Triangle(
            randomPoint(0..1024),
            randomPoint(0..1024),
            randomPoint(0..1024)
        ),
        randomColor(),
        randomColor(),
        randomColor()
    )
    var currentTime = System.currentTimeMillis()
    while (true) {
        canvas.clear(Color.BLACK)
        if (System.currentTimeMillis() - currentTime > 2000) {
            currentTime = System.currentTimeMillis()
            coloredTriangle = ColoredTriangle(
                Triangle(
                    randomPoint(0..1024),
                    randomPoint(0..1024),
                    randomPoint(0..1024)
                ),
                randomColor(),
                randomColor(),
                randomColor()
            )
        }
        with(coloredTriangle) { canvas.fillTriangle(triangle, c1, c2, c3)}
        display.swapBuffers()
    }
}

private data class ColoredTriangle(
    val triangle: Triangle,
    val c1: Color,
    val c2: Color,
    val c3: Color
)
