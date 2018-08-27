package com.github.holgerbrandl.kravis.ggplot


import com.github.holgerbrandl.kravis.ggplot.Aesthetic.x
import com.github.holgerbrandl.kravis.ggplot.Aesthetic.y
import com.github.holgerbrandl.kravis.ggplot.device.SwingPlottingDevice
import krangl.DataFrame
import krangl.irisData
import krangl.writeTSV
import java.io.File

/**
 * @author Holger Brandl
 */

fun DataFrame.ggplot(aes: Aes? = null) = GGPlot(this, aes)

fun DataFrame.ggplot(vararg aes: Pair<String, Aesthetic>) = GGPlot(this, Aes(*aes))

//var KRAVIS_LOG_GGPLOT_SCRIPT = false

class GGPlot(
    data: DataFrame? = null,
    mapping: Aes? = null,
    environment: String? = null
) {
    private val plotCmd = emptyList<String>().toMutableList()

    private val dataRegistry = mapOf<String, DataFrame>().toMutableMap()

    init {
        //todo check variable persence  in df her
        val dataVar: VarName? = registerDataset(data)

        val args = arg2string(
            "mapping" to mapping?.stringify(),
            "data" to dataVar,
            "environment" to environment
        )

        plotCmd.add("ggplot(${args})")
    }

    fun addLayer(layerSpec: String) {
        plotCmd.add("${layerSpec}")
    }


    fun registerDataset(data: DataFrame?): VarName? {
        if (data == null) {
            return null
        }

        val newVarName = "data" + dataRegistry.size + 1

        dataRegistry.put(newVarName, data)

        return VarName(newVarName)
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
        addLayer("coord_flip()")
    }

    fun title(title: String) = apply {
        addLayer("""ggtitle("${title.replace("\"", "'")}")""")
    }

    /** Add a custom command which is not yet supported by the wrapper API.  Example `gg.addCustom("+stat_bin()")*/
    fun addCustom(cmd: String) = apply {
        addLayer(cmd)
    }


    fun save(file: File) = render(file.extension).copyTo(file)


    fun show() {
        val imageFile = render(".png")

        require(imageFile.exists()) { "Visualization Failed. Could not render image." }

        //        FXPlottingDevice.showImage(imageFile)
        SwingPlottingDevice.showImage(imageFile)
    }

    override fun toString(): String {
        show()
        return ""
    }

    // todo expose supported formats as enum
    internal fun render(format: String = ".png"): File {
        val final = plotCmd.joinToString("+\n")

        val imageFile = createTempFile(suffix = format)

        // save all the data
        // todo hash dfs where possible to avoid IO
        val dataIngest = dataRegistry.mapValues {
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

class VarName(val name: String) {
    override fun toString() = name
}

internal fun Any.toStringAndQuote() = when (this) {
    is String -> "'${this}'"
    //    is VarName -> this.toString()
    is Aes -> this.toString().nullIfEmpty()
    is Boolean -> this.toString().toUpperCase()
    is RColor -> "'${this}'"
    else -> this
}

private fun String.nullIfEmpty(): String? {
    if (isEmpty()) return null else return this
}

/** Concatenates string-value pairs for which the value is not null*/
internal fun arg2string(vararg namedArgs: Pair<String, Any?>) =
    namedArgs.toMap()
        .filterValues { it != null }
        .map { "${it.key}=${it.value!!.toStringAndQuote()}" }
        .joinToString(", ")


class Aes(vararg val aes: Pair<String, Aesthetic>) {

    constructor(x: String? = null, y: String? = null, vararg aes: Pair<String, Aesthetic>) :
        this(*(aes.toList().addNonNull(x, Aesthetic.x).addNonNull(y, Aesthetic.y)).toTypedArray())

    fun stringify(): VarName? {
        if (aes.isEmpty()) return null

        val map = aes.map { "${it.second}=`${it.first}`" }
        val stringified = map.joinToString(",")
        return VarName("""aes($stringified)""")
    }
}

private fun List<Pair<String, Aesthetic>>.addNonNull(x: String?, aes: Aesthetic): List<Pair<String, Aesthetic>> {
    return if (x != null) this + Pair(x, aes) else this
}

enum class Aesthetic {
    x, y, color, fill, yintercept, xintercept, size, alpha
}

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


interface Stat


/** The identity statistic leaves the data unchanged. */
class StatIdentity : Stat {
    override fun toString(): String = "stat_identity()"
}


class StatCustom(val custom: String) : Stat {
    override fun toString() = custom
}


fun main(args: Array<String>) {
    //    ggplot(irisData, Aestethics("R" to x)).geomBar().show()
    GGPlot(irisData, Aes("Sepal.Length" to x, "Petal.Width" to y)).geomPoint(alpha = 0.1).title("Cool plot").show()

}