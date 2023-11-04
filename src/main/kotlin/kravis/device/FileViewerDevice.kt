package kravis.device

import kravis.GGPlot
import kravis.render.PlotFormat
import java.awt.Desktop
import java.awt.Dimension
import java.nio.file.Files
import java.nio.file.Path
import java.time.Instant


/**
 * Generates and displays a temporary PNG file using the system's image viewer
 *
 * @return The path of the generated file, or null if an error occurred.
 */
fun GGPlot.showFile(): Path? {
    val file = Files.createTempFile("kravis" + Instant.now().toString().replace(":", ""), ".png")

    save(file, Dimension(1000, 800))

    Desktop.getDesktop().open(file.toFile())

    return file
}



class FileViewerDevice : OutputDevice(){
    override fun getPreferredFormat(): PlotFormat = PlotFormat.PNG

    override fun show(plot: GGPlot): Any {


        return plot.showFile()!!
    }

}
