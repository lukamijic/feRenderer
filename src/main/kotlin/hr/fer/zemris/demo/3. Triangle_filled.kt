package hr.fer.zemris.demo

import hr.fer.zemris.color.Color
import hr.fer.zemris.demo.util.randomInt
import hr.fer.zemris.demo.util.randomPoint
import hr.fer.zemris.display.Display
import hr.fer.zemris.model.Point
import hr.fer.zemris.model.Triangle

private val pointRange = 0..1024

fun main() {

    val display = Display(1024, 1024, "Filled Triangles")
    val canvas = display.canvas

    var generatedTriangles = generateTestTriangles(2)
    var prevTime = System.currentTimeMillis()
    while (true) {
        canvas.clear(Color.BLACK)

        canvas.fillTriangle(Triangle(Point(-2, 512), Point(1025, 512), Point(512, 0)), Color.RED)
        canvas.fillTriangle(Triangle(Point(-2, 512), Point(1025, 512), Point(512, 1025)), Color.MAGENTA)
        canvas.fillTriangle(Triangle(Point(256, 500), Point(768, 500), Point(512, 300)), Color.YELLOW)
        canvas.fillTriangle(Triangle(Point(15, 564), Point(232, 412), Point(76, 754)), Color.GREEN)
        canvas.fillTriangle(Triangle(Point(900, 700), Point(850, 300), Point(600, 950)), Color.CYAN)

        if (System.currentTimeMillis() - prevTime >= 5000) {
            prevTime = System.currentTimeMillis()
            generatedTriangles = generateTestTriangles(2)
        }

        generatedTriangles.forEach {
            canvas.fillTriangle(
                Triangle(
                    it.p1,
                    it.p2,
                    it.p3
                ),
                it.color
            )
        }

        display.swapBuffers()
    }
}

private class TestTriangle(
    val p1: Point,
    val p2: Point,
    val p3: Point,
    val color: Color
)

private fun generateTestTriangles(n: Int) =
    (0..n).map {
        TestTriangle(
            randomPoint(pointRange),
            randomPoint(pointRange),
            randomPoint(pointRange),
            Color(randomInt(0..523332))
        )
    }