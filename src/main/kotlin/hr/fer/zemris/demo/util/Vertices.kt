package hr.fer.zemris.demo.util

import hr.fer.zemris.math.util.vector

val cubeVertices = listOf(
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

val pyramidVertices = listOf(
    vector(-1, -1, -1, 1), vector(-1, -1, 1, 1), vector(1, -1, -1, 1),
    vector(-1, -1, 1, 1), vector(1, -1, 1, 1), vector(1, -1, -1, 1),
    vector(-1, -1, -1, 1), vector(0, 1, 0, 1), vector(1, -1, -1, 1),
    vector(1, -1, -1, 1), vector(0, 1, 0, 1), vector(1, -1, 1, 1),
    vector(-1, -1, 1, 1), vector(0, 1, 0, 1), vector(1, -1, 1, 1),
    vector(-1, -1, -1, 1), vector(0, 1, 0, 1), vector(-1, -1, 1, 1)
)

val diamondVertices = listOf(
    vector(-1, 0, 0, 1), vector(-1, 0, 1, 1), vector(1, 0, -1, 1),
    vector(-1, 0, 1, 1), vector(1, 0, 1, 1), vector(1, 0, -1, 1),
    vector(-1, 0, -1, 1), vector(0, 1, 0, 1), vector(1, 0, -1, 1),
    vector(1, 0, -1, 1), vector(0, 1, 0, 1), vector(1, 0, 1, 1),
    vector(-1, 0, 1, 1), vector(0, 1, 0, 1), vector(1, 0, 1, 1),
    vector(-1, 0, -1, 1), vector(0, 1, 0, 1), vector(-1, 0, 1, 1),

    vector(-1, 0, 0, 1), vector(-1, 0, 1, 1), vector(1, 0, -1, 1),
    vector(-1, 0, 1, 1), vector(1, 0, 1, 1), vector(1, 0, -1, 1),
    vector(-1, 0, -1, 1), vector(0, -1, 0, 1), vector(1, 0, -1, 1),
    vector(1, 0, -1, 1), vector(0, -1, 0, 1), vector(1, 0, 1, 1),
    vector(-1, 0, 1, 1), vector(0, -1, 0, 1), vector(1, 0, 1, 1),
    vector(-1, 0, -1, 1), vector(0, -1, 0, 1), vector(-1, 0, 1, 1)
)