package hr.fer.zemris.resources.mesh

import hr.fer.zemris.math.vector.Vector
import hr.fer.zemris.util.IndexedList

open class Mesh(
    val vertices: IndexedList<Vector>,
    val normals: IndexedList<Vector>?
)