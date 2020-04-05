package hr.fer.zemris.graphicsAlgorithms.culling

import hr.fer.zemris.math.vector.Vector

object Culling {

    /**
     * Returns true if triangle should be drawn
     */
    fun cull(normal: Vector, cameraRay: Vector): Boolean = (normal * cameraRay) > 0
}