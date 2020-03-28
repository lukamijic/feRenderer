package hr.fer.zemris.renderer.camera

import hr.fer.zemris.math.matrix.Matrix
import hr.fer.zemris.math.util.matrix
import hr.fer.zemris.math.util.row
import hr.fer.zemris.math.util.vector
import hr.fer.zemris.math.vector.Vector
import java.lang.IllegalArgumentException

private const val CAMERA_VECTOR_DIMENSION = 3


class CameraImpl(
    position: Vector = vector(0, 0, 0),
    target: Vector = vector(0, 0, -1),
    up: Vector  = vector(0, 1, 0)
) : Camera {

    override var cameraPosition: Vector = returnIfValidVector(position)
        set(value) {
            shouldRecalculateCameraMatrix = true
            field = returnIfValidVector(value)
        }

    override var cameraTarget: Vector = returnIfValidVector(target)
        set(value) {
            shouldRecalculateCameraMatrix = true
            field = returnIfValidVector(value)
        }
    override var cameraUpVector: Vector = returnIfValidVector(up)
        set(value) {
            shouldRecalculateCameraMatrix = true
            field = returnIfValidVector(value).normalize()
        }


    override val cameraMatrix: Matrix
        get() {
            if (shouldRecalculateCameraMatrix) {
                internalCameraMatrix = calculateCameraMatrix(cameraPosition, cameraTarget, cameraUpVector)
            }

            return internalCameraMatrix
        }

    private var internalCameraMatrix: Matrix = calculateCameraMatrix(cameraPosition, cameraTarget, cameraUpVector)

    private var shouldRecalculateCameraMatrix = false

    /**
     * forward vector -> +z of camera
     * up -> +y
     * right -> +x
     */
    private fun calculateCameraMatrix(cameraPosition: Vector, cameraTarget: Vector, cameraUpVector: Vector): Matrix {
        shouldRecalculateCameraMatrix = false

        val forward = (cameraPosition - cameraTarget).normalize()
        val right = (cameraUpVector.normalize() x forward).normalize()
        val up = forward x right

        return matrix(
            row(right[0], up[0], forward[0], 0),
            row(right[1], up[1], forward[1], 0),
            row(right[2], up[2], forward[2], 0),
            row(-(right * cameraPosition), -(up * cameraPosition), -(forward * cameraPosition), 1)
        )
    }

    private fun returnIfValidVector(v: Vector): Vector =
        if (v.dimension != CAMERA_VECTOR_DIMENSION) {
            throw IllegalArgumentException("Vector must have dimension$CAMERA_VECTOR_DIMENSION, but it was ${v.dimension}.")
        } else {
            v
        }
}