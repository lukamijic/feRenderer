package hr.fer.zemris.resources.loader

import hr.fer.zemris.math.util.vector
import hr.fer.zemris.math.vector.Vector
import hr.fer.zemris.resources.loader.exceptions.InvalidLoadingFormat
import hr.fer.zemris.resources.loader.exceptions.UnsupportedFormatMode
import hr.fer.zemris.resources.mesh.Mesh
import hr.fer.zemris.util.IndexedList
import hr.fer.zemris.util.WHITESPACE_REGEX
import java.io.BufferedReader
import java.io.FileReader
import java.lang.Exception

private const val COMMENT_SIGN = "#"
private const val VERTEX_SIGN = "v"
private const val TEXTURE_SIGN = "vt"
private const val NORMAL_SIGN = "vn"
private const val POLYGON_SIGN = "f"


private val VERTEX_VALUES_REGEX = "[0-9]+\\s+[0-9]+\\s+[0-9]+".toRegex()
private val VERTEX_AND_TEXTURE_VALUES_REGEX = "[0-9]+/[0-9]+\\s+[0-9]+/[0-9]+\\s+[0-9]+/[0-9]+".toRegex()
private val VERTEX_AND_NORMAL_VALUES_REGEX = "[0-9]+//[0-9]+\\s+[0-9]+//[0-9]+\\s+[0-9]+//[0-9]+".toRegex()
private val VERTEX_AND_TEXTURE_AND_NORMAL_VALUES_REGEX = "[0-9]+/[0-9]+/[0-9]+\\s+[0-9]+/[0-9]+/[0-9]+\\s+[0-9]+/[0-9]+/[0-9]+".toRegex()

private val INDEX_REGEX = "\\d+".toRegex()

object ObjLoader {

    fun load(path: String): Mesh {
        val vertices = mutableListOf<Vector>()
        val verticesIndices = mutableListOf<Int>()

        val texels = mutableListOf<Vector>()
        val texelsIndices = mutableListOf<Int>()

        val normals = mutableListOf<Vector>()
        val normalIndices = mutableListOf<Int>()

        BufferedReader(FileReader(path)).useLines { lines ->
            lines.map(String::trim)
                .filter { !it.startsWith(COMMENT_SIGN) }
                .forEach {
                    when {
                        it.startsWith(TEXTURE_SIGN) -> texels.add(parseTexture(it.substring(TEXTURE_SIGN.length).trim()))
                        it.startsWith(NORMAL_SIGN) -> normals.add(parseNormal(it.substring(NORMAL_SIGN.length).trim()))
                        it.startsWith(POLYGON_SIGN) -> parsePolygon(it.substring(POLYGON_SIGN.length).trim()).let { indices ->
                            verticesIndices.addAll(indices.vertexIndices)
                            indices.texelIndices?.let { texelIndicesToAdd -> texelsIndices.addAll(texelIndicesToAdd) }
                            indices.normalIndices?.let { normalIndicesToAdd -> normalIndices.addAll(normalIndicesToAdd) }
                        }
                        it.startsWith(VERTEX_SIGN) -> vertices.add(parseVertex(it.substring(VERTEX_SIGN.length).trim()))
                        else -> UnsupportedFormatMode("Invalid line $it")
                    }
                }
        }

        return Mesh(
            IndexedList(vertices, verticesIndices),
            if (texels.isEmpty()) null else IndexedList(texels, texelsIndices),
            if (normals.isEmpty()) null else IndexedList(normals, normalIndices)
        )
    }

    private fun parseVertex(line: String): Vector {
        val vertexValues = line.split(WHITESPACE_REGEX).map(String::toFloat)
        return when (vertexValues.size) {
            3 -> vector(vertexValues[0], vertexValues[1], vertexValues[2], 1f)
            4 -> vector(vertexValues[0], vertexValues[1], vertexValues[2], vertexValues[3])
            else -> throw InvalidLoadingFormat("Vertex line must have 3 or 4 values, it had $line -> ${vertexValues.size}")
        }
    }

    private fun parseTexture(line: String) : Vector {
        val textureValues = line.split(WHITESPACE_REGEX).map(String::toFloat)
        return when (textureValues.size) {
            1 -> vector(textureValues[0], 0, 0)
            2 -> vector(textureValues[0], textureValues[1], 0)
            3 -> vector(textureValues[0], textureValues[1], textureValues[1])
            else ->  throw InvalidLoadingFormat("Texture line must have 1 to 3 values, it had $line -> ${textureValues.size}")
        }
    }

    private fun parseNormal(line: String) : Vector {
        val normalValues = line.split(WHITESPACE_REGEX).map(String::toFloat)
        return when(normalValues.size) {
            3 -> vector(normalValues[0], normalValues[1], normalValues[2])
            else -> throw InvalidLoadingFormat("Normals must have 3 values, it had $line -> ${normalValues.size}")
        }
    }

    private fun parsePolygon(line: String) : Indices = when {
        line.matches(VERTEX_VALUES_REGEX) -> parsePolygonVertexValues(line)
        line.matches(VERTEX_AND_TEXTURE_VALUES_REGEX) -> parsePolygonVertexAndTexelsValues(line)
        line.matches(VERTEX_AND_NORMAL_VALUES_REGEX) -> parsePolygonVertexAndNormalsValues(line)
        line.matches(VERTEX_AND_TEXTURE_AND_NORMAL_VALUES_REGEX) -> parsePolygonVertexAndTexelsAndNormalsValues(line)
        else -> throw InvalidLoadingFormat("$line is not valid obj polygon value")
    }


    private fun parsePolygonVertexValues(line: String): Indices {
        try {
            return Indices(INDEX_REGEX.findAll(line).map(MatchResult::value).map(String::toInt).map(Int::dec).toList())
        } catch (e : Exception) {
            throw InvalidLoadingFormat("Expected all tokens to have pattern of \"d  d  d\", it was $line")
        }
    }

    private fun parsePolygonVertexAndTexelsValues(line: String) : Indices {
        try {
            return INDEX_REGEX.findAll(line).map(MatchResult::value).map(String::toInt).map(Int::dec).toList().run {
                Indices(
                    this.filterIndexed { index, _ -> index % 2 == 0},
                    this.filterIndexed { index, _ -> index % 2 == 1}
                )
            }
        } catch (e : Exception) {
            throw InvalidLoadingFormat("Expected all tokens to have pattern of \"d/d  d/d  d/d\", it was $line")
        }
    }

    private fun parsePolygonVertexAndNormalsValues(line: String) : Indices {
        try {
            return INDEX_REGEX.findAll(line).map(MatchResult::value).map(String::toInt).map(Int::dec).toList().run {
                Indices(
                    this.filterIndexed { index, _ -> index % 2 == 0},
                    null,
                    this.filterIndexed { index, _ -> index % 2 == 1}
                )
            }
        } catch (e : Exception) {
            throw InvalidLoadingFormat("Expected all tokens to have pattern of \"d//d  d//d  d//d\", it was $line")
        }
    }

    private fun parsePolygonVertexAndTexelsAndNormalsValues(line: String) : Indices {
        try {
            return INDEX_REGEX.findAll(line).map(MatchResult::value).map(String::toInt).map(Int::dec).toList().run {
                Indices(
                    this.filterIndexed { index, _ -> index % 3 == 0},
                    this.filterIndexed { index, _ -> index % 3 == 1},
                    this.filterIndexed { index, _ -> index % 3 == 2}
                )
            }
        } catch (e : Exception) {
            throw InvalidLoadingFormat("Expected all tokens to have pattern of \"d/d/d  d/d/d  d/d/d\", it was $line")
        }
    }

    private data class Indices(
        val vertexIndices: List<Int>,
        val texelIndices: List<Int>? = null,
        val normalIndices: List<Int>? = null
    )
}