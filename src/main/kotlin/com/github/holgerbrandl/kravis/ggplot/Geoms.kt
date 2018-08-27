package com.github.holgerbrandl.kravis.ggplot

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
    position: Position? = null,
    // boxplot specific args
    notch: Boolean? = null,
    fill: RColor? = null,
    color: RColor? = null
): GGPlot = apply {
    val arg2string = arg2string("position" to position, "notch" to notch, "fill" to fill, "color" to color)

    addLayer("geom_boxplot($arg2string)")
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
    stat: Stat? = null,
    position: Position? = null,
    showLegend: Boolean? = null,

    // geom specific options
    removeNAs: Boolean? = null,
    inheritAes: Boolean? = null,

    // list all the aesthetics it understands
    alpha: Double? = null,
    color: RColor? = null,
    fill: RColor? = null,
    // group, // todo, unclear usage
    shape: Int? = null,
    size: Int? = null,
    stroke: Int? = null // Use the stroke aesthetic to modify the width of the border
): GGPlot = apply {

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
        "alpha" to alpha,
        "color" to color,
        "fill" to fill,
        "shape" to shape,
        "size" to size,
        "stroke" to stroke
    )

    addLayer("geom_point(${args})")
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
): GGPlot = apply {

}

// todo use more constrained aestetics with just the suppored fields
fun GGPlot.geomBar(
    mapping: Aes? = null,
    data: DataFrame? = null,
    stat: Stat? = null,
    position: String = "identity",

    naRm: Boolean = false,
    showLegend: Boolean? = null
    //        inheritAes: Boolean = true
): GGPlot = apply {
    // todo make sure to forward all options
    addLayer("geom_bar()")
}
