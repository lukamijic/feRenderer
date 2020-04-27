package hr.fer.zemris.renderer.lightning

import hr.fer.zemris.math.vector.Vector

data class Light(
    var id: String,
    var position: Vector,
    var ambientIntensity: Intensity,
    var intensity: Intensity
)
