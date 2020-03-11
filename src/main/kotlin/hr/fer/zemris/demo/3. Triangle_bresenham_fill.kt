package hr.fer.zemris.demo

import hr.fer.zemris.color.Color
import hr.fer.zemris.color.RGB
import hr.fer.zemris.display.Display
import hr.fer.zemris.model.Point
import hr.fer.zemris.model.Triangle

fun main() {

    val display = Display(1024, 1024, "Bresenham Filled Triangles")
    val canvas = display.canvas


    val triangle = Triangle(Point(0, 512), Point(512, 0), Point(1024, 512))
    while (true) {
        canvas.fillTriangle(triangle, RGB.RED, RGB.GREEN, RGB.BLUE)

        display.swapBuffers()
    }
}