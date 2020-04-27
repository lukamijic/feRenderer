package hr.fer.zemris.renderer.lightning

private const val DEFAULT_DISTANCE_FACTOR = 0.5

data class LightCoefs(
    val ambientR: Double,
    val ambientG: Double,
    val ambientB: Double,
    val diffuseR: Double,
    val diffuseG: Double,
    val diffuseB: Double,
    val mirror: Double,
    val mirrorN: Double,
    val distanceFactor: Double = DEFAULT_DISTANCE_FACTOR
) {

    constructor(ambient: Double, diffuse: Double, mirror: Double, mirrorN: Double, distanceFactor: Double = DEFAULT_DISTANCE_FACTOR):
            this(ambient, ambient, ambient, diffuse, diffuse, diffuse, mirror, mirrorN, distanceFactor)
}