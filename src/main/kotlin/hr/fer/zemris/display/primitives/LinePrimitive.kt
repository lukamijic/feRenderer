package hr.fer.zemris.display.primitives

import hr.fer.zemris.color.Color
import hr.fer.zemris.display.Canvas
import hr.fer.zemris.geometry.model.Point2i
import hr.fer.zemris.graphicsAlgorithms.BresenhamLineAlgorithm
import hr.fer.zemris.graphicsAlgorithms.lineclipping.CohenSutherlandLineClippingAlgorithm
import hr.fer.zemris.graphicsAlgorithms.lineclipping.LineClipping

data class LinePrimitive(
    val p1: Point2i,
    val p2: Point2i,
    val color: Color
) : Primitive {

    override fun draw(canvas: Canvas) {
        canvas.lineClipping
            .clip(p1, p2)?.let { (p1, p2) ->
                BresenhamLineAlgorithm.bresenhamCalculateLine(p1, p2)
                    .forEach { p ->
                        canvas.drawPixel(p.x, p.y, 0.0, color)
                    }
            }
    }
}