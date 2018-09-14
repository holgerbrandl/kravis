package kravis

import krangl.DataFrame

/**
 * @author Holger Brandl
 */

/**
 * The boxplot compactly displays the distribution of a continuous variable. It visualises five summary
 * statistics (the median, two hinges and two whiskers), and all "outlying" points individually.
 *
 * Original reference https://ggplot2.tidyverse.org/reference/geom_boxplot.html
 */
fun GGPlot.geomBoxplot(
    // generic options to all geoms
    mapping: Aes? = null,
    data: DataFrame? = null,
    stat: Stat = StatBxoplot(),
    position: Position = PositionDodge2(),
    showLegend: Boolean? = null,
    removeNAs: Boolean = false,
    inheritAes: Boolean = true,

    outlierColour: RColor? = null,
    outlierColor: RColor? = null,
    outlierFill: RColor? = null,
    outlierShape: Int = 19,
    outlierSize: Double = 1.5,
    outlierStroke: Double = .5,
    outlierAlpha: Double? = null,
    notch: Boolean = false,
    notchwidth: Double = .5,
    varwidth: Boolean = false,


    // list all the aesthetics it understands
    alpha: Double? = null,
    color: RColor? = null,
    fill: RColor? = null,
    // group, // todo, unclear usage
    linetype: LineType? = null,
    shape: Int? = null,
    size: Int? = null,
    weight: Double? = null

): GGPlot = appendSpec {

    val dataVar: VarName? = registerDataset(data)

    val arg2string = arg2string(
        "mapping" to mapping?.stringify(),
        "data" to dataVar,
        "stat" to stat,
        "position" to position,
        "na.rm" to removeNAs,
        "show.legend" to showLegend,
        "inherit.aes" to inheritAes,

        // other options
        "outlier.colour" to outlierColour,
        "outlier.color" to outlierColor,
        "outlier.fill" to outlierFill,
        "outlier.shape" to outlierShape,
        "outlier.size" to outlierSize,
        "outlier.stroke" to outlierStroke,
        "outlier.alpha" to outlierAlpha,
        "notch" to notch,
        "notchwidth" to notchwidth,
        "varwidth" to varwidth,

        // list all the aesthetics it understands
        "alpha" to requireZeroOne(alpha),
        "color" to color,
        "fill" to fill,
        "shape" to shape,
        "linetype" to linetype,
        "size" to size,
        "weight" to weight
    )


    addSpec("geom_boxplot($arg2string)")
}

/**
 * The point geom is used to create scatterplots. The scatterplot is most useful for displaying the relationship
 * between two continuous variables. It can be used to compare one continuous and one categorical variable, or
 * two categorical variables.
 *
 * Official reference [geom_point](https://ggplot2.tidyverse.org/reference/geom_point.html)
 */
fun GGPlot.geomPoint(
    // generic options to all geoms
    mapping: Aes? = null,
    data: DataFrame? = null,
    stat: Stat = StatIdentity(),
    position: Position = PositionIdentity(),
    showLegend: Boolean? = null,
    removeNAs: Boolean = false,
    inheritAes: Boolean = true,

    // list all the aesthetics it understands
    alpha: Double? = null,
    color: RColor? = null,
    fill: RColor? = null,
    // group, // todo, unclear usage
    shape: Int? = null,
    size: Int? = null,
    stroke: Int? = null // Use the stroke aesthetic to modify the width of the border
): GGPlot = appendSpec {

    val dataVar: VarName? = registerDataset(data)

    val args = arg2string(
        "mapping" to mapping?.stringify(),
        "data" to dataVar,
        "stat" to stat,
        "position" to position,
        "na.rm" to removeNAs,
        "show.legend" to showLegend,
        "inherit.aes" to inheritAes,


        "position" to position,
        "alpha" to requireZeroOne(alpha),
        "color" to color,
        "fill" to fill,
        "shape" to shape,
        "size" to size,
        "stroke" to stroke
    )

    addSpec("geom_point(${args})")
}

/**
 * In a dot plot, the width of a dot corresponds to the bin width (or maximum width, depending on the binning algorithm), and dots are stacked, with each dot representing one observation.
 *
 * Official reference [geom_dotplot](https://ggplot2.tidyverse.org/reference/geom_dotplot.html)
 */
fun GGPlot.geomDotplot(
    // generic options to all geoms
    mapping: Aes? = null,
    data: DataFrame? = null,
    stat: Stat = StatIdentity(),
    position: Position = PositionIdentity(),
    showLegend: Boolean? = null,
    removeNAs: Boolean = false,
    inheritAes: Boolean = true,

    // list all the aesthetics it understands
    alpha: Double? = null,
    color: RColor? = null,
    fill: RColor? = null,
    binaxis: String = "x",
    method: String = "dotdensity",
    binpositions: String = "bygroup",
    stackdir: String = "up",
    stackratio: Double = 1.0,
    dotsize: Double = 1.0,
    binwidth: Double
): GGPlot = appendSpec {

    val dataVar: VarName? = registerDataset(data)

    val args = arg2string(
        "mapping" to mapping?.stringify(),
        "data" to dataVar,
        "stat" to stat,
        "position" to position,
        "na.rm" to removeNAs,
        "show.legend" to showLegend,
        "inherit.aes" to inheritAes,


        "position" to position,
        "alpha" to requireZeroOne(alpha),
        "color" to color,
        "fill" to fill,

        "binaxis" to binaxis,
        "method" to method,
        "binpositions" to binpositions,
        "stackdir" to stackdir,
        "stackratio" to stackratio,
        "dotsize" to dotsize
    )

    addSpec("geom_dotplot(${args})")
}


private fun requireZeroOne(d: Double?) = d?.also { require(it >= 0 && it <= 1) { "alpha must be [0,1] but was $it." } }


enum class BarStat {
    count, identity
}
// todo use more constrained aestetics with just the suppored fields or validate supported aestehtics
/**
 * There are two types of bar charts: `geom_bar` makes the height of the bar proportional to the number of cases in each group (or if the weight aesthetic is supplied, the sum of the weights). If you want the heights of the bars to represent values in the data, use `geom_col` instead. `geom_bar` uses `stat_count` by default: it counts the number of cases at each x position. `geom_col` uses `stat_identity`: it leaves the data as is.
 */
fun GGPlot.geomBar(
    // generic options to all geoms
    mapping: Aes? = null,
    data: DataFrame? = null,
    stat: BarStat = BarStat.count,
    position: Position = PositionIdentity(),
    showLegend: Boolean? = null,
    removeNAs: Boolean = false,
    inheritAes: Boolean = true,

    // list all the aesthetics it understands
    alpha: Double? = null,
    color: RColor? = null,
    fill: RColor? = null,
    // group, // todo, unclear usage
    linetype: LineType? = null,
    size: Int? = null
): GGPlot = appendSpec {
    val dataVar: VarName? = registerDataset(data)

    val args = arg2string(
        "mapping" to mapping?.stringify(),
        "data" to dataVar,
        "stat" to stat,
        "position" to position,
        "na.rm" to removeNAs,
        "show.legend" to showLegend,
        "inherit.aes" to inheritAes,


        "alpha" to requireZeroOne(alpha),
        "color" to color,
        "fill" to fill,
        "linetype" to linetype,
        "size" to size
    )

    addSpec("geom_bar(${args})")
}

/**
 * A geom that draws error bars, defined by an upper and lower value. This is useful e.g., to draw confidence intervals.
 * See https://ggplot2.tidyverse.org/reference/geom_linerange.html
 */
fun GGPlot.geomErrorBar(
    // generic options to all geoms
    mapping: Aes? = null,
    data: DataFrame? = null,
    stat: BarStat = BarStat.identity,
    position: Position = PositionIdentity(),
    showLegend: Boolean? = null,
    removeNAs: Boolean = false,
    inheritAes: Boolean = true,

    // list all the aesthetics it understands
    alpha: Double? = null,
    color: RColor? = null,
    fill: RColor? = null,
    linetype: LineType? = null,
    size: Int? = null,
    width: Double? = null
): GGPlot = appendSpec {
    val dataVar: VarName? = registerDataset(data)

    val args = arg2string(
        "mapping" to mapping?.stringify(),
        "data" to dataVar,
        "stat" to stat,
        "position" to position,
        "na.rm" to removeNAs,
        "show.legend" to showLegend,
        "inherit.aes" to inheritAes,


        "alpha" to requireZeroOne(alpha),
        "width" to width,
        "color" to color,
        "fill" to fill,
        "linetype" to linetype,
        "size" to size,
        "width" to width
    )

    addSpec("geom_errorbar(${args})")
}


/**
 * @linetype For options see http://sape.inf.usi.ch/quick-reference/ggplot2/linetype
 */
fun GGPlot.geomLine(
    mapping: Aes? = null,
    data: DataFrame? = null,
    stat: Stat? = null,
    position: Position? = null,
    showLegend: Boolean? = null,

    alpha: Double? = null,
    color: RColor? = null,
    // group, // todo
    linetype: Int? = null,
    size: Int? = null
): GGPlot = appendSpec {

}