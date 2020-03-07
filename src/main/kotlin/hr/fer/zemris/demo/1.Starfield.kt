package hr.fer.zemris.demo

import hr.fer.zemris.color.Color
import hr.fer.zemris.display.Canvas
import hr.fer.zemris.display.Display
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
        val delta = (currTime - prevTime).toFloat() / NANO_IN_SEC
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
    private val starsX: FloatArray = FloatArray(starsCount) { randomXStarPosition() }
    private val starsY: FloatArray = FloatArray(starsCount) { randomYStarPosition() }
    private val starsZ: FloatArray = FloatArray(starsCount) { randomZStarPosition() }

    private val tanHalfFov: Float = tan(Math.toRadians(fov / 2.0)).toFloat()

    fun render(canvas: Canvas, delta: Float) {
        canvas.clear(Color.BLACK)

        val halfWidth = canvas.width / 2.0f
        val halfHeight =  canvas.height / 2.0f

        for (i in 0 until starsCount) {
            starsZ[i] -= (delta * speed)
            if (starsZ[i] < 0) resetStar(i)

            drawStar(canvas, i, halfWidth, halfHeight)
        }
    }

    private fun drawStar(canvas: Canvas, index: Int, halfWidth: Float, halfHeight: Float) {
        val x = (starsX[index]/(starsZ[index] * tanHalfFov) * halfWidth + halfWidth).toInt()
        val y = (starsY[index]/(starsZ[index] * tanHalfFov) * halfHeight + halfHeight).toInt()

        if ((x in 0 until  canvas.width - 1).not() || (y in (0 until canvas.height - 1)).not()) {
            resetStar(index)
        } else {
            canvas.drawPixel(x, y, Color.WHITE)
        }
    }

    private fun resetStar(index: Int) {
        starsX[index] = randomXStarPosition()
        starsY[index] = randomYStarPosition()
        starsZ[index] = randomZStarPosition()
    }

    private fun randomXStarPosition() = (Math.random() - 0.5).toFloat() * 2 * spread
    private fun randomYStarPosition() = (Math.random() - 0.5).toFloat() * 2 * spread
    private fun randomZStarPosition() = (Math.random() - 0.00001).toFloat() * spread
}