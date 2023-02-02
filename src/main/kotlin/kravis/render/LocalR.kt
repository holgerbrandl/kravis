package kravis.render

import com.github.holgerbrandl.kdfutils.writeTSV
import kravis.GGPlot
import java.awt.Dimension
import java.io.File
import java.nio.file.Path
import kotlin.io.path.createTempFile
import kotlin.io.path.exists

/**
 * @param path Path to R executable. If not set it will be inferred from PATH/environment settings.
 */
class LocalR(val r:File?= null) : AbstractLocalRenderEngine() {

    override fun render(plot: GGPlot, outputFile: Path, preferredSize: Dimension?): Path {
        // save all the data
        // todo hash dfs where possible to avoid IO
        val dataIngest = plot.dataRegistry.mapValues { (_, value) ->
//            it.value.schema()
//            it.value.print()
            createTempFile(".txt").apply { value.writeTSV(this.toFile()) }
        }.map { (dataVar, file) ->
            """${dataVar} = read_tsv("${file.toFile().invariantSeparatorsPath}")"""
        }.joinToString("\n")

        val rScript = compileScript(plot, dataIngest, outputFile.toFile().invariantSeparatorsPath, preferredSize)

        val result = RUtils.runRScript(rScript, r)
        if (result.exitCode != 0) {
            throw LocalRenderingFailedException(rScript, result)
        }

        require(outputFile.exists()) { System.err.println("Image generation failed") }
        return outputFile
    }
}