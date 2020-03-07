package hr.fer.zemris.color

import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class ColorTest {

    @Test
    fun `should return same rgb for chain rgb then color then rgb`() {
        val rgb = RGB(127.toByte(), 127.toByte(), 127.toByte())
        assertEquals(rgb, rgb.toColor().toRGB())
    }

    @Test
    fun `should return same color for chain color then rgb then color`() {
        val color = Color(0xFFFFFF)

        assertEquals(color.value, color.toRGB().toColor().value)
    }
}