package kravis


/**
 * facet_wrap wraps a 1d sequence of panels into 2d. This is generally a better use of screen space than facet_grid()
 * because most displays are roughly rectangular.
 *
 * @param facets A set of variables or expressions quoted by vars() and defining faceting groups on the rows or columns
 * dimension. The variables can be named (the names are passed to labeller). For compatibility with the classic
 * interface, can also be a formula or character vector. Use either a one sided formula, '~a b, or a character
 * vector,c("a", "b")'.
 * @param ncol Number of rows.
 * @param ncol Number of columns.
 * @param scales Should scales be fixed ("fixed", the default), free ("free"), or free in one dimension ("free_x",
 * "free_y")?
 * @param shrink If TRUE, will shrink scales to fit output of statistics, not raw data. If FALSE, will be range of raw
 * data before statistical summary.
 * @param labeller A function that takes one data frame of labels and returns a list or data frame of character vectors.
 * Each input column corresponds to one factor. Thus there will be more than one with formulae of the type ~cyl + am.
 * Each output column gets displayed as one separate line in the strip label. This function should inherit from the
 * "labeller" S3 class for compatibility with labeller(). See label_value() for more details and pointers to other
 * options.
 * @param asTable If TRUE, the default, the facets are laid out like a table with highest values at the bottom-right.
 * If FALSE, the facets are laid out like a plot with the highest value at the top-right.
 * @param switch By default, the labels are displayed on the top and right of the plot. If "x", the top labels will be
 * displayed to the bottom. If "y", the right-hand side labels will be displayed to the left. Can also be set to
 * "both".
 * @param drop If TRUE, the default, all factor levels not used in the data will automatically be dropped. If FALSE, all
 * factor levels will be shown, regardless of whether or not they appear in the data.
 * @param dir Direction: either "h" for horizontal, the default, or "v", for vertical.
 * @param stripPosition By default, the labels are displayed on the top of the plot. Using strip.position it is
 * possible to place the labels on either of the four sides by setting strip.position = c("top", "bottom", "left",
 * "right")
 */
@Suppress("KDocUnresolvedReference")
fun GGPlot.facetWrap(
    vararg facets: String,
    nrow: Int? = null,
    ncol: Int? = null,
    scales: FacetScales = FacetScales.fixed,
    shrink: Boolean = true,
//    labeller: String = "label_value", // todo enable later
    asTable: Boolean = true,
    switch: FacetSwitch? = null,
    drop: Boolean = true,
    dir: String = "h",
    stripPosition: StripPosition = StripPosition.top
) = appendSpec {
    val args = arg2string(
        "facets" to facets.asList().apply { require(isNotEmpty()) { "facets must not be empty" } }.toRVector(),
        "nrow" to nrow,
        "ncol" to ncol,
        "scales" to scales,
        "shrink" to shrink,
//        "labeller" to labeller,
        "as.table" to asTable,
        "switch" to switch,
        "drop" to drop,
        "dir" to dir,
        "strip" to stripPosition
    )

    addSpec("""facet_wrap($args)""")
}

/**
 * facet_grid forms a matrix of panels defined by row and column faceting variables. It is most useful when you have two discrete variables, and all combinations of the variables exist in the data.

 *
 * @param rows A set of variables or expressions defining faceting groups on the rows dimension.
 * @param cols A set of variables or expressions defining faceting groups on the columns dimension.
 * @param scales Should scales be fixed ("fixed", the default), free ("free"), or free in one dimension ("free_x",
 * "free_y")?
 * @param space If "fixed", the default, all panels have the same size. If "free_y" their height will be proportional to the length of the y scale; if "free_x" their width will be proportional to the length of the x scale; or if "free" both height and width will vary. This setting has no effect unless the appropriate scales also vary.
 * @param shrink If TRUE, will shrink scales to fit output of statistics, not raw data. If FALSE, will be range of raw
 * data before statistical summary.
 * @param labeller A function that takes one data frame of labels and returns a list or data frame of character vectors.
 * Each input column corresponds to one factor. Thus there will be more than one with formulae of the type ~cyl + am.
 * Each output column gets displayed as one separate line in the strip label. This function should inherit from the
 * "labeller" S3 class for compatibility with labeller(). See label_value() for more details and pointers to other
 * options.
 * @param asTable If TRUE, the default, the facets are laid out like a table with highest values at the bottom-right.
 * If FALSE, the facets are laid out like a plot with the highest value at the top-right.
 * @param switch By default, the labels are displayed on the top and right of the plot. If "x", the top labels will be
 * displayed to the bottom. If "y", the right-hand side labels will be displayed to the left. Can also be set to
 * "both".
 * @param drop If TRUE, the default, all factor levels not used in the data will automatically be dropped. If FALSE, all
 * factor levels will be shown, regardless of whether or not they appear in the data.
 * @param stripPosition By default, the labels are displayed on the top of the plot. Using strip.position it is
 * possible to place the labels on either of the four sides by setting strip.position = c("top", "bottom", "left",
 * "right")
 */
@Suppress("KDocUnresolvedReference")
fun GGPlot.facetGrid(
    rows: Any? = null,
    cols: Any? = null,
    scales: FacetScales = FacetScales.fixed,
    space: FacetScales = FacetScales.fixed,
    shrink: Boolean = true,
//    labeller: String = "label_value", // todo enable later
    asTable: Boolean = true,
    switch: FacetSwitch? = null,
    drop: Boolean = true,
    margins: Boolean = false
) = appendSpec {
    val args = arg2string(
        "rows" to rows?.toStringList()?.toVars(),
        "cols" to cols?.toStringList()?.toVars(),
        "scales" to scales,
        "space" to space,
        "shrink" to shrink,
//        "labeller" to labeller,
        "as.table" to asTable,
        "switch" to switch,
        "drop" to drop,
        "margins" to margins
    )

    addSpec("""facet_grid($args)""")
}

private fun Any.toStringList(): List<String> = when (this) {
    is Iterable<*> -> this.map { it.toString() }
    else -> listOf(this.toString())
}


//fun GGPlot.facetGrid(
//    rows: String,
//    cols: String,
//    scales: FacetScales = FacetScales.fixed,
//    space: FacetScales = FacetScales.fixed,
//    shrink: Boolean = true,
//    asTable: Boolean = true,
//    switch: FacetSwitch? = null,
//    drop: Boolean = true,
//    margins: Boolean = false
//) = facetGrid(listOf(rows), listOf(cols), scales, space, shrink, asTable, switch, drop, margins )

@Suppress("EnumEntryName", "unused")
enum class FacetScales {
    fixed, free, free_x, free_y;

    override fun toString(): String = super.toString().quoted
}

@Suppress("EnumEntryName", "unused")
enum class FacetSwitch {
    both, x, y;

    override fun toString(): String = super.toString().quoted

}

@Suppress("EnumEntryName", "unused")
enum class StripPosition {
    top, bottom, left, right;

    override fun toString(): String = super.toString().quoted
}

