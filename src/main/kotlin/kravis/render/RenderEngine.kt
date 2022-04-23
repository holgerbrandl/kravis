package kravis.render

import kravis.GGPlot
import kravis.toStringAndQuote
import java.awt.Dimension
import java.io.*
import java.nio.charset.StandardCharsets
import java.nio.file.Path
import java.util.*
import kotlin.io.path.createTempFile

/**
 * @author Holger Brandl
 */

enum class PlotFormat {
    PNG, SVG, EPS, JPG, PDF;

    override fun toString(): String {
        return "." + super.toString().lowercase(Locale.US)
    }

    companion object {
        @Suppress("SENSELESS_COMPARISON")
        fun isSupported(extension: String): Boolean = valueOf(extension.uppercase(Locale.US)) != null
    }
}


abstract class RenderEngine {
    internal abstract fun render(plot: GGPlot, outputFile: Path, preferredSize: Dimension?): Path
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
    data class CmdResult(val exitCode: Int, val sout: String, val serr: String)

    fun runRScript(script: String, r: File? = null): CmdResult {
        val scriptFile = createTempFile(suffix = ".R").toFile().apply { writeText(script) }

        return evalCmd(r?.absolutePath ?: "R", listOf("--vanilla", "--quiet", "--slave", "-f", scriptFile.absolutePath))
    }

    fun evalBash(cmd: String): CmdResult = evalCmd("bash", listOf("-c", cmd))

    //    fun isInPath(tool: String) = evalBash("which $tool").sout().trim().isNotBlank()
    fun isInPath(tool: String, helpCmd: String = "--help"): Boolean {
        val rt = Runtime.getRuntime()
        val proc = rt.exec("$tool $helpCmd")
        proc.waitFor()
        return proc.exitValue() == 0
    }

    fun requireInPath(tool: String) = require(isInPath(tool)) { "$tool is required but not in PATH" }

    fun evalCmd(
        executable: String, args: List<String>, showOutput: Boolean = false
    ): CmdResult {

        try {
            val pb = ProcessBuilder(*(arrayOf(executable) + args)) //.inheritIO();
            pb.directory(File("."));
            val p = pb.start();

            // any error???
            val exitVal = p.waitFor()
            val sout = p.inputStream.readAllBytes().toString(StandardCharsets.UTF_8)
            val serr = p.errorStream.readAllBytes().toString(StandardCharsets.UTF_8)
            if (showOutput) {
                System.out.println(sout)
                System.err.println(serr)
            }
            return CmdResult(exitVal, sout, serr)
        } catch (t: Throwable) {
            throw RuntimeException(t)
        }
    }
}


open class RenderingFailedException : java.lang.RuntimeException()

class LocalRenderingFailedException(val script: String, val invokeResult: RUtils.CmdResult) : RenderingFailedException() {
    override fun toString(): String = "Script:\n" + script + "\n\n" + invokeResult.serr
}
