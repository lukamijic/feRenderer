package hr.fer.zemris.renderer.dsl

import hr.fer.zemris.color.Color
import hr.fer.zemris.math.transformations.identityMatrix
import hr.fer.zemris.renderer.*
import hr.fer.zemris.renderer.lightning.LightCoefs
import hr.fer.zemris.resources.bitmap.Bitmap
import hr.fer.zemris.resources.loader.BitmapLoader
import hr.fer.zemris.resources.loader.ObjLoader
import hr.fer.zemris.resources.mesh.Mesh
import java.lang.RuntimeException

fun meshRenderObject(block: MeshRenderObjectBuilder.() -> Unit): RenderObject =
    MeshRenderObjectBuilder().apply(block).build()

fun fillRenderObject(block: FillRenderObjectBuilder.() -> Unit): RenderObject =
    FillRenderObjectBuilder().apply(block).build()

fun bitmapRenderObject(block: BitmapRenderObjectBuilder.() -> Unit): RenderObject =
    BitmapRenderObjectBuilder().apply(block).build()

fun gouraudShadingFillRenderObject(block: GouraudShadingFillRenderObjectBuilder.() -> Unit) : RenderObject =
    GouraudShadingFillRenderObjectBuilder().apply(block).build()

fun gouraudShadingBitmapRenderObject(block: GouraudShadingBitmapRenderObjectBuilder.() -> Unit) : RenderObject =
    GouraudShadingBitmapRenderObjectBuilder().apply(block).build()

abstract class RenderObjectBuilder {

    var id: String? = null
    var mesh: Mesh? = null
    var modelViewMatrix = identityMatrix()
    var enableCulling = true

    var meshRes: String? = null

    abstract fun build(): RenderObject
}

class MeshRenderObjectBuilder : RenderObjectBuilder() {

    var color: Color? = null

    override fun build(): RenderObject {
        val mesh = mesh ?: (meshRes?.run { ObjLoader.load(this) } ?: throw RuntimeException("Mesh not defined."))

        return MeshRenderObject(
            id!!,
            mesh,
            color!!,
            modelViewMatrix,
            enableCulling
        )
    }
}

class FillRenderObjectBuilder : RenderObjectBuilder() {

    var color: Color? = null

    override fun build(): RenderObject {
        val mesh = mesh ?: (meshRes?.run { ObjLoader.load(this) } ?: throw RuntimeException("Mesh not defined."))

        return FillRenderObject(
            id!!,
            mesh,
            color!!,
            modelViewMatrix,
            enableCulling
        )
    }
}

class BitmapRenderObjectBuilder : RenderObjectBuilder() {

    var bitmap: Bitmap? = null
    var bitmapRes: String? = null

    override fun build(): RenderObject {
        val mesh = mesh ?: (meshRes?.run { ObjLoader.load(this) } ?: throw RuntimeException("Mesh not defined."))
        val bitmap =
            bitmap ?: (bitmapRes?.run { BitmapLoader.load(this) } ?: throw RuntimeException("Bitmap not defined"))

        return BitmapRenderObject(
            id!!,
            mesh,
            bitmap,
            modelViewMatrix,
            enableCulling
        )
    }
}

class GouraudShadingFillRenderObjectBuilder: RenderObjectBuilder() {

    var color: Color? = null
    var lightsCoefs : LightCoefs? = null

    override fun build(): RenderObject {
        val mesh = mesh ?: (meshRes?.run { ObjLoader.load(this) } ?: throw RuntimeException("Mesh not defined."))

        return GouraudShadingFillRenderObject(
            id!!,
            mesh,
            color!!,
            lightsCoefs!!,
            modelViewMatrix,
            enableCulling
        )
    }
}

class GouraudShadingBitmapRenderObjectBuilder: RenderObjectBuilder() {

    var bitmap: Bitmap? = null
    var bitmapRes: String? = null
    var lightsCoefs : LightCoefs? = null

    override fun build(): RenderObject {
        val mesh = mesh ?: (meshRes?.run { ObjLoader.load(this) } ?: throw RuntimeException("Mesh not defined."))
        val bitmap =
            bitmap ?: (bitmapRes?.run { BitmapLoader.load(this) } ?: throw RuntimeException("Bitmap not defined"))

        return GouraudShadingBitmapRenderObject(
            id!!,
            mesh,
            bitmap,
            lightsCoefs!!,
            modelViewMatrix,
            enableCulling
        )
    }
}