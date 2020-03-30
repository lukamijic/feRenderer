package hr.fer.zemris.util

class IndexedList<T>(
    private val values: List<T>,
    private val indices: List<Int>
) : List<T> {

    override val size: Int
        get() = indices.size

    override fun contains(element: T): Boolean = values.contains(element)

    override fun containsAll(elements: Collection<T>): Boolean = values.containsAll(elements)

    override fun get(index: Int): T = values[indices[index]]

    override fun indexOf(element: T): Int {
        val valuesIndexOf = values.indexOf(element)
        return indices.firstOrNull { it == valuesIndexOf } ?: -1
    }

    override fun lastIndexOf(element: T): Int {
        val valuesIndexOf = values.indexOf(element)
        return indices.lastOrNull { it == valuesIndexOf } ?: -1
    }

    override fun isEmpty(): Boolean = values.isEmpty()

    override fun iterator(): Iterator<T> = object : Iterator<T> {
        val indicesIterator = indices.iterator()

        override fun hasNext(): Boolean = indicesIterator.hasNext()

        override fun next(): T = values[indicesIterator.next()]
    }

    override fun listIterator(): ListIterator<T> = object: ListIterator<T> {
        val indicesIterator = indices.listIterator()

        override fun hasNext(): Boolean = indicesIterator.hasNext()

        override fun hasPrevious(): Boolean = indicesIterator.hasPrevious()

        override fun next(): T = values[indicesIterator.next()]

        override fun nextIndex(): Int = indicesIterator.nextIndex()

        override fun previous(): T = values[indicesIterator.previous()]

        override fun previousIndex(): Int = indicesIterator.previousIndex()
    }

    override fun listIterator(index: Int): ListIterator<T> = object: ListIterator<T> {
        val indicesIterator = indices.listIterator(index)

        override fun hasNext(): Boolean = indicesIterator.hasNext()

        override fun hasPrevious(): Boolean = indicesIterator.hasPrevious()

        override fun next(): T = values[indicesIterator.next()]

        override fun nextIndex(): Int = indicesIterator.nextIndex()

        override fun previous(): T = values[indicesIterator.previous()]

        override fun previousIndex(): Int = indicesIterator.previousIndex()
    }

    override fun subList(fromIndex: Int, toIndex: Int): List<T> =
        indices.subList(fromIndex, toIndex).map { values[it] }
}