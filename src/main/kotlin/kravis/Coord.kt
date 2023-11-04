package kravis



/**
 *The Cartesian coordinate system is the most familiar, and common, type of coordinate system. Setting limits on the coordinate system will zoom the plot (like you're looking at it with a magnifying glass), and will not change the underlying data like setting limits on a scale will.
 *
 * @param ratio aspect ratio, expressed as y / x
 * @param xlim Limits for the x and y axes.
 * @param ylim Limits for the x and y axes.
 * @param expand If TRUE, the default, adds a small expansion factor to the limits to ensure that data and axes don't
 * overlap. If FALSE, limits are taken exactly from the data or xlim/ylim.
 * @param clip Allows drawing of data points anywhere on the plot, including in the plot margins.
 */
fun GGPlot.coordCartesian(
    xlim: Limits? = null,
    ylim: Limits? = null,
    expand: Boolean = true,
    clip: Boolean = true
) = appendSpec {
    val args = arg2string(
        "xlim" to xlim?.toRVector(),
        "ylim" to ylim?.toRVector(),
        "expand" to expand,
        "clip" to if (clip) "on" else "off"
    )

    addSpec("""coord_cartesian($args)""")
}


/**
 *  Supply the limits argument to the x scale. Note that, by default, any values outside the limits will be replaced with NA.
 */
fun GGPlot.xlim(limits: Limits): GGPlot = appendSpec {
    addSpec("""coord_cartesian(xlim = ${limits.toRVector(false)})""")
}

/**
 *  Supply the limits argument to the x scale. Note that, by default, any values outside the limits will be replaced with NA.
 */
fun GGPlot.xlim(lower: Number?, upper: Number?) = xlim(lower to upper)


/**
 *  Supply the limits argument to the y scale. Note that, by default, any values outside the limits will be replaced with NA.
 */
fun GGPlot.ylim(limits: Limits): GGPlot = appendSpec {
    addSpec("""coord_cartesian(ylim = ${limits.toRVector(false)})""")
}

/**
 *  Supply the limits argument to the y scale. Note that, by default, any values outside the limits will be replaced with NA.
 */
fun GGPlot.ylim(lower: Number?, upper: Number?) = ylim(lower to upper)


/**
 * A fixed scale coordinate system forces a specified ratio between the physical representation of data units on the axes. The ratio represents the number of units on the y-axis equivalent to one unit on the x-axis. The default, ratio = 1, ensures that one unit on the x-axis is the same length as one unit on the y-axis. Ratios higher than one make units on the y axis longer than units on the x-axis, and vice versa. This is similar to MASS::eqscplot(), but it works for all types of graphics.
 *
 * @param ratio aspect ratio, expressed as y / x
 * @param xlim Limits for the x and y axes.
 * @param ylim Limits for the x and y axes.
 * @param expand If TRUE, the default, adds a small expansion factor to the limits to ensure that data and axes don't
 * overlap. If FALSE, limits are taken exactly from the data or xlim/ylim.
 * @param clip Allows drawing of data points anywhere on the plot, including in the plot margins.
 */
fun GGPlot.coordFixed(
    ratio: Double = 1.0,
    xlim: Double? = null,
    ylim: Double? = null,
    expand: Boolean = true,
    clip: Boolean = true
) = appendSpec {
    val args = arg2string(
        "ratio" to ratio,
        "xlim" to xlim,
        "ylim" to ylim,
        "expand" to expand,
        "clip" to if (clip) "on" else "off"
    )

    addSpec("""coord_fixed($args)""")
}


/**
 * Flip cartesian coordinates so that horizontal becomes vertical, and vertical, horizontal. This is primarily useful for converting geoms and statistics which display y conditional on x, to x conditional on y.
 *
 * @param xlim Limits for the x and y axes.
 * @param ylim Limits for the x and y axes.
 * @param expand If TRUE, the default, adds a small expansion factor to the limits to ensure that data and axes don't
 * overlap. If FALSE, limits are taken exactly from the data or xlim/ylim.
 * @param clip Allows drawing of data points anywhere on the plot, including in the plot margins.
 */
fun GGPlot.coordFlip(
    xlim: Double? = null,
    ylim: Double? = null,
    expand: Boolean = true,
    clip: Boolean = true
) = appendSpec {
    val args = arg2string(
        "xlim" to xlim,
        "ylim" to ylim,
        "expand" to expand,
        "clip" to if (clip) "on" else "off"
    )

    addSpec("""coord_flip($args)""")
}

