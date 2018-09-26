package kravis


import krangl.DataFrame
import kravis.SessionPrefs.OUTPUT_DEVICE
import kravis.SessionPrefs.RENDER_BACKEND
import kravis.device.JupyterDevice
import kravis.device.OutputDevice
import kravis.device.SwingPlottingDevice
import kravis.render.EngineAutodetect
import kravis.render.PlotFormat
import kravis.render.RenderEngine
import java.awt.Dimension
import java.io.File

/**
 * Various Settings to finetune rendering and behavior of kravis. Those settins are not persisted and need to be configured on a per session basis
 */
object SessionPrefs {


    private val AUTO_DETECT_DEVICE by lazy {
        try {
            Class.forName("org.jetbrains.kotlin.jupyter.KernelConfig")
            infoMsg("Using jupyter device")
            JupyterDevice()
        } catch (e: ClassNotFoundException) {
            // it's not jupyter so default back to swing
            SwingPlottingDevice()
        }
    }

    var OUTPUT_DEVICE: OutputDevice = AUTO_DETECT_DEVICE

    /** Render plots when toString is invoked. This makes it more convenient in a termimal setting. */

    var RENDER_BACKEND: RenderEngine = EngineAutodetect.R_ENGINE_DEFAULT

    /** Render plots when toString is invoked. This makes it more convenient in a termimal setting. */
    var SHOW_TO_STRING = !(OUTPUT_DEVICE is JupyterDevice)
}


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
    ymax: String? = null,
    label: String? = null

): GGPlot {
    val mapping = listOf<Pair<String, Aesthetic>>()
        .addNonNull(x, Aesthetic.x)
        .addNonNull(y, Aesthetic.y)
        .addNonNull(alpha, Aesthetic.alpha)
        .addNonNull(color, Aesthetic.color)
        .addNonNull(fill, Aesthetic.fill)
        .addNonNull(shape, Aesthetic.shape)
        .addNonNull(size, Aesthetic.size)
        .addNonNull(stroke, Aesthetic.stroke)
        .addNonNull(ymin, Aesthetic.ymin)
        .addNonNull(ymax, Aesthetic.ymax)
        .addNonNull(label, Aesthetic.label)

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
        if (SessionPrefs.SHOW_TO_STRING) {
            try {
                show()
            } catch (e: Exception) {
                System.err.println("Plot pendering failed with " + e)
            }
        }
        //        show() // this should just apply to a terminal setting. in jupypter we actually need to return a value
        return spec
        //        return ""
    }

    val spec: String get() = plotCmd.joinToString(" + \n\t")


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
        ymax: String? = null,
        label: String? = null
    ) :
        this(*listOf<Pair<String, Aesthetic>>()
            .addNonNull(x, Aesthetic.x)
            .addNonNull(y, Aesthetic.y)
            .addNonNull(alpha, Aesthetic.alpha)
            .addNonNull(color, Aesthetic.color)
            .addNonNull(fill, Aesthetic.fill)
            .addNonNull(shape, Aesthetic.shape)
            .addNonNull(size, Aesthetic.size)
            .addNonNull(stroke, Aesthetic.size)
            .addNonNull(ymin, Aesthetic.ymin)
            .addNonNull(ymax, Aesthetic.ymax)
            .addNonNull(label, Aesthetic.label)
            .toTypedArray()
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

    ymax,

    label
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

