package hr.fer.zemris

import hr.fer.zemris.math.util.matrix
import hr.fer.zemris.math.util.row

fun main() {
    val m = matrix(
        row(1, 3, 6, 3),
        row(5, 1, 6, 4),
        row(1, 7, 8, 2),
        row(6, 34, 2, 3)
    )

    println(m.inverse())
    println()
    println()
    println(m.inverse().transpose())
}