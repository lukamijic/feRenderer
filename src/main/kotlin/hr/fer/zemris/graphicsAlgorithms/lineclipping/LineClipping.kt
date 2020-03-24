package hr.fer.zemris.graphicsAlgorithms.lineclipping

import hr.fer.zemris.geometry.model.Point

interface LineClipping {

    fun clip(p1: Point, p2: Point) : Pair<Point, Point>?
}