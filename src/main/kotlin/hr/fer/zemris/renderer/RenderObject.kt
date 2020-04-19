package hr.fer.zemris.renderer


import hr.fer.zemris.color.Color
import hr.fer.zemris.math.matrix.Matrix
import hr.fer.zemris.resources.bitmap.Bitmap
import hr.fer.zemris.resources.mesh.Mesh

sealed class RenderObject(
    val mesh: Mesh,
    var modelViewTransform: Matrix,
    var enableCulling: Boolean
)

class MeshRenderObject(
    mesh: Mesh,
    modelViewTransform: Matrix,
    enableCulling: Boolean,
    var color: Color
) : RenderObject(mesh, modelViewTransform, enableCulling)

class FillRenderObject(
    mesh: Mesh,
    modelViewTransform: Matrix,
    enableCulling: Boolean,
    var color: Color
) : RenderObject(mesh, modelViewTransform, enableCulling)

class BitmapRenderObject(
    mesh: Mesh,
    modelViewTransform: Matrix,
    enableCulling: Boolean,
    var bitmap: Bitmap
) : RenderObject(mesh, modelViewTransform, enableCulling)