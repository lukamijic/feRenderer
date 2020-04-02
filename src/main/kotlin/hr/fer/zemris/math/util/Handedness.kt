package hr.fer.zemris.math.util

import hr.fer.zemris.geometry.model.Point

enum class Handedness {
    LEFT, RIGHT;

    companion object {
        /**
         * 2D cross product isn't defined. It is assumed that z is 0.
         * While calculating with z component as 0 factors next to
         * i and j will be 0, so value next to z will be returned as
         * scalar.
         *
         * This is used in calculating handiness of triangle and
         * only the sign of the scalar is actually important
         */
        fun calculateHandiness(v1: Point, v2: Point, v3: Point): Handedness = v1.run {
            val x1 = v2.x - x
            val y1 = v2.y - y

            val x2 = v3.x - x
            val y2 = v3.y - y

            return if ((x1 * y2 - x2 * y1) < 0) {
                LEFT
            } else {
                RIGHT
            }
        }
    }
}
