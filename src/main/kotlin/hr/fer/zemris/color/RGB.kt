package hr.fer.zemris.color

data class RGB(
    val r: Byte,
    val g: Byte,
    val b: Byte
) {

    companion object {
        const val R_OFFSET = 16
        const val G_OFFSET = 8
        const val B_OFFSET = 0

        private const val FIRST_BYTE_MASK = 0xFF

        val BLACK = RGB(0.toByte(), 0.toByte(), 0.toByte())
        val WHITE = RGB(0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte())
        val RED = RGB(0xFF.toByte(), 0.toByte(), 0.toByte())
        val GREEN = RGB(0.toByte(), 0xFF.toByte(), 0.toByte())
        val BLUE = RGB(0.toByte(), 0.toByte(), 0xFF.toByte())
    }

    fun toColor(): Color {
        return Color(
            ((r.toInt() and FIRST_BYTE_MASK) shl R_OFFSET) or
                    ((g.toInt() and FIRST_BYTE_MASK) shl G_OFFSET) or
                    ((b.toInt() and FIRST_BYTE_MASK) shl B_OFFSET)
        )
    }
}