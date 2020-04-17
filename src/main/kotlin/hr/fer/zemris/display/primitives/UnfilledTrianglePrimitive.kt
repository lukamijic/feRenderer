package hr.fer.zemris.display.primitives

import hr.fer.zemris.color.Color
import hr.fer.zemris.display.Canvas
import hr.fer.zemris.geometry.model.Point2i
import hr.fer.zemris.geometry.model.Triangle2i

data class UnfilledTrianglePrimitive(
    val triangle2i: Triangle2i,
    val color: Color
) : Primitive {

    override fun draw(canvas: Canvas) = with(triangle2i) {
        LinePrimitive(p1, p2, color).draw(canvas)
        LinePrimitive(p1, p3, color).draw(canvas)
        LinePrimitive(p2, p3, color).draw(canvas)
    }

}