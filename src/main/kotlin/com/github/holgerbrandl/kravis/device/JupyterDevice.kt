package com.github.holgerbrandl.kravis.device

import com.github.holgerbrandl.kravis.Aesthetic
import com.github.holgerbrandl.kravis.GGPlot
import com.github.holgerbrandl.kravis.OUTPUT_DEVICE
import com.github.holgerbrandl.kravis.geomPoint
import com.github.holgerbrandl.kravis.nshelper.ggplot
import jupyter.kotlin.MimeTypedResult
import jupyter.kotlin.resultOf
import krangl.irisData
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*
import javax.imageio.ImageIO

/**
 * @author Holger Brandl
 */
class JupyterDevice : OutputDevice() {
    override fun show(script: GGPlot): MimeTypedResult {

        val pathToFile = File("/Users/brandl/Downloads/Clipboard.png")

        val imageF = ImageIO.read(pathToFile)

        // Draw the image on to the buffered image
        val bimage = BufferedImage(imageF.getWidth(null), imageF.getHeight(null), BufferedImage.TYPE_INT_RGB)
        val bGr = bimage.createGraphics()
        bGr.drawImage(imageF, 0, 0, null)
        bGr.dispose()

        val buf = ByteArrayOutputStream()

        val writer = ImageIO.getImageWritersByMIMEType("image/jpeg").next()

        val ios = ImageIO.createImageOutputStream(buf)
        writer.output = ios
        writer.write(bimage)
        ios.close()
        val b64img = Base64.getEncoder().encodeToString(buf.toByteArray())
        return resultOf("image/jpeg" to b64img)
    }
}

val testPlot by lazy {
    irisData
        .ggplot("Petal.Length" to Aesthetic.x, "Petal.Width" to Aesthetic.y)
        .geomPoint(alpha = .4)
}

fun main(args: Array<String>) {
    OUTPUT_DEVICE = JupyterDevice()

    println(testPlot.show())
}

//fun resultOf(vararg mimeToData: Pair<String, Any>): MimeTypedResult = MimeTypedResult(mapOf(*mimeToData))


//fun mimeResult(vararg mimeToData: Pair<String, Any>): MimeTypedResult = MimeTypedResult(mapOf(*mimeToData))
//fun textResult(text: String): Map<String, Any> = MimeTypedResult(mapOf("text/plain" to text))

//class MimeTypedResult(mimeData: Map<String, Any>): Map<String, Any> by mimeData

