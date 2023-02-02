package kravis

import org.jetbrains.kotlinx.dataframe.DataFrame

/**
 * @author Holger Brandl
 */



/**
 * The point geom is used to create scatterplots. The scatterplot is most useful for displaying the relationship
 * between two continuous variables. It can be used to compare one continuous and one categorical variable, or
 * two categorical variables.
 *
 * Official reference [geom_point](https://ggplot2.tidyverse.org/reference/geom_point.html)
 *
 * @sample kravis.samples.doScatter
 */
fun GGPlot.geomPoint(
    // generic options to all geoms
    mapping: Aes? = null,
    data: DataFrame<*>? = null,
    stat: Stat = Stat.identity,
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
    size: Double? = null,
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


        "alpha" to requireZeroOne(alpha),
        "color" to color,
        "fill" to fill,
        "shape" to shape,
        "size" to size,
        "stroke" to stroke
    )

    addSpec("geom_point(${args})")
}

internal fun requireZeroOne(d: Double?) = d?.also { require(it in 0.0..1.0) { "alpha must be [0,1] but was $it." } }


/**
 * The boxplot compactly displays the distribution of a continuous variable. It visualises five summary
 * statistics (the median, two hinges and two whiskers), and all "outlying" points individually.
 *
 * Original reference https://ggplot2.tidyverse.org/reference/geom_boxplot.html
 */
fun GGPlot.geomBoxplot(
    // generic options to all geoms
    mapping: Aes? = null,
    data: DataFrame<*>? = null,
    stat: Stat = Stat.boxplot,
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
    size: Double? = null,
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
 * Adds text directly to the plot.
 *
 * Official reference [geom_text](https://ggplot2.tidyverse.org/reference/geom_text.html)
 *
 * @param parse If TRUE, the labels will be parsed into expressions and displayed as described in ?plotmath
 */
fun GGPlot.geomText(
    // generic options to all geoms
    mapping: Aes? = null,
    data: DataFrame<*>? = null,
    stat: Stat = Stat.identity,
    position: Position? = null,
    showLegend: Boolean? = null,
    removeNAs: Boolean = false,
    inheritAes: Boolean = true,

    // custom options
    parse: Boolean = false,
    nudgeX: Double = .0,
    nudgeY: Double = .0,
    checkOverlap: Boolean = false,

    // list all the aesthetics it understands
    alpha: Double? = null,
    color: RColor? = null,
    // group, // todo, unclear usage
    size: Double? = null,
    hjust: Double? = null,
    vjust: Double? = null,

    vararg dotdotdot: Pair<String, String>

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

        "parse" to parse,
        "nudge_x" to nudgeX,
        "nudge_y" to nudgeY,
        "check_overlap" to checkOverlap,

        "alpha" to requireZeroOne(alpha),
        "color" to color,
        "size" to size,
        "hjust" to hjust,
        "vjust" to vjust,

        *dotdotdot.asList().toTypedArray()
    )

    addSpec("geom_text(${args})")
}

/**
 * In a dot plot, the width of a dot corresponds to the bin width (or maximum width, depending on the binning algorithm), and dots are stacked, with each dot representing one observation.
 *
 * Official reference [geom_dotplot](https://ggplot2.tidyverse.org/reference/geom_dotplot.html)
 */
fun GGPlot.geomDotplot(
    // generic options to all geoms
    mapping: Aes? = null,
    data: DataFrame<*>? = null,
    stat: Stat = Stat.identity,
    position: Position = PositionIdentity(),
    showLegend: Boolean? = null,
    removeNAs: Boolean = false,
    inheritAes: Boolean = true,

    // list all the aesthetics it understands
    alpha: Double? = null,
    color: RColor? = null,
    fill: RColor? = null,
    binWidth: Double? = null,

    // specific options
    binaxis: String = "x",
    method: String = "dotdensity",
    binpositions: String = "bygroup",
    stackdir: String = "up",
    stackratio: Double = 1.0,
    dotsize: Double = 1.0
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
        "binwidth" to binWidth,

        "binaxis" to binaxis,
        "method" to method,
        "binpositions" to binpositions,
        "stackdir" to stackdir,
        "stackratio" to stackratio,
        "dotsize" to dotsize
    )

    addSpec("geom_dotplot(${args})")
}


// todo use more constrained aestetics with just the supported fields or validate supported aestehtics
/**
 * There are two types of bar charts: `geom_bar` makes the height of the bar proportional to the number of cases in each group (or if the weight aesthetic is supplied, the sum of the weights). If you want the heights of the bars to represent values in the data, use `geom_col` instead. `geom_bar` uses `stat_count` by default: it counts the number of cases at each x position. `geom_col` uses `stat_identity`: it leaves the data as is.
 *
 * @sample kravis.samples.doBarChart

 */
fun GGPlot.geomBar(
    // generic options to all geoms
    mapping: Aes? = null,
    data: DataFrame<*>? = null,
    stat: Stat = Stat.count,
    position: Position = PositionStack(),
    showLegend: Boolean? = null,
    removeNAs: Boolean = false,
    inheritAes: Boolean = true,

    // list all the aesthetics it understands
    alpha: Double? = null,
    color: RColor? = null,
    fill: RColor? = null,
    // group, // todo, unclear usage
    linetype: LineType? = null,
    size: Double? = null
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
 * There are two types of bar charts: `geom_bar` makes the height of the bar proportional to the number of cases in each group (or if the weight aesthetic is supplied, the sum of the weights). If you want the heights of the bars to represent values in the data, use `geom_col` instead. `geom_bar` uses `stat_count` by default: it counts the number of cases at each x position. `geom_col` uses `stat_identity`: it leaves the data as is.
 */
fun GGPlot.geomCol(
    // generic options to all geoms
    mapping: Aes? = null,
    data: DataFrame<*>? = null,
    position: Position = PositionStack(),
    showLegend: Boolean? = null,
    removeNAs: Boolean = false,
    inheritAes: Boolean = true,

    // list all the aesthetics it understands
    alpha: Double? = null,
    color: RColor? = null,
    fill: RColor? = null,
    // group, // todo, unclear usage
    linetype: LineType? = null,
    size: Double? = null
): GGPlot = appendSpec {
    val dataVar: VarName? = registerDataset(data)

    val args = arg2string(
        "mapping" to mapping?.stringify(),
        "data" to dataVar,
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

    addSpec("geom_col(${args})")
}

/**
 * A geom that draws error bars, defined by an upper and lower value. This is useful e.g., to draw confidence intervals.
 * See https://ggplot2.tidyverse.org/reference/geom_linerange.html
 */
fun GGPlot.geomErrorBar(
    // generic options to all geoms
    mapping: Aes? = null,
    data: DataFrame<*>? = null,
    stat: Stat = Stat.identity,
    position: Position = PositionIdentity(),
    showLegend: Boolean? = null,
    removeNAs: Boolean = false,
    inheritAes: Boolean = true,

    // list all the aesthetics it understands
    alpha: Double? = null,
    color: RColor? = null,
    fill: RColor? = null,
    linetype: LineType? = null,
    size: Double? = null,
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


//
/**
 * geom_path() connects the observations in the order in which they appear in the data. geom_line() connects them in order of the variable on the x axis. geom_step() creates a stairstep plot, highlighting exactly when changes occur. The group aesthetic determines which cases are connected together.
 *
 * @linetype For options see http://sape.inf.usi.ch/quick-reference/ggplot2/linetype
 */
fun GGPlot.geomPath(
    // generic options to all geoms
    mapping: Aes? = null,
    data: DataFrame<*>? = null,
    stat: Stat = Stat.identity,
    position: Position = PositionIdentity(),
    showLegend: Boolean? = null,
    removeNAs: Boolean = false,
    inheritAes: Boolean = true,

    lineEnd: String = "butt",
    lineJoin: String = "round",
    lineMitre: Int = 10,
    arrow: String? = null,

    // list all the aesthetics it understands
    alpha: Double? = null,
    color: RColor? = null,
    linetype: LineType? = null,
    size: Double? = null
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

        "lineend" to lineEnd,
        "linejoin" to lineJoin,
        "linemitre" to lineMitre,
        "arrow" to arrow,


        "alpha" to requireZeroOne(alpha),
        "color" to color,
        "linetype" to linetype,
        "size" to size
    )

    addSpec("geom_path(${args})")
}

//
/**
 * geom_path() connects the observations in the order in which they appear in the data. geom_line() connects them in order of the variable on the x axis. geom_step() creates a stairstep plot, highlighting exactly when changes occur. The group aesthetic determines which cases are connected together.
 *
 * @linetype For options see http://sape.inf.usi.ch/quick-reference/ggplot2/linetype
 */
fun GGPlot.geomLine(
    // generic options to all geoms
    mapping: Aes? = null,
    data: DataFrame<*>? = null,
    stat: Stat = Stat.identity,
    position: Position = PositionIdentity(),
    showLegend: Boolean? = null,
    removeNAs: Boolean = false,
    inheritAes: Boolean = true,

    // list all the aesthetics it understands
    alpha: Double? = null,
    color: RColor? = null,
    linetype: LineType? = null,
    size: Double? = null
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
        "linetype" to linetype,
        "size" to size
    )

    addSpec("geom_line(${args})")
}


/**
 * Displays a y interval defined by ymin and ymax.
 */
fun GGPlot.geomRibbon(
    // generic options to all geoms
    mapping: Aes? = null,
    data: DataFrame<*>? = null,
    stat: Stat = Stat.identity,
    position: Position = PositionIdentity(),
    showLegend: Boolean? = null,
    removeNAs: Boolean = false,
    inheritAes: Boolean = true,

    // list all the aesthetics it understands
    alpha: Double? = null,
    color: RColor? = null,
    fill: RColor? = null,
    linetype: LineType? = null,
    size: Double? = null
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

    addSpec("geom_ribbon(${args})")
}


/**
 * `geomArea` is a special case of `geomRibbon`, where the ymin is fixed to 0 and y is used instead of ymax.
 */
fun GGPlot.geomArea(
    // generic options to all geoms
    mapping: Aes? = null,
    data: DataFrame<*>? = null,
    stat: Stat = Stat.identity,
    position: Position = PositionIdentity(),
    showLegend: Boolean? = null,
    removeNAs: Boolean = false,
    inheritAes: Boolean = true,

    // list all the aesthetics it understands
    alpha: Double? = null,
    color: RColor? = null,
    fill: RColor? = null,
    linetype: LineType? = null,
    size: Double? = null
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

    addSpec("geom_area(${args})")
}

/**
 * geom_segment() draws a straight line between points (x, y) and (xend, yend).
 *
 * @linetype For options see http://sape.inf.usi.ch/quick-reference/ggplot2/linetype
 */
fun GGPlot.geomSegment(
    // generic options to all geoms
    mapping: Aes? = null,
    data: DataFrame<*>? = null,
    stat: Stat = Stat.identity,
    position: Position = PositionIdentity(),
    showLegend: Boolean? = null,
    removeNAs: Boolean = false,
    inheritAes: Boolean = true,

    // list all the aesthetics it understands
    alpha: Double? = null,
    color: RColor? = null,
    linetype: LineType? = null,
    size: Double? = null
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
        "linetype" to linetype,
        "size" to size
    )

    addSpec("geom_segment(${args})")
}

/**
 * geom_path() connects the observations in the order in which they appear in the data. geom_line() connects them in order of the variable on the x axis. geom_step() creates a stairstep plot, highlighting exactly when changes occur. The group aesthetic determines which cases are connected together.
 *
 * @linetype For options see http://sape.inf.usi.ch/quick-reference/ggplot2/linetype
 */

@Suppress("EnumEntryName", "unused")
enum class StepDirection {
    /** Horizontal then vertical */
    hv,
    /** Vertical then horizontal*/
    vh;

    override fun toString(): String {
        return super.toString().quoted
    }
}

fun GGPlot.geomStep(
    // generic options to all geoms
    mapping: Aes? = null,
    data: DataFrame<*>? = null,
    stat: Stat = Stat.identity,
    position: Position = PositionIdentity(),
    showLegend: Boolean? = null,
    removeNAs: Boolean = false,
    inheritAes: Boolean = true,

    direction: StepDirection = StepDirection.hv,

    // list all the aesthetics it understands
    alpha: Double? = null,
    color: RColor? = null,
    linetype: LineType? = null,
    size: Double? = null
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


        "direction" to direction,


        "alpha" to requireZeroOne(alpha),
        "color" to color,
        "linetype" to linetype,
        "size" to size
    )

    addSpec("geom_step(${args})")
}

/**
 * Visualise the distribution of a single continuous variable by dividing the x axis into bins and counting the number of observations in each bin. Histograms (geom_histogram) display the count with bars
 *
 * For a complete reference of the underlying method see https://ggplot2.tidyverse.org/reference/geom_histogram.html
 *
 * @param binWidth The width of the bins. Can be specified as a numeric value, or a function that calculates width from x. The default is to use bins bins that cover the range of the data. You should always override this value, exploring multiple widths to find the best to illustrate the stories in your data. The bin width of a date variable is the number of days in each time; the bin width of a time variable is the number of seconds.
 *
 * @param bins Number of bins. Overridden by binwidth. Defaults to 30.
 *
 */
fun GGPlot.geomHistogram(
    // generic options to all geoms
    mapping: Aes? = null,
    data: DataFrame<*>? = null,
    stat: Stat = Stat.bin,
    position: Position = PositionStack(),
    showLegend: Boolean? = null,
    removeNAs: Boolean = false,
    inheritAes: Boolean = true,

    binWidth: Double? = null,
    bins: Int = 30,

    // list all the aesthetics it understands
    alpha: Double? = null,
    color: RColor? = null,
    fill: RColor? = null,
    group: String? = null,
    linetype: LineType? = null,
    size: Double? = null
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

        "binwidth" to binWidth,
        "bins" to bins,

        "alpha" to alpha,
        "color" to color,
        "color" to color,
        "fill" to fill,
        "group" to group,
        "linetype" to linetype,
        "size" to size
    )

    addSpec("geom_histogram(${args})")
}


/**
 * Visualize heatmaps. geom_tile uses the center of the tile and its size (x, y, width, height)
 *
 * For a complete reference of the underlying method see https://ggplot2.tidyverse.org/reference/geom_tile.html
 */
fun GGPlot.geomTile(
    // generic options to all geoms
    mapping: Aes? = null,
    data: DataFrame<*>? = null,
    stat: Stat = Stat.identity,
    position: Position = PositionIdentity(),
    showLegend: Boolean? = null,
    removeNAs: Boolean = false,
    inheritAes: Boolean = true,

    // list all the aesthetics it understands
    alpha: Double? = null,
    color: RColor? = null,
    fill: RColor? = null,
    height: RColor? = null,
    width: RColor? = null,
    size: Double? = null
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
        "height" to height,
        "width" to width,
        "size" to size
    )

    addSpec("geom_tile(${args})")
}

/**
 * Divides the plane into rectangles, counts the number of cases in each rectangle, and then (by default) maps the number of cases to the rectangle's fill. This is a useful alternative to geom_point() in the presence of overplotting.
 *
 * Official Reference https://ggplot2.tidyverse.org/reference/geom_bin2d.html
 *
 * @param bins Number of bins. Overridden by binwidth. Defaults to 30.
 * @param binWidth Numeric vector giving bin width in both vertical and horizontal directions. Overrides bins if both set.

 */
fun GGPlot.geomBin2D(
    // generic options to all geoms
    mapping: Aes? = null,
    data: DataFrame<*>? = null,
    stat: Stat = Stat.bin2d,
    position: Position = PositionIdentity(),
    showLegend: Boolean? = null,
    removeNAs: Boolean = false,
    inheritAes: Boolean = true,

    bins: Int = 30,
    binWidth: List<Double>? = null,

    // list all the aesthetics it understands
    fill: RColor? = null
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

        "bins" to bins,
        "binwidth" to binWidth?.toRVector(),

        "fill" to fill
    )

    addSpec("geom_bin2d(${args})")
}




/**
 * Aids the eye in seeing patterns in the presence of overplotting. geom_smooth() and stat_smooth() are effectively aliases: they both use the same arguments. Use stat_smooth() if you want to display the results with a non-standard geom.
 *
 * For a complete reference of the underlying method see https://ggplot2.tidyverse.org/reference/geom_smooth.html
 *
 * @param method Smoothing method (function) to use, accepts either NULL or a character vector, e.g. "lm", "glm", "gam", "loess" or a function, e.g. MASS::rlm or mgcv::gam, stats::lm, or stats::loess. "auto" is also accepted for backwards compatibility. It is equivalent to NULL. For method = NULL the smoothing method is chosen based on the size of the largest group (across all panels). stats::loess() is used for less than 1,000 observations; otherwise mgcv::gam() is used with formula = y ~ s(x, bs = "cs") with method = "REML".
 * @param formula Formula to use in smoothing function, eg. y ~ x, y ~ poly(x, 2), y ~ log(x). NULL by default, in which case method = NULL implies formula = y ~ x when there are fewer than 1,000 observations and formula = y ~ s(x, bs = "cs") otherwise.
 * @param se Display confidence interval around smooth? (TRUE by default, see level to control.)
 * @param span Controls the amount of smoothing for the default loess smoother. Smaller numbers produce wigglier lines, larger numbers produce smoother lines. Only used with loess, i.e. when method = "loess", or when method = NULL (the default) and there are fewer than 1,000 observations.
 */
fun GGPlot.geomSmooth(
    // generic options to all geoms
    mapping: Aes? = null,
    data: DataFrame<*>? = null,
    stat: Stat = Stat.smooth,
    position: Position = PositionIdentity(),
    showLegend: Boolean? = null,
    removeNAs: Boolean = false,
    inheritAes: Boolean = true,

    //geom specific
    method: String? = null,
    formula: String? = null,
    span: Int? = null,
    se: Boolean = true,

    // list all the aesthetics it understands
    alpha: Double? = null,
    color: RColor? = null,
    linetype: LineType? = null,
    fill: RColor? = null,
    height: RColor? = null,
    width: RColor? = null,
    size: Double? = null,
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
        "linetype" to linetype,
        "fill" to fill,
        "height" to height,
        "width" to width,
        "size" to size,

        "method" to method,
        "formula" to formula,
        "span" to span,
        "se" to se,
    )

    addSpec("geom_smooth(${args})")
}
