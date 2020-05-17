package hr.fer.zemris.graphicsAlgorithms.clipping.line

import hr.fer.zemris.geometry.model.Point2i

interface LineClipping {

    fun clip(p1: Point2i, p2: Point2i) : Pair<Point2i, Point2i>?
}