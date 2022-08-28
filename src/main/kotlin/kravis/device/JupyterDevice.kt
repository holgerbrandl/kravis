package kravis.device

import kravis.Aesthetic
import kravis.GGPlot
import kravis.SessionPrefs
import kravis.geomPoint
import kravis.nshelper.plot
import kravis.render.PlotFormat
import org.jetbrains.kotlinx.dataframe.datasets.irisData
import org.jetbrains.kotlinx.jupyter.api.MimeTypedResult
import java.awt.Dimension
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.file.Path
import java.util.*
import javax.imageio.ImageIO
import kotlin.io.path.createTempFile
import kotlin.io.path.exists

/**
 * @author Holger Brandl
 */
class JupyterDevice(val renderSVG: Boolean = false, val size: Dimension? = Dimension(1400, 600) ) : OutputDevice() {

    override fun getPreferredFormat() = if (renderSVG) PlotFormat.SVG else PlotFormat.PNG

    override fun show(plot: GGPlot): MimeTypedResult {
        val imageFile = plot.save(createTempFile(suffix = getPreferredFormat().toString()), getPreferredSize())
        require(imageFile.exists()) { "Visualization Failed. Could not render image." }

        return if (renderSVG) renderAsSVG(imageFile) else renderAsImage(imageFile)
    }

    override fun getPreferredSize(): Dimension? = size

    internal fun renderAsSVG(imageFile: Path): MimeTypedResult {
        TODO()
    }

    internal fun renderAsImage(imageFile: Path): MimeTypedResult {
        // testing
        // val imageFile = File("/Users/brandl/Downloads/Clipboard.png")

        val imageF = ImageIO.read(imageFile.toFile())

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

//abstract class ScriptTemplateWithDisplayHelpers(val args: kotlin.Array<kotlin.String>) {
fun resultOf(vararg mimeToData: Pair<String, String>): MimeTypedResult = MimeTypedResult(mapOf(*mimeToData))
//}

val testPlot by lazy {
    irisData
        .plot("Petal.Length" to Aesthetic.x, "Petal.Width" to Aesthetic.y)
        .geomPoint(alpha = .4)
}

fun main() {
    SessionPrefs.OUTPUT_DEVICE = JupyterDevice()
    SessionPrefs.SHOW_TO_STRING = true

    val result = testPlot.show()
    println(result)
}

//fun resultOf(vararg mimeToData: Pair<String, Any>): MimeTypedResult = MimeTypedResult(mapOf(*mimeToData))


//fun mimeResult(vararg mimeToData: Pair<String, Any>): MimeTypedResult = MimeTypedResult(mapOf(*mimeToData))
//fun textResult(text: String): Map<String, Any> = MimeTypedResult(mapOf("text/plain" to text))

//class MimeTypedResult(mimeData: Map<String, Any>): Map<String, Any> by mimeData

