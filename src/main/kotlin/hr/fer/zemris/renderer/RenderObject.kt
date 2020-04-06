package hr.fer.zemris.renderer

import hr.fer.zemris.color.Color
import hr.fer.zemris.math.matrix.Matrix
import hr.fer.zemris.resources.mesh.Mesh

data class RenderObject(
    val mesh: Mesh,
    var modelViewTransform: Matrix,
    var enableCulling: Boolean,
    var color: Color,
    var renderType: RenderType
)

enum class RenderType {
    DRAW, FILL
}