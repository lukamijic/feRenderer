package hr.fer.zemris.util

data class IndexedList<T>(
    val values: List<T>,
    val indices: List<Int>
)