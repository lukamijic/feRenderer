package hr.fer.zemris.renderer.camera

import hr.fer.zemris.math.matrix.Matrix
import hr.fer.zemris.math.util.matrix
import hr.fer.zemris.math.util.row
import hr.fer.zemris.math.util.vector
import hr.fer.zemris.math.vector.Vector
import hr.fer.zemris.util.UpdatableData
import java.lang.IllegalArgumentException

private const val CAMERA_VECTOR_DIMENSION = 3


class CameraImpl(
    position: Vector = vector(0, 0, 0),
    target: Vector = vector(0, 0, -1),
    viewUp: Vector = vector(0, 1, 0)
) : Camera {

    override var cameraPosition: Vector = returnIfValidVector(position)
        set(value) {
            setUpdateFields()
            field = returnIfValidVector(value)
        }

    override var cameraTarget: Vector = returnIfValidVector(target)
        set(value) {
            setUpdateFields()
            field = returnIfValidVector(value)
        }
    override var cameraUpVector: Vector = returnIfValidVector(viewUp)
        set(value) {
            setUpdateFields()
            field = returnIfValidVector(value).normalize()
        }

    override val forward: Vector
        get() = internalForwardVector.value
    override val right: Vector
        get() = internalRightVector.value
    override val up: Vector
        get() = internalUpVector.value

    override val cameraMatrix: Matrix
        get() = internalCameraMatrix.value

    private var internalForwardVector = UpdatableData { (cameraPosition - cameraTarget).normalize() }
    private var internalRightVector = UpdatableData { (cameraUpVector.normalize() x forward).normalize() }
    private var internalUpVector = UpdatableData { forward x right }

    private var internalCameraMatrix =
        UpdatableData { calculateCameraMatrix(forward, right, up) }

    /**
     * forward vector -> +z of camera
     * up -> +y
     * right -> +x
     */
    private fun calculateCameraMatrix(forward: Vector, right: Vector, up: Vector): Matrix =
        matrix(
            row(right[0], up[0], forward[0], 0),
            row(right[1], up[1], forward[1], 0),
            row(right[2], up[2], forward[2], 0),
            row(-(right * cameraPosition), -(up * cameraPosition), -(forward * cameraPosition), 1)
        )

    private fun setUpdateFields() {
        internalCameraMatrix.update = true
        internalForwardVector.update = true
        internalRightVector.update = true
        internalUpVector.update = true
    }

    private fun returnIfValidVector(v: Vector): Vector =
        if (v.dimension != CAMERA_VECTOR_DIMENSION) {
            throw IllegalArgumentException("Vector must have dimension$CAMERA_VECTOR_DIMENSION, but it was ${v.dimension}.")
        } else {
            v
        }
}