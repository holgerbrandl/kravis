package kravis

import java.awt.Point


//todo redo dotdotdot
fun GGPlot.scaleXLog10(vararg dotdotdot: Pair<String, String>): GGPlot = appendSpec {
    addSpec("""scale_x_log10(${arg2string(*dotdotdot.asList().toTypedArray())})""")
}


//todo redo dotdotdot
fun GGPlot.scaleYLog10(vararg dotdotdot: Pair<String, String>): GGPlot = appendSpec {
    addSpec("""scale_y_log10(${arg2string(*dotdotdot.asList().toTypedArray())})""")
}

/**
 * Modifies the x axis' appearance, limits, and rendering.
 *
 * @ param breaks A character vector of breaks
 * @param limits A character vector that defines possible values of the scale and their order.
 * @param drop Should unused factor levels be omitted from the scale? The default, TRUE, uses the levels that appear in
 * the data ; FALSE uses all the levels in the factor.
 * @param naTranslate Unlike continuous scales, discrete scales can easily show missing values, and do so by default.
 * If you want to remove missing values from a discrete scale, specify na.translate = FALSE.
 * @param naValue If na.translate = TRUE, what value aesthetic value should missing be displayed as? Does not apply to
 * position scales where NA is always placed at the far right.
 */
fun GGPlot.scaleXDiscrete(
    breaks: List<String>? = null,
    limits: List<String>? = null,
    drop: Boolean = true,
    naTranslate: Boolean = true,
    naValue: String? = null,
    expand: List<Double>? = null,
    position: XScalePosition = XScalePosition.bottom
) = appendSpec {
    val args = arg2string(
//        *dotdotdot.asList().toTypedArray()
        "breaks" to breaks?.toRVector(),
        "limits" to limits?.toRVector(),
        "drop" to drop,
        "na.translate" to naTranslate,
        "na.value" to naValue,
        "expand" to expand?.toRVector(),
        "position" to position
    )

    addSpec("""scale_x_discrete($args)""")
}
/**
 * Modifies the y axis' appearance, limits, and rendering.
 *
 * @ param breaks A character vector of breaks
 * @param limits A character vector that defines possible values of the scale and their order.
 * @param drop Should unused factor levels be omitted from the scale? The default, TRUE, uses the levels that appear in
 * the data ; FALSE uses all the levels in the factor.
 * @param naTranslate Unlike continuous scales, discrete scales can easily show missing values, and do so by default.
 * If you want to remove missing values from a discrete scale, specify na.translate = FALSE.
 * @param naValue If na.translate = TRUE, what value aesthetic value should missing be displayed as? Does not apply to
 * position scales where NA is always placed at the far right.
 */
fun GGPlot.scaleYDiscrete(
    breaks: List<String>? = null,
    limits: List<String>? = null,
    drop: Boolean = true,
    naTranslate: Boolean = true,
    naValue: String? = null,
    expand: List<Double>? = null,
    position: YScalePosition = YScalePosition.left
) = appendSpec {
    val args = arg2string(
//        *dotdotdot.asList().toTypedArray()
        "breaks" to breaks?.toRVector(),
        "limits" to limits?.toRVector(),
        "drop" to drop,
        "na.translate" to naTranslate,
        "na.value" to naValue,
        "expand" to expand?.toRVector(),
        "position" to position
    )

    addSpec("""scale_y_discrete($args)""")
}


enum class XScalePosition {
    bottom, center;

    override fun toString() = super.toString().quoted
}

enum class YScalePosition {
    left, right;

    override fun toString() = super.toString().quoted
}
