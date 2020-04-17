package hr.fer.zemris.display.primitives

import hr.fer.zemris.color.Color
import hr.fer.zemris.display.Canvas

data class PointPrimitive(
    val x: Int,
    val y: Int,
    val z: Double,
    val color: Color
) : Primitive {

    override fun draw(canvas: Canvas) =
        canvas.drawPixel(x, y, z, color)
}