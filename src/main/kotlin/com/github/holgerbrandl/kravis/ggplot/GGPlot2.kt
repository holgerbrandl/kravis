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

fun DataFrame.ggplot(aes: AES? = null) = GGPlot(this, aes)

fun DataFrame.ggplot(vararg aes: Pair<String, Aesthetic>) = GGPlot(this, AES(*aes))

class GGPlot(df: DataFrame? = null, aes: AES? = null) {
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
    fun geomBar(aes: AES? = null, data: DataFrame? = null, stat: String = "identity",
                position: String = "identity", naRm: Boolean = false, showLegend: String? = null,
                inheritAes: Boolean = true): GGPlot = apply {
        // todo make sure to forward all options
        plotCmd.append("+ geom_bar()")
    }

    fun geomPoint(aes: AES? = null, data: DataFrame? = null, stat: String = "identity",
                  position: Position? = null, naRm: Boolean = false, showLegend: String? = null,
                  inheritAes: Boolean = true,

        // list all the aes it understands here for static mapping
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


    fun geomBoxplot(position: Position? = null) = apply {
        plotCmd.append("+ geom_boxplot(${arg2string("position" to position)})")
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

    private fun render(format: String): File {
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

                gg = $final
                ggsave(filename="${imageFile.absolutePath}", plot=gg)
            """.trimIndent()

        RUtils.runRScript(script = rScript)

        println(rScript)

        require(imageFile.exists()) { System.err.println("Image generation failed") }
        return imageFile
    }

}

internal fun arg2string(vararg namedArgs: Pair<String, Any?>) =
    namedArgs.toMap()
        .filterValues { it != null }
        .map { "${it.key}=${it.value.toString()}" }
        .joinToString(", ")

interface Position

class PositionJitter(val height: Double? = null, val width: Double? = null) : Position {
    override fun toString(): String {
        var args = ""

        if (height != null) args += "height=$height"
        if (height != null && width != null) args += ", "
        if (width != null) args += "width=$width"

        return "position_jitter($args)"
    }
}

class AES(vararg val aes: Pair<String, Aesthetic>) {

    override fun toString(): String {
        //todo check variable persence  in df her
        val stringified = aes.map { "${it.second}=${it.first}" }.joinToString(",")
        return """aes($stringified)"""
    }
}

enum class Aesthetic {
    x, y, color, fill, yintercept, xintercept, size, alpha
}

fun main(args: Array<String>) {
    //    ggplot(irisData, Aestethics("R" to x)).geomBar().show()
    GGPlot(irisData, AES("Sepal.Length" to x, "Petal.Width" to y)).geomPoint(alpha = 0.1).title("Cool plot").show()

}