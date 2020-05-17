package hr.fer.zemris.graphicsAlgorithms.clipping.polygon

import hr.fer.zemris.math.vector.Vector

interface PolygonClipping {

    fun clip(vertices: List<Vector>) :  List<Vector>
}