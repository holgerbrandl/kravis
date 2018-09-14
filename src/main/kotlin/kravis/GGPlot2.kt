package kravis


import krangl.DataFrame
import krangl.irisData
import kravis.Aesthetic.x
import kravis.Aesthetic.y
import kravis.device.DeviceAutodetect
import kravis.device.OutputDevice
import kravis.render.EngineAutodetect
import kravis.render.PlotFormat
import kravis.render.RenderEngine
import java.awt.Dimension
import java.io.File

/**
 * @author Holger Brandl
 */

// session preferences
// should we encapsulate them into a namespace??
var OUTPUT_DEVICE: OutputDevice = DeviceAutodetect.OUTPUT_DEVICE_DEFAULT
var RENDER_BACKEND: RenderEngine = EngineAutodetect.R_ENGINE_DEFAULT

fun DataFrame.ggplot(aes: Aes? = null) = GGPlot(this, aes)

fun DataFrame.ggplot(
    x: String? = null,
    y: String? = null,
    alpha: String? = null,
    color: String? = null,
    fill: String? = null,
    shape: String? = null,
    size: String? = null,
    stroke: String? = null,
    ymin: String? = null,
    ymax: String? = null

): GGPlot {
    val mapping = listOf<Pair<String, Aesthetic>>()
        .addNonNull(x, Aesthetic.x)
        .addNonNull(y, Aesthetic.y)
        .addNonNull(alpha, Aesthetic.alpha)
        .addNonNull(color, Aesthetic.color)
        .addNonNull(fill, Aesthetic.fill)
        .addNonNull(shape, Aesthetic.shape)
        .addNonNull(size, Aesthetic.size)
        .addNonNull(size, Aesthetic.stroke)
        .addNonNull(ymin, Aesthetic.ymin)
        .addNonNull(ymax, Aesthetic.ymax)

    val aes = Aes(*mapping.toTypedArray()
    )
    return GGPlot(this, aes)
}

fun DataFrame.ggplot(vararg aes: Pair<String, Aesthetic>) = GGPlot(this, Aes(*aes))


//var KRAVIS_LOG_GGPLOT_SCRIPT = false

class GGPlot(
    data: DataFrame? = null,
    mapping: Aes? = null,
    environment: String? = null
) {
    internal val preambble = emptyList<String>().toMutableList()

    internal val plotCmd = emptyList<String>().toMutableList()

    internal val dataRegistry = mapOf<String, DataFrame>().toMutableMap()

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

    fun addPreamble(preAmble: String) = apply { preambble.add(preAmble) }

    internal fun appendSpec(block: GGPlot.() -> Unit): GGPlot {
        val newPlot = GGPlot()

        // clone the current state
        newPlot.plotCmd.apply { clear(); addAll(plotCmd) }

        newPlot.preambble.apply { addAll(preambble) }
        newPlot.dataRegistry.apply { putAll(dataRegistry) }


        // apply the changes
        newPlot.block()

        return newPlot
    }


    fun addSpec(layerSpec: String) {
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


    /** Add a custom command which is not yet supported by the wrapper API.  Example `gg.addCustom("+stat_bin()")*/
    fun addCustom(cmd: String) = appendSpec {
        addSpec(cmd)
    }


    /** Return the file to which the plot was saved. */
    fun save(file: File, preferredSize: Dimension? = null): File {
        require(PlotFormat.isSupported(file.extension)) { "Unsupported image format" }
        return RENDER_BACKEND.render(this, file, preferredSize)
    }


    fun show(): Any {
        return OUTPUT_DEVICE.show(this)
    }

    override fun toString(): String {
        //        show() // this should just apply to a terminal setting. in jupypter we actually need to return a value
        return plotCmd.joinToString("+\n")
        //        return ""
    }

    // various helper methods (which could all be extensions for simplicity)
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
    fun coordFlip() = appendSpec {
        addSpec("coord_flip()")
    }

    fun title(title: String) = appendSpec {
        addSpec("""ggtitle("${title.replace("\"", "'")}")""")
    }

    fun xLabel(label: String) = appendSpec {
        addSpec("""xlab("${label}")""")
    }

    fun yLabel(label: String) = appendSpec {
        addSpec("""ylab("${label}")""")
    }

    /**
     * Good labels are critical for making your plots accessible to a wider audience. Ensure the axis and legend
     * labels display the full variable name. Use the plot title and subtitle to explain the main findings.
     * It's common to use the caption to provide information about the data source. tag can be used for adding identification tags.
     */
    fun labs(title: String? = null, caption: String? = null, x: String? = null, y: String? = null, tag: String? = null) = appendSpec {
        val args = arg2string(
            "title" to title,
            "caption" to caption,
            "x" to x,
            "y" to y,
            "tag" to tag
        )

        addSpec("""labs("${args}")""")
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
    is BarStat -> "'${this}'"
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

    //    constructor(x: String? = null, y: String? = null, vararg aes: Pair<String, Aesthetic>) :
    //        this(*(aes.toList().addNonNull(x, Aesthetic.x).addNonNull(y, Aesthetic.y)).toTypedArray())


    constructor(
        x: String? = null,
        y: String? = null,
        alpha: String? = null,
        color: String? = null,
        fill: String? = null,
        shape: String? = null,
        size: String? = null,
        stroke: String? = null,
        ymin: String? = null,
        ymax: String? = null
    ) :
        this(*listOf<Pair<String, Aesthetic>>()
            .addNonNull(x, Aesthetic.x)
            .addNonNull(y, Aesthetic.y)
            .addNonNull(alpha, Aesthetic.alpha)
            .addNonNull(color, Aesthetic.color)
            .addNonNull(fill, Aesthetic.fill)
            .addNonNull(shape, Aesthetic.shape)
            .addNonNull(size, Aesthetic.size)
            .addNonNull(ymin, Aesthetic.ymin)
            .addNonNull(ymax, Aesthetic.ymax).toTypedArray()
        )

    fun stringify(): VarName? {
        if (aes.isEmpty()) return null

        val map = aes.map { (expr, aesthetic) ->
            val quoted = if (expr.isRExpression) {
                expr.removePrefix(EXPRESSION_PREFIX)
            } else {
                "`$expr`"
            }
            "${aesthetic}=$quoted"
        }
        val stringified = map.joinToString(",")
        return VarName("""aes($stringified)""")
    }
}

private fun List<Pair<String, Aesthetic>>.addNonNull(x: String?, aes: Aesthetic): List<Pair<String, Aesthetic>> {
    return if (x != null) this + Pair(x, aes) else this
}

enum class Aesthetic {
    x, y, color, fill, yintercept, xintercept, size, alpha, shape, stroke,

    ymin,

    ymax
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

class PositionIdentity() : Position {
    override fun toString() = "position_identity()"
}

/**
 * `position_nudge` is generally useful for adjusting the position of items on discrete scales by a small amount.
 * Nudging is built in to geom_text() because it's so useful for moving labels a small distance from what they're labelling.
 */
class PositionNudge(val x: Double = 0.0, val y: Double = 0.0) : Position {

    override fun toString(): String = "position_nudge(${x}, ${y})"
}


interface Stat


/** The identity statistic leaves the data unchanged. */
class StatIdentity : Stat {
    override fun toString(): String = "identity".quoted
}


class StatCustom(val custom: String) : Stat {
    override fun toString() = custom
}

internal val String.quoted: String
    get() = "'" + this + "'"


fun main(args: Array<String>) {
    //    ggplot(irisData, Aestethics("R" to x)).geomBar().show()
    GGPlot(irisData, Aes("Sepal.Length" to x, "Petal.Width" to y)).geomPoint(alpha = 0.1).title("Cool Plot").show()

}

/** Geoms that draw lines have a "linetype" parameter.
 *
 * See http://sape.inf.usi.ch/quick-reference/ggplot2/linetype
 */
enum class LineType {
    blank, solid, dashed, dotted, dotdash, longdash, twodash
}


object OrderUtils {

    enum class OrderFun {
        mean, median
    }

    /**
     * fct_reorder() is useful for 1d displays where the factor is mapped to position
     *
     * The levels of `f` are reordered so that the values of `fun(orderAttribute)` (for fct_reorder()) are in ascending order.
     */
    fun reorder(f: String, orderAttribute: String, orderFun: OrderFun = OrderFun.mean, ascending: Boolean = true): String {
        return if (ascending) "fct_reorder($f, $orderAttribute, fun=$orderFun)"
        else "fct_rev(fct_reorder($f, $orderAttribute, fun=$orderFun))".asRExpression
    }

}

// note we should rather use some receive context if possible here
val String.asDiscreteVariable: String
    get() = "as.factor($this)".asRExpression

internal val EXPRESSION_PREFIX = ".r_expression."

internal val String.asRExpression: String
    get() = EXPRESSION_PREFIX + this

internal val String.isRExpression: Boolean
    get() = startsWith(EXPRESSION_PREFIX)

