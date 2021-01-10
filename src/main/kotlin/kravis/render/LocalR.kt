package kravis.render

import krangl.print
import krangl.schema
import krangl.writeTSV
import kravis.GGPlot
import java.awt.Dimension
import java.io.File

/**
 * @param path Path to R executable. If not set it will be inferred from PATH/environment settings.
 */
class LocalR(val r:File?= null) : AbstractLocalRenderEngine() {

    override fun render(plot: GGPlot, outputFile: File, preferredSize: Dimension?): File {
        // save all the data
        // todo hash dfs where possible to avoid IO
        val dataIngest = plot.dataRegistry.mapValues {
//            it.value.schema()
//            it.value.print()
            createTempFile(".txt").apply { it.value.writeTSV(this) }
        }.map { (dataVar, file) ->
            """${dataVar} = read_tsv("${file.invariantSeparatorsPath}")"""
        }.joinToString("\n")

        val rScript = compileScript(plot, dataIngest, outputFile.invariantSeparatorsPath, preferredSize)

        val result = RUtils.runRScript(rScript, r)
        if (result.exitCode != 0) {
            throw LocalRenderingFailedException(rScript, result)
        }

        require(outputFile.exists()) { System.err.println("Image generation failed") }
        return outputFile
    }
}