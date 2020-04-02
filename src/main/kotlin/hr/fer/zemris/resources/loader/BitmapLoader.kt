package hr.fer.zemris.resources.loader

import hr.fer.zemris.resources.bitmap.Bitmap
import java.io.File
import javax.imageio.ImageIO

object BitmapLoader {

    fun load(path: String): Bitmap = ImageIO.read(File(path)).run {
        val pixels = IntArray(width * height)
        getRGB(0, 0, width, height, pixels, 0, width)
        Bitmap(width, height, pixels)
    }
}