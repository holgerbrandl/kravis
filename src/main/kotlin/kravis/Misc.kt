package kravis

/**
 * Guides for each scale can be set scale-by-scale with the guide argument, or en masse with guides().
 */
fun GGPlot.guides(
    alpha: LegendType? = null,
    color: LegendType? = null,
    fill: LegendType? = null,
    shape: LegendType? = null,
    size: LegendType? = null,
    stroke: LegendType? = null
) = appendSpec {

    val args = mapOf(
        "alpha" to  alpha,
        "color" to  color,
        "fill" to  fill,
        "shape" to  shape,
        "size" to  size
    ).filterValues { it!=null }.toList().run{arg2string(*this.toTypedArray())}


    addSpec("""guides($args)""")
}

enum class LegendType{
    none, colorbar, legend;

    override fun toString(): String {
        return super.toString().quoted
    }}

