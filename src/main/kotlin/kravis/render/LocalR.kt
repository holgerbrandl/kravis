package kravis.render

import krangl.writeTSV
import kravis.GGPlot
import java.awt.Dimension
import java.io.File

class LocalR : AbstractLocalRenderEngine() {

    override fun render(plot: GGPlot, outputFile: File, preferredSize: Dimension?): File {
        // save all the data
        // todo hash dfs where possible to avoid IO
        val dataIngest = plot.dataRegistry.mapValues {
            createTempFile(".txt").apply { it.value.writeTSV(this) }
        }.map { (dataVar, file) ->
            """${dataVar} = read_tsv("${file}")"""
        }.joinToString("\n")

        val rScript = compileScript(plot, dataIngest, outputFile.absolutePath, preferredSize)

        val result = RUtils.runRScript(rScript)
        if (result.exitCode != 0) {
            throw LocalRenderingFailedException(rScript, result)
        }

        require(outputFile.exists()) { System.err.println("Image generation failed") }
        return outputFile
    }
}