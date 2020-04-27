package hr.fer.zemris.display.primitives

import hr.fer.zemris.color.Color
import hr.fer.zemris.color.RGB
import hr.fer.zemris.display.Canvas
import hr.fer.zemris.geometry.model.Triangle
import hr.fer.zemris.graphicsAlgorithms.BarycentricCoordinatesCalculator
import hr.fer.zemris.graphicsAlgorithms.draw.TrianglePointsProcessor
import hr.fer.zemris.graphicsAlgorithms.interpolate.Interpolate
import hr.fer.zemris.graphicsAlgorithms.lightning.Lightning
import hr.fer.zemris.renderer.lightning.Intensity

data class Triangle1cShadingPrimitive(
    private val triangle: Triangle,
    private val i1: Intensity,
    private val i2: Intensity,
    private val i3: Intensity,
    private val c: Color
) : Primitive {

    override fun draw(canvas: Canvas) {
        TrianglePointsProcessor(triangle, canvas.canvasBoundingBox)
            .processPoints { x, y, z ->
                val barycentricCoordinates = BarycentricCoordinatesCalculator.calculateBarycentricCoordinate(triangle, x, y)
                val rI = Interpolate.interpolateDouble(i1.r, i2.r, i3.r, barycentricCoordinates)
                val gI = Interpolate.interpolateDouble(i1.r, i2.r, i3.r, barycentricCoordinates)
                val bI = Interpolate.interpolateDouble(i1.r, i2.r, i3.r, barycentricCoordinates)

                val shadedColor = c.toRGB().let {
                    RGB(
                        Lightning.applyLight(it.r, rI),
                        Lightning.applyLight(it.g, gI),
                        Lightning.applyLight(it.b, bI)
                    )
                }.toColor()
                canvas.drawPixel(x, y, z, shadedColor)
            }
    }
}