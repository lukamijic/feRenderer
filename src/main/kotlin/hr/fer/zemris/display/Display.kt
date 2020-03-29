package hr.fer.zemris.display

import java.awt.Dimension
import java.awt.Graphics
import java.awt.event.KeyListener
import java.awt.image.BufferedImage
import java.awt.image.DataBufferInt
import javax.swing.JFrame

typealias AwtCanvas = java.awt.Canvas

private const val DEFAULT_FRAME_SIZE = 512
private const val DEFAULT_FRAME_NAME = "feRenderer"
private const val BUFFER_NUMBER = 1

class Display(
    val frameWidth: Int = DEFAULT_FRAME_SIZE,
    val frameHeight: Int = DEFAULT_FRAME_SIZE,
    title: String = DEFAULT_FRAME_NAME
) : AwtCanvas() {

    val canvas = Canvas(frameWidth, frameHeight)

    private val frame: JFrame
    private val drawGraphics: Graphics
    private val displayImage: BufferedImage = BufferedImage(frameWidth, frameHeight, BufferedImage.TYPE_INT_RGB)
    private val frameBuffer: IntArray =
        (displayImage.raster.dataBuffer as DataBufferInt).data

    init {
        val size = Dimension(frameWidth, frameHeight)

        preferredSize = size
        minimumSize = size
        maximumSize = size

        frame = JFrame(title).apply {
            add(this@Display)
            pack()
            isResizable = false
            defaultCloseOperation = JFrame.EXIT_ON_CLOSE
            setLocationRelativeTo(null)
            isVisible = true
        }

        createBufferStrategy(BUFFER_NUMBER)
        drawGraphics = bufferStrategy.drawGraphics
    }

    fun swapBuffers() {
        canvas.fillRGBIntArray(frameBuffer)
        drawGraphics.drawImage(displayImage, 0, 0, canvas.width, canvas.height, null)
        bufferStrategy.show()
    }
}