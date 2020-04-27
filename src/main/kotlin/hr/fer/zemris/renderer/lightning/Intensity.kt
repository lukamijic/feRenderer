package hr.fer.zemris.renderer.lightning

data class Intensity(
    var r: Double,
    var g: Double,
    var b: Double
) {

    constructor(intensity: Double) : this(intensity, intensity, intensity)
}