package hr.fer.zemris.util

data class UpdatableData<T>(
    private val valueResolver: () -> T
) {

    var update = false

    val value: T
        get() {
            if (update) {
                update = false
                internalValue = valueResolver()
            }

            return internalValue
        }

    private var internalValue = valueResolver()
}