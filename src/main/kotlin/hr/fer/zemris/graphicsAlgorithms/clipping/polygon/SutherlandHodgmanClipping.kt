package hr.fer.zemris.graphicsAlgorithms.clipping.polygon

import hr.fer.zemris.math.extensions.times
import hr.fer.zemris.math.vector.Vector

object SutherlandHodgmanClipping : PolygonClipping {

    override fun clip(vertices: List<Vector>): List<Vector> = vertices.toMutableList().apply {
        val auxillaryList = mutableListOf<Vector>()
        if (clipPolygonAxis(this, auxillaryList, { vector -> vector[0] }, ClipAxis.X) &&
            clipPolygonAxis(this, auxillaryList, { vector -> vector[1] }, ClipAxis.Y) &&
            clipPolygonAxis(this, auxillaryList, { vector -> vector[2] }, ClipAxis.Z)
        ) {}
    }

    private fun clipPolygonAxis(
        vertices: MutableList<Vector>,
        auxillaryList: MutableList<Vector>,
        componentGetter: (Vector) -> Double,
        axis: ClipAxis
    ): Boolean {
        val boundGetters = when (axis) {
            ClipAxis.X, ClipAxis.Y -> arrayOf({ vector: Vector -> vector[3] }, { vector: Vector -> vector[3] })
            ClipAxis.Z -> arrayOf({ vector: Vector -> vector[3] }, { vector: Vector -> vector[3] })
        }
        clipPolygonComponent(vertices, componentGetter, 1.0, boundGetters[0], auxillaryList)
        vertices.clear()

        if (auxillaryList.isEmpty()) {
            return false
        }

        clipPolygonComponent(auxillaryList, componentGetter, -1.0, boundGetters[1], vertices)
        auxillaryList.clear()

        return vertices.isNotEmpty()
    }

    private fun clipPolygonComponent(
        vertices: List<Vector>,
        componentGetter: (Vector) -> Double,
        componentFactor: Double,
        boundGetter: (Vector) -> Double,
        result: MutableList<Vector>
    ) {
        var prevVertex = vertices.last()
        var prevComponent = componentGetter(prevVertex) * componentFactor
        var prevInside = prevComponent <= boundGetter(prevVertex)

        for (currentVertex in vertices) {
            val currentComponent = componentGetter(currentVertex) * componentFactor
            val currentInside = currentComponent <= boundGetter(currentVertex)

            if (currentInside xor prevInside) {
                val lerpFactor =
                    (prevVertex[3] - prevComponent) / ((prevVertex[3] - prevComponent) - (currentVertex[3] - currentComponent))
                result.add(prevVertex.lerp(currentVertex, lerpFactor))
            }

            if (currentInside) {
                result.add(currentVertex)
            }

            prevVertex = currentVertex
            prevComponent = currentComponent
            prevInside = currentInside
        }
    }

    private fun Vector.lerp(other: Vector, lerpFactor: Double) = (lerpFactor * (other - this)) + this
}

private enum class ClipAxis {
    X, Y, Z
}