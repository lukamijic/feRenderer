package hr.fer.zemris.display.primitives

import hr.fer.zemris.color.Color
import hr.fer.zemris.display.Canvas
import hr.fer.zemris.geometry.model.Point
import hr.fer.zemris.geometry.model.Triangle
import hr.fer.zemris.graphicsAlgorithms.draw.TrianglePointsProcessor

class Triangle1cPrimitive(
    val triangle: Triangle,
    val color: Color
) :  Primitive {

    override fun draw(canvas: Canvas) {
        TrianglePointsProcessor(triangle, canvas.canvasBoundingBox)
            .processPoints { x, y, z -> canvas.drawPixel(x, y, z, color) }
    }
}
