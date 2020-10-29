package kravis.render

import krangl.irisData
import kravis.GGPlot
import kravis.SessionPrefs
import kravis.demo.irisScatter
import org.rosuda.REngine.Rserve.RConnection
import java.awt.Desktop
import java.awt.Dimension
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.File
import java.io.IOException
import java.nio.charset.StandardCharsets
import javax.imageio.ImageIO

/**
 * A rendering engine that uses a remote Rserve instance to build the plot. See https://www.rforge.net/Rserve/
 * @author Holger Brandl
 */
class RserveEngine(val host: String = "localhost", val port: Int = 6311) : RenderEngine() {

    override fun render(plot: GGPlot, outputFile: File, preferredSize: Dimension?): File {

        val connection = RConnection(host, port)

        plot.dataRegistry.forEach { (varName, df) ->
            connection.setTable(varName, df)
        }

        val plotFormat = PlotFormat.valueOf(outputFile.extension.toUpperCase())
        val rScript = compileScript(plot, preferredSize, plotFormat)
//        val rScript = """plot(1:10)"""

        val resultData = connection.evalAndFetchResult(rScript)


        if (plotFormat == PlotFormat.SVG) {
            outputFile.writeText(String(resultData))

        } else {
            val img: BufferedImage? = try {
                ImageIO.read(ByteArrayInputStream(resultData))
            } catch (e: IOException) {
                throw RServeExceptionException(e.message ?: "")
            }

            // todo reenable
//        if (result.exitCode != 0) {
//            throw LocalRenderingFailedException(rScript, result)
//        }

            ImageIO.write(img, outputFile.extension, outputFile)
        }


        require(outputFile.exists()) { System.err.println("Image generation failed") }

        return outputFile
    }


    fun compileScript(plot: GGPlot, preferredSize: Dimension?, plotFormat: PlotFormat): String {
        val final = plot.spec

        val preamble = plot.preambble.joinToString("\n")

        val optionalSizeConfig = preferredSize.adjustSize()

        val rScript = """
library(ggplot2)
library(dplyr)
library(readr)
library(scales)
library(forcats)

$preamble


set.seed(2009)

gg = $final

plotFile = tempfile(fileext='.${plotFormat}')
ggsave(filename=plotFile, plot=gg${optionalSizeConfig ?: ""})

                """.trimIndent()


        return rScript
    }
}


internal fun RConnection.readImage(script: String): BufferedImage? {

    val resultData = evalAndFetchResult(script)

    //        } catch (REXPMismatchException e) {
    //            throw new KnimeScriptingException("Failed to close image device and to read in plot as binary:+\n" + e.getMessage());
    //        }

    return try {
        ImageIO.read(ByteArrayInputStream(resultData))
    } catch (e: IOException) {
        throw RServeExceptionException(e.message ?: "")
    }
}

private fun RConnection.evalAndFetchResult(script: String): ByteArray {
    val fixedScript = fixEncoding(script)

    voidEval("try({\n$fixedScript\n}, silent = FALSE)")

    // close the image
    // check if the plot file has been written
    val tempFileName = eval("plotFile").asString()
    val xpInt = eval("file.access('$tempFileName',0)").asInteger()
    if (xpInt == -1) throw RServeExceptionException("Plot could not be created. Please check your script or submit a ticket to https://github.com/holgerbrandl/kravis")

    // we limit the file size to 1MB which should be sufficient and we delete the file as well
    val resultData = eval("try({ fileBytes <- readBin('$tempFileName','raw',2024*2024); unlink('$tempFileName'); fileBytes })")

    //            if (xp.inherits("try-error")) { // if the result is of the class try-error then there was a problem
    //                throw new KnimeScriptingException(xp.asString());
    //            }

    return resultData.asBytes()
}


class RServeExceptionException(s: String) : RuntimeException(s)


fun fixEncoding(stringValue: String): String {
    val encodedString = String(stringValue.toByteArray(StandardCharsets.UTF_8))
    return encodedString.replace("\r", "")
}


object RserveDeviceTester {
    @JvmStatic
    fun main(args: Array<String>) {
        SessionPrefs.RENDER_BACKEND = RserveEngine(host = "localhost", port = 6302)
//        SessionPrefs.RENDER_BACKEND = RserveEngine()

        irisScatter.show()
    }

}

fun main() {
    val connection = RConnection()
//    val connection = RConnection("localhost", 6311)


    val data = irisData.remove("Species")

    connection.setTable("df", data)

    val bufImage = connection.readImage("""plot(1:10)""")

    val outputfile = File("image.jpg")
    ImageIO.write(bufImage, outputfile.extension, outputfile)
    Desktop.getDesktop().open(outputfile)
}


internal fun GGPlot.saveTempFile(format: String = ".png") = save(createTempFile(suffix = format))
