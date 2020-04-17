package hr.fer.zemris.demo

import hr.fer.zemris.color.Color
import hr.fer.zemris.display.Canvas
import hr.fer.zemris.display.Display
import hr.fer.zemris.display.primitives.PointPrimitive
import java.util.concurrent.TimeUnit
import kotlin.math.tan

private val NANO_IN_SEC = TimeUnit.SECONDS.toNanos(1)

fun main() {
    val display = Display(1024, 1024, "Starfield")
    val canvas = display.canvas

    val starField = StarField(2500, 25.0f, 10.0f, 60)

    var prevTime = System.nanoTime()
    while(true) {
        val currTime = System.nanoTime()
        val delta = (currTime - prevTime).toDouble() / NANO_IN_SEC
        prevTime = currTime

        starField.render(canvas, delta)
        display.swapBuffers()
    }
}

class StarField(
    private val starsCount: Int,
    private val spread: Float,
    private val speed: Float,
    fov: Int
) {

    /**
     * Start location of stars should be in [-1, 1] range for x and y and [0, 1] for z
     */
    private val starsX: DoubleArray = DoubleArray(starsCount) { randomXStarPosition() }
    private val starsY: DoubleArray = DoubleArray(starsCount) { randomYStarPosition() }
    private val starsZ: DoubleArray = DoubleArray(starsCount) { randomZStarPosition() }

    private val tanHalfFov: Float = tan(Math.toRadians(fov / 2.0)).toFloat()

    fun render(canvas: Canvas, delta: Double) {
        canvas.clear(Color.BLACK)
        canvas.clearDepth()

        val halfWidth = canvas.width / 2.0
        val halfHeight =  canvas.height / 2.0

        for (i in 0 until starsCount) {
            starsZ[i] -= (delta * speed)
            if (starsZ[i] < 0) resetStar(i)

            drawStar(canvas, i, halfWidth, halfHeight)
        }
    }

    private fun drawStar(canvas: Canvas, index: Int, halfWidth: Double, halfHeight: Double) {
        val x = (starsX[index]/(starsZ[index] * tanHalfFov) * halfWidth + halfWidth).toInt()
        val y = (starsY[index]/(starsZ[index] * tanHalfFov) * halfHeight + halfHeight).toInt()

        val pointPrimitive = PointPrimitive(x, y, 0.0, Color.WHITE)
        if ((x in 0 until  canvas.width - 1).not() || (y in (0 until canvas.height - 1)).not()) {
            resetStar(index)
        } else {
            pointPrimitive.draw(canvas)
        }
    }

    private fun resetStar(index: Int) {
        starsX[index] = randomXStarPosition()
        starsY[index] = randomYStarPosition()
        starsZ[index] = randomZStarPosition()
    }

    private fun randomXStarPosition() = (Math.random() - 0.5) * 2 * spread
    private fun randomYStarPosition() = (Math.random() - 0.5) * 2 * spread
    private fun randomZStarPosition() = (Math.random() - 0.00001) * spread
}