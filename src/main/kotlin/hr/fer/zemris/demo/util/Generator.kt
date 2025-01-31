package hr.fer.zemris.demo.util

import hr.fer.zemris.color.Color
import hr.fer.zemris.geometry.model.Point
import hr.fer.zemris.geometry.model.Point2i
import kotlin.random.Random
import kotlin.random.nextInt

private val random = Random(System.currentTimeMillis())

private val colors = listOf(
    Color.WHITE,
    Color.RED,
    Color.GREEN,
    Color.BLUE,
    Color.MAGENTA,
    Color.YELLOW,
    Color.CYAN
)

fun randomPoint2i(range: IntRange): Point2i =
    Point2i(
        random.nextInt(range),
        random.nextInt(range)
    )

fun randomPoint(range: IntRange): Point =
    Point(
        random.nextInt(range),
        random.nextInt(range)
    )

fun randomColor(): Color =
    colors[random.nextInt(colors.size - 1)]

fun randomInt(range: IntRange) = random.nextInt(range)