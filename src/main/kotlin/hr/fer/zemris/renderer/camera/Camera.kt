package hr.fer.zemris.renderer.camera

import hr.fer.zemris.math.matrix.Matrix
import hr.fer.zemris.math.vector.Vector

interface Camera {

    var cameraPosition: Vector
    var cameraTarget: Vector
    var cameraUpVector: Vector

    val cameraMatrix: Matrix
}