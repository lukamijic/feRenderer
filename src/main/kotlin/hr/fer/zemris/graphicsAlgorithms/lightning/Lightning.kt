package hr.fer.zemris.graphicsAlgorithms.lightning

import kotlin.math.max
import kotlin.math.min

object Lightning {

    fun applyLight(colorComponent: Byte, intensity: Double) : Byte =
        min(255.0, max((colorComponent.toInt().let { b -> if (b < 0 ) b + 255 else b}.toDouble() * intensity), 0.0) / 255.0).toByte()
}