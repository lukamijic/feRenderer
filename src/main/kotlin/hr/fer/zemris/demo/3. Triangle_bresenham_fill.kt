package hr.fer.zemris.demo

import hr.fer.zemris.color.Color
import hr.fer.zemris.demo.util.randomColor
import hr.fer.zemris.demo.util.randomPoint
import hr.fer.zemris.display.Display
import hr.fer.zemris.display.primitives.Triangle3cPrimitive
import hr.fer.zemris.geometry.model.Triangle

fun main() {

    val display = Display(1024, 1024, "Bresenham Filled Triangles")
    val canvas = display.canvas


    var coloredTriangle =
        Triangle3cPrimitive(
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
        canvas.clearDepth()

        if (System.currentTimeMillis() - currentTime > 2000) {
            currentTime = System.currentTimeMillis()
            coloredTriangle = Triangle3cPrimitive(
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
        coloredTriangle.draw(canvas)
        display.swapBuffers()
    }
}

private data class ColoredTriangle(
    val triangle: Triangle,
    val c1: Color,
    val c2: Color,
    val c3: Color
)
