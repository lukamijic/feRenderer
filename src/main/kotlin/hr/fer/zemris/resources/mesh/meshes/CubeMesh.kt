package hr.fer.zemris.resources.mesh.meshes

import hr.fer.zemris.math.util.vector
import hr.fer.zemris.resources.mesh.Mesh

private val cubeVertices = listOf(
    vector(0, 0, 0, 1), vector(0, 1, 0, 1), vector(1, 1, 0, 1),
    vector(0, 0, 0, 1), vector(1, 1, 0, 1), vector(1, 0, 0, 1),

    vector(1, 0, 0, 1), vector(1, 1, 0, 1), vector(1, 1, 1, 1),
    vector(1, 0, 0, 1), vector(1, 1, 1, 1), vector(1, 0, 1, 1),

    vector(1, 0, 1, 1), vector(1, 1, 1, 1), vector(0, 1, 1, 1),
    vector(1, 0, 1, 1), vector(0, 1, 1, 1), vector(0, 0, 1, 1),

    vector(0, 0, 1, 1), vector(0, 1, 1, 1), vector(0, 1, 0, 1),
    vector(0, 0, 1, 1), vector(0, 1, 0, 1), vector(0, 0, 0, 1),

    vector(0, 1, 0, 1), vector(0, 1, 1, 1), vector(1, 1, 1, 1),
    vector(0, 1, 0, 1), vector(1, 1, 1, 1), vector(1, 1, 0, 1),

    vector(1, 0, 1, 1), vector(0, 0, 1, 1), vector(0, 0, 0, 1),
    vector(1, 0, 1, 1), vector(0, 0, 0, 1), vector(1, 0, 0, 1)
)

class CubeMesh: Mesh(cubeVertices)