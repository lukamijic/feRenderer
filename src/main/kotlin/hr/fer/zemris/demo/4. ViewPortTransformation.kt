import hr.fer.zemris.color.Color
import hr.fer.zemris.display.Display
import hr.fer.zemris.geometry.model.Point
import hr.fer.zemris.geometry.model.Triangle
import hr.fer.zemris.math.matrix.Matrix
import hr.fer.zemris.math.transformations.rotateYMatrix
import hr.fer.zemris.math.transformations.translateMatrix
import hr.fer.zemris.math.util.vector
import hr.fer.zemris.math.vector.Vector
import hr.fer.zemris.renderer.viewport.ScreenSpaceTransform
import kotlin.math.abs
import kotlin.math.ceil

fun main() {

    val display = Display(1024, 1024, "ViewPort Transformation")
    val canvas = display.canvas

    val v1 = vector(-1, 0, 0f, 1f)
    val v2 = vector(0f, 1f, 0f, 1f)
    val v3 = vector(1, 0, 0f, 1f)

    val viewPortTransform = ScreenSpaceTransform(1024f, 1024f)

    val modelTransformator = ModelTransformator()

    while (true) {
        canvas.clear(Color.BLACK)

        val p1 = screenTransformToPoint(v1, viewPortTransform.viewPortMatrix(), modelTransformator.transformationMatrix())
        val p2 = screenTransformToPoint(v2, viewPortTransform.viewPortMatrix(), modelTransformator.transformationMatrix())
        val p3 = screenTransformToPoint(v3, viewPortTransform.viewPortMatrix(), modelTransformator.transformationMatrix())

        modelTransformator.step()
        canvas.fillTriangle(
            Triangle(p1, p2, p3),
            Color.RED,
            Color.GREEN,
            Color.BLUE
        )

        display.swapBuffers()
    }
}

private fun screenTransformToPoint(v: Vector, viewPortTransform: Matrix, modelTransformation: Matrix): Point =
    (v.toMatrix(Vector.ToMatrix.ROW) * modelTransformation * viewPortTransform).toVector()
        .let { it * (1f / it[3]) }
        .let { Point(ceil(it[0]).toInt(), ceil(it[1]).toInt()) }

private class ModelTransformator {

    var rot = 0f
    val rotStep = 0.03f

    var translation = 0f
    var translationStep = 0.005f

    fun step() {
        rot += rotStep
        translation += translationStep

        if (abs(translation) >= 0.5) {
            translationStep *= -1f
        }
    }

    fun transformationMatrix() =
         translateMatrix(translation, translation, 0f) * rotateYMatrix(rot)

}