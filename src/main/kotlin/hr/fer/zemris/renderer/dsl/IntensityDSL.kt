package hr.fer.zemris.renderer.dsl

import hr.fer.zemris.renderer.lightning.Intensity

fun intensity(block: IntensityBuilder.() -> Unit) : Intensity = IntensityBuilder().apply(block).build()

class IntensityBuilder {

    var r : Double? = null
    var g : Double? = null
    var b : Double? = null

    var intensity: Double? = null

    fun build(): Intensity =
        if (r == null && g == null && b == null) {
            Intensity(intensity!!)
        } else {
            Intensity(r!!, g!!, b!!)
        }

}