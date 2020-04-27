package hr.fer.zemris.renderer.dsl

import hr.fer.zemris.math.vector.Vector
import hr.fer.zemris.renderer.lightning.Intensity
import hr.fer.zemris.renderer.lightning.Light

fun light(block: LightBuilder.() -> Unit) : Light = LightBuilder().apply(block).build()

class LightBuilder {

    var id: String? = null
    var position: Vector? = null
    var ambientIntensity: Intensity? = null
    var intensity: Intensity? = null

    fun build(): Light =
        Light(
            id!!,
            position!!,
            ambientIntensity!!,
            intensity!!
        )
}