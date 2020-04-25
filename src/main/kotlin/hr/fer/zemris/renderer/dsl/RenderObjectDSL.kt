package hr.fer.zemris.renderer.dsl

import hr.fer.zemris.color.Color
import hr.fer.zemris.math.transformations.identityMatrix
import hr.fer.zemris.renderer.BitmapRenderObject
import hr.fer.zemris.renderer.FillRenderObject
import hr.fer.zemris.renderer.MeshRenderObject
import hr.fer.zemris.renderer.RenderObject
import hr.fer.zemris.resources.bitmap.Bitmap
import hr.fer.zemris.resources.loader.BitmapLoader
import hr.fer.zemris.resources.loader.ObjLoader
import hr.fer.zemris.resources.mesh.Mesh
import java.lang.RuntimeException

fun meshRenderObject(block: RenderObjectBuilder.() -> Unit) : RenderObject = RenderObjectBuilder().apply(block).buildMeshRenderObject()
fun fillRenderObject(block: RenderObjectBuilder.() -> Unit) : RenderObject = RenderObjectBuilder().apply(block).buildFillRenderObject()
fun bitmapRenderObject(block: RenderObjectBuilder.() -> Unit) : RenderObject = RenderObjectBuilder().apply(block).buildBitmapRenderObject()

class RenderObjectBuilder {

    var id: String? = null
    var mesh: Mesh? = null
    var modelViewMatrix = identityMatrix()
    var enableCulling = true
    var color: Color? = null
    var bitmap: Bitmap? = null

    var meshRes: String? = null
    var bitmapRes: String? = null

    fun buildMeshRenderObject(): RenderObject {
        val mesh = mesh ?: (meshRes?.run { ObjLoader.load(this) } ?: throw RuntimeException("Mesh not defined."))

        return MeshRenderObject(
            id!!,
            mesh,
            color!!,
            modelViewMatrix,
            enableCulling
        )
    }

    fun buildFillRenderObject(): RenderObject {
        val mesh = mesh ?: (meshRes?.run { ObjLoader.load(this) } ?: throw RuntimeException("Mesh not defined."))

        return FillRenderObject(
            id!!,
            mesh,
            color!!,
            modelViewMatrix,
            enableCulling
        )
    }

    fun buildBitmapRenderObject(): RenderObject {
        val mesh = mesh ?: (meshRes?.run { ObjLoader.load(this) } ?: throw RuntimeException("Mesh not defined."))
        val bitmap = bitmap ?: (bitmapRes?.run { BitmapLoader.load(this) } ?: throw RuntimeException("Bitmap not defined"))

        return BitmapRenderObject(
            id!!,
            mesh,
            bitmap,
            modelViewMatrix,
            enableCulling
        )
    }
}