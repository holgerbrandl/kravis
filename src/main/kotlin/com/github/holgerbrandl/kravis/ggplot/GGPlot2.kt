package com.github.holgerbrandl.kravis.ggplot


import com.github.holgerbrandl.kravis.ggplot.Aesthetic.x
import com.github.holgerbrandl.kravis.ggplot.Aesthetic.y
import krangl.DataFrame
import krangl.irisData
import krangl.writeTSV
import java.io.File

/**
 * @author Holger Brandl
 */

fun DataFrame.ggplot(aes: aes? = null) = GGPlot(this, aes)

fun DataFrame.ggplot(vararg aes: Pair<String, Aesthetic>) = GGPlot(this, aes(*aes))

//var KRAVIS_LOG_GGPLOT_SCRIPT = false

class GGPlot(df: DataFrame? = null, aes: aes? = null) {
    val plotCmd = StringBuffer()

    val dfs = mutableMapOf<String, DataFrame>()

    init {
        if (df != null) {
            dfs.put("df1", df)
        }

        if (aes != null) {
            plotCmd.append("ggplot(df1, $aes)")
        } else {
            plotCmd.append("ggplot(df1)")
        }
    }

    // todo use more constrained aestetics with just the suppored fields
    fun geomBar(mapping: aes? = null, data: DataFrame? = null, stat: String = "identity",
                position: String = "identity", naRm: Boolean = false, showLegend: String? = null,
                inheritAes: Boolean = true): GGPlot = apply {
        // todo make sure to forward all options
        plotCmd.append("+ geom_bar()")
    }

    fun geomPoint(mapping: aes? = null, data: DataFrame? = null, stat: String = "identity",
                  position: Position? = null, naRm: Boolean = false, showLegend: String? = null,
                  inheritAes: Boolean = true,

        // list all the mapping it understands here for static mapping
                  alpha: Double?
        //                  static: Map<Aesthetic, Any>?=null
    ): GGPlot {

        val args = mutableListOf<String>()

        args.add(arg2string("position" to position))

        if (alpha != null) args.add("alpha=${alpha}")

        //        val staticAesStringified = static.map { it.key.toString() + "=" + it.value.toString() }
        //        args.addAll(staticAesStringified)

        return apply {
            // todo make sure to forward all options
            plotCmd.append("+ geom_point(${args.joinToString(" ,")})")
        }
    }


    /**
     * The boxplot compactly displays the distribution of a continuous variable. It visualises five summary
     * statistics (the median, two hinges and two whiskers), and all "outlying" points individually.
     *
     * Original reference https://ggplot2.tidyverse.org/reference/geom_boxplot.html
     */
    fun geomBoxplot(
        position: Position? = null,
        // boxplot specific args
        notch: Boolean? = null,
        fill: RColor? = null,
        color: RColor? = null
    ): GGPlot = apply {
        val arg2string = arg2string("position" to position, "notch" to notch, "fill" to fill, "color" to color)

        plotCmd.append("+ geom_boxplot($arg2string)")
    }


    // orig signature coord_flip(xlim = NULL, ylim = NULL, expand = TRUE, clip = "on")
    /**
     * Flip cartesian coordinates so that horizontal becomes vertical, and vertical, horizontal. This is primarily useful for converting geoms and statistics which display y conditional on x, to x conditional on y.
     *
     * ### Example
     * ```
     * ggplot(diamonds, aes(cut, price)) +
     *   geom_boxplot() +
     *   coord_flip()
     * ```
     */
    fun coordFlip() = apply {
        plotCmd.append("+ coord_flip()")
    }

    fun title(title: String) = apply {
        plotCmd.append(""" + ggtitle("${title.replace("\"", "'")}")""")
    }

    /** Add a custom command which is not yet supported by the wrapper API.  Example `gg.addCustom("+stat_bin()")*/
    fun addCustom(cmd: String) = apply {
        plotCmd.append(cmd)
    }


    fun save(file: File) = render(file.extension).copyTo(file)


    fun show() {
        val imageFile = render(".png")

        FXPlottingDevice.showImage(imageFile)
    }

    override fun toString(): String {
        show()
        return ""
    }

    // todo expose supported formats as enum
    internal fun render(format: String = ".png"): File {
        val final = plotCmd.toString()

        val imageFile = createTempFile(suffix = format)

        // save all the data
        // todo hash dfs where possible to avoid IO
        val dataIngest = dfs.mapValues {
            createTempFile(".txt").apply { it.value.writeTSV(this) }
        }.map { (dataVar, file) ->
            """${dataVar} = read_tsv("${file}")"""
        }.joinToString("\n")


        val rScript = """
                library(ggplot2)
                library(dplyr)
                library(readr)

                $dataIngest

set.seed(2009)
                gg = $final
                ggsave(filename="${imageFile.absolutePath}", plot=gg)
            """.trimIndent()

        RUtils.runRScript(script = rScript)

        println(rScript)

        require(imageFile.exists()) { System.err.println("Image generation failed") }
        return imageFile
    }

}

internal fun Any.toStringAndQuote() = when (this) {
    is String -> "'${this}'"
    is Boolean -> this.toString().toUpperCase()
    is RColor -> "'${this}'"
    else -> this
}

internal fun arg2string(vararg namedArgs: Pair<String, Any?>) =
    namedArgs.toMap()
        .filterValues { it != null }
        .map { "${it.key}=${it.value!!.toStringAndQuote()}" }
        .joinToString(", ")

interface Position

class PositionJitter(val height: Double? = null, val width: Double? = null, val seed: Int? = null) : Position {
    override fun toString(): String {
        val args = mapOf("height" to height, "width" to width, "seed" to seed)
            .filter { it.value != null }
            .map { (key, value) -> "$key=$value" }
            .joinToString(", ")

        return "position_jitter($args)"
    }
}


class aes(vararg val aes: Pair<String, Aesthetic>) {

    constructor(x: String, y: String, vararg aes: Pair<String, Aesthetic>) : this(*(aes.toList() + Pair(x, Aesthetic.x) + Pair(y, Aesthetic.y)).toTypedArray()) {
    }

    override fun toString(): String {
        //todo check variable persence  in df her
        val stringified = aes.map { "${it.second}=`${it.first}`" }.joinToString(",")
        return """aes($stringified)"""
    }
}

enum class Aesthetic {
    x, y, color, fill, yintercept, xintercept, size, alpha
}

fun main(args: Array<String>) {
    //    ggplot(irisData, Aestethics("R" to x)).geomBar().show()
    GGPlot(irisData, aes("Sepal.Length" to x, "Petal.Width" to y)).geomPoint(alpha = 0.1).title("Cool plot").show()

}