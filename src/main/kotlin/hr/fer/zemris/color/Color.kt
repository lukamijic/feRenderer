package hr.fer.zemris.color

/**
 * Int stores RGB
 */
class Color (
    val value: Int
) {

    companion object {

        val BLACK = RGB.BLACK.toColor()
        val WHITE = RGB.WHITE.toColor()

        val RED = RGB.RED.toColor()
        val GREEN = RGB.GREEN.toColor()
        val BLUE = RGB.BLUE.toColor()

        val MAGENTA = RGB.MAGENTA.toColor()
        val YELLOW = RGB.YELLOW.toColor()
        val CYAN = RGB.CYAN.toColor()
    }

    fun toRGB(): RGB =
        RGB(
            (value shr RGB.R_OFFSET).toByte(),
            (value shr RGB.G_OFFSET).toByte(),
            (value shr RGB.B_OFFSET).toByte()
        )
}