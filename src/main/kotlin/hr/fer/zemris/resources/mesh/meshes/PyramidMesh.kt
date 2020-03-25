package hr.fer.zemris.resources.mesh.meshes

import hr.fer.zemris.math.util.vector
import hr.fer.zemris.resources.mesh.Mesh

private val pyramidVertices = listOf(
    vector(-1, -1, -1, 1), vector(-1, -1, 1, 1), vector(1, -1, -1, 1),
    vector(-1, -1, 1, 1), vector(1, -1, 1, 1), vector(1, -1, -1, 1),
    vector(-1, -1, -1, 1), vector(0, 1, 0, 1), vector(1, -1, -1, 1),
    vector(1, -1, -1, 1), vector(0, 1, 0, 1), vector(1, -1, 1, 1),
    vector(-1, -1, 1, 1), vector(0, 1, 0, 1), vector(1, -1, 1, 1),
    vector(-1, -1, -1, 1), vector(0, 1, 0, 1), vector(-1, -1, 1, 1)
)

class PyramidMesh : Mesh(pyramidVertices)