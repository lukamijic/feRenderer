package hr.fer.zemris.resources.loader

import hr.fer.zemris.math.util.vector
import hr.fer.zemris.math.vector.Vector
import hr.fer.zemris.resources.loader.exceptions.InvalidLoadingFormat
import hr.fer.zemris.util.WHITESPACE_REGEX

const val COMMENT_SIGN = "#"
const val VERTEX_SIGN = "v"
const val POLYGON_SIGN = "f"

fun parseVertex(line: String): Vector {
    val vertexValues = line.split(WHITESPACE_REGEX).map(String::toFloat)
    return when (vertexValues.size) {
        3 -> vector(vertexValues[0], vertexValues[1], vertexValues[2], 1f)
        4 -> vector(vertexValues[0], vertexValues[1], vertexValues[2], vertexValues[3])
        else -> throw InvalidLoadingFormat("Vertex line must have 3 or 4 values, it had $line -> ${vertexValues.size}")
    }
}

fun parsePolygonIndices(line: String): List<Int> {
    val indicesValues = line.split(WHITESPACE_REGEX).map(String::toInt)
    return when(indicesValues.size) {
        3 -> listOf(indicesValues[0] - 1, indicesValues[1] - 1, indicesValues[2] - 1)
        else -> throw InvalidLoadingFormat("Simple polygon line must have three int values, $line -> ${indicesValues.size}")
    }
}