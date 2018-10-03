package kravis.render

import kravis.GGPlot
import java.awt.Dimension
import java.io.*

/**
 * @author Holger Brandl
 */

enum class PlotFormat {
    PNG, SVG, EPS, JPG, PDF;

    override fun toString(): String {
        return "." + super.toString().toLowerCase()
    }

    companion object {
        @Suppress("SENSELESS_COMPARISON")
        fun isSupported(extension: String): Boolean = valueOf(extension.toUpperCase()) != null
    }
}


abstract class RenderEngine {
    internal abstract fun render(plot: GGPlot, outputFile: File, preferredSize: Dimension?): File
}

internal object EngineAutodetect {

    val R_ENGINE_DEFAULT by lazy {
        // todo autodetect environment and inform user about choide

        // todo make sure that required packages are installed and stop if not
        LocalR()
    }
}

abstract class AbstractLocalRenderEngine : RenderEngine() {


    fun compileScript(plot: GGPlot, dataIngest: String, savePath: String, preferredSize: Dimension?): String {
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

$dataIngest

set.seed(2009)

gg = $final

ggsave(filename="$savePath", plot=gg${optionalSizeConfig ?: ""})
                """.trimIndent()

        return rScript
    }

}


fun Dimension?.adjustSize(): String? {
    val optionalSizeConfig = this?.let {
        //https://graphicdesign.stackexchange.com/questions/71797/how-do-i-convert-the-width-from-pixels-to-inches-at-300-dpi
        //        You have an image that is 1,200x1,200 pixels.
        //        1,200 / 300 = 4
        //        So if you are printing at 300PPI, your image will be 4x4".

        val resulution = 150
        if (it.width / resulution < 0.1) {
            null
        } else {
            """ , width = ${it.width / resulution}, height = ${it.height / resulution}, units = "in""""
        }
    }
    return optionalSizeConfig
}


object RUtils {
    data class CmdResult(val exitCode: Int, val stdout: Iterable<String>, val stderr: Iterable<String>) {
        fun sout() = stdout.joinToString("\n").trim()

        fun serr() = stderr.joinToString("\n").trim()
    }


    fun runRScript(script: String): CmdResult {
        val scriptFile = createTempFile(suffix = ".R").apply { writeText(script) }

        return evalCmd("/usr/local/bin/R", listOf("--vanilla", "--quiet", "--slave", "-f", scriptFile.absolutePath))
    }

    fun evalBash(cmd: String): CmdResult = evalCmd("bash", listOf("-c", cmd))

    fun isInPath(tool: String) = evalBash("which $tool").sout().trim().isNotBlank()

    fun requireInPath(tool: String) = require(isInPath(tool)) { "$tool is required but not in PATH" }

    fun evalCmd(executable: String, args: List<String>, showOutput: Boolean = false,
                redirectStdout: File? = null, redirectStderr: File? = null): CmdResult {

        try {
            val pb = ProcessBuilder(*(arrayOf(executable) + args)) //.inheritIO();
            pb.directory(File("."));
            val p = pb.start();

            val outputGobbler = StreamGobbler(p.getInputStream(), if (showOutput) System.out else null)
            val errorGobbler = StreamGobbler(p.getErrorStream(), if (showOutput) System.err else null)

            // kick them off
            errorGobbler.start()
            outputGobbler.start()

            // any error???
            val exitVal = p.waitFor()
            return CmdResult(exitVal, outputGobbler.sb.lines(), errorGobbler.sb.lines())
        } catch (t: Throwable) {
            throw RuntimeException(t)
        }
    }


    internal class StreamGobbler(var inStream: InputStream, val printStream: PrintStream?) : Thread() {
        var sb = StringBuilder()

        override fun run() {
            try {
                val isr = InputStreamReader(inStream)
                val br = BufferedReader(isr)
                for (line in br.linesJ7()) {
                    sb.append(line!! + "\n")
                    printStream?.println(line)
                }
            } catch (ioe: IOException) {
                ioe.printStackTrace()
            }
        }

        private fun BufferedReader.linesJ7(): Iterable<String> = lineSequence().toList()


        val output: String get() = sb.toString()
    }

}


open class RenderingFailedException : java.lang.RuntimeException()

class LocalRenderingFailedException(val script: String, val invokeResult: RUtils.CmdResult) : RenderingFailedException() {
    override fun toString(): String = "Script:\n" + script + "\n\n" + invokeResult.serr()
}
