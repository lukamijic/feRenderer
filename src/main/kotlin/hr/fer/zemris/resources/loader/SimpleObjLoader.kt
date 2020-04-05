package hr.fer.zemris.resources.loader

import hr.fer.zemris.math.vector.Vector
import hr.fer.zemris.resources.loader.exceptions.UnsupportedFormatMode
import hr.fer.zemris.resources.mesh.Mesh
import hr.fer.zemris.util.IndexedList
import java.io.BufferedReader
import java.io.FileReader

/**
 * This simple obj loader loads only vertices and indices
 */
object SimpleObjLoader {

    fun load(path: String) : Mesh {
        val vertices = mutableListOf<Vector>()
        val indices = mutableListOf<Int>()
        BufferedReader(FileReader(path)).useLines { lines ->
            lines.map(String::trim)
                .filter { !it.startsWith(COMMENT_SIGN) }
                .forEach {
                    when {
                        it.startsWith(VERTEX_SIGN) -> { vertices.add(parseVertex(it.substring(VERTEX_SIGN.length).trim())) }
                        it.startsWith(POLYGON_SIGN) -> { indices.addAll(parsePolygonIndices(it.substring(POLYGON_SIGN.length).trim()))}
                        else -> UnsupportedFormatMode("SimpleObjLoader supports only vertex loading and polygon indices loading")
                    }
                }
        }

        return Mesh(IndexedList(vertices, indices), null)
    }
}