package hr.fer.zemris.renderer


import hr.fer.zemris.color.Color
import hr.fer.zemris.math.matrix.Matrix
import hr.fer.zemris.math.transformations.identityMatrix
import hr.fer.zemris.renderer.lightning.LightCoefs
import hr.fer.zemris.resources.bitmap.Bitmap
import hr.fer.zemris.resources.mesh.Mesh

sealed class RenderObject(
    val id: String,
    val mesh: Mesh,
    var modelViewTransform: Matrix,
    var enableCulling: Boolean
)

class MeshRenderObject(
    id: String,
    mesh: Mesh,
    var color: Color,
    modelViewTransform: Matrix = identityMatrix(),
    enableCulling: Boolean = true
) : RenderObject(id, mesh, modelViewTransform, enableCulling)

class FillRenderObject(
    id: String,
    mesh: Mesh,
    var color: Color,
    modelViewTransform: Matrix = identityMatrix(),
    enableCulling: Boolean = true
) : RenderObject(id, mesh, modelViewTransform, enableCulling)

class BitmapRenderObject(
    id: String,
    mesh: Mesh,
    var bitmap: Bitmap,
    modelViewTransform: Matrix = identityMatrix(),
    enableCulling: Boolean = true
) : RenderObject(id, mesh, modelViewTransform, enableCulling)


sealed class GouardShadingRenderObject(
    id: String,
    mesh: Mesh,
    var lightsCoefs: LightCoefs,
    modelViewTransform: Matrix = identityMatrix(),
    enableCulling: Boolean = true
) : RenderObject(id, mesh, modelViewTransform, enableCulling)

class GouraudShadingFillRenderObject(
    id: String,
    mesh: Mesh,
    var color: Color,
    lightsCoefs: LightCoefs,
    modelViewTransform: Matrix = identityMatrix(),
    enableCulling: Boolean = true
) : GouardShadingRenderObject(id, mesh, lightsCoefs, modelViewTransform, enableCulling)

class GouraudShadingBitmapRenderObject(
    id: String,
    mesh: Mesh,
    var bitmap: Bitmap,
    lightsCoefs: LightCoefs,
    modelViewTransform: Matrix = identityMatrix(),
    enableCulling: Boolean = true
): GouardShadingRenderObject(id, mesh, lightsCoefs, modelViewTransform, enableCulling)