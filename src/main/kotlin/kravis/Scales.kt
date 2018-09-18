package kravis


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
 * @param breaks A character vector of breaks
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


//
// MANUAL SCALES
//


/**
 * Allow you to specify your own set of mappings from levels in the data to aesthetic values.
 *
 * For more details see https://ggplot2.tidyverse.org/reference/scale_manual.html
 *
 * @param values A set of aesthetic values to map data values to. If this is a named vector, then the values will be matched based on the names.
 * @param limits A character vector that defines possible values of the scale and their order.
 * @param drop Should unused factor levels be omitted from the scale? The default, TRUE, uses the levels that appear in
 * the data ; FALSE uses all the levels in the factor.
 * @param naTranslate Unlike continuous scales, discrete scales can easily show missing values, and do so by default.
 * If you want to remove missing values from a discrete scale, specify na.translate = FALSE.
 * @param naValue If na.translate = TRUE, what value aesthetic value should missing be displayed as? Does not apply to
 * position scales where NA is always placed at the far right.
 */
fun GGPlot.scaleColorManual(
    values: Map<String, RColor>? = null,
    limits: List<String>? = null,
    drop: Boolean = true,
    naTranslate: Boolean = true,
    naValue: String? = null,
    name: String? = null,
    vararg dotdotdot: Pair<String, String>
) = appendSpec {

    val args = arg2string(
        "values" to values?.toRNamedVector(),

        // common scale options passed on to discrete_scale
        "limits" to limits?.toRVector(),
        "drop" to drop,
        "na.translate" to naTranslate,
        "na.value" to naValue,
        "name" to name,
        *dotdotdot.asList().toTypedArray()
    )

    addSpec("""scale_color_manual($args)""")
}


/**
 * Allow you to specify your own set of mappings from levels in the data to aesthetic values.
 *
 * For more details see https://ggplot2.tidyverse.org/reference/scale_manual.html
 *
 * @param values A set of aesthetic values to map data values to. If this is a named vector, then the values will be matched based on the names.
 * @param limits A character vector that defines possible values of the scale and their order.
 * @param drop Should unused factor levels be omitted from the scale? The default, TRUE, uses the levels that appear in
 * the data ; FALSE uses all the levels in the factor.
 * @param naTranslate Unlike continuous scales, discrete scales can easily show missing values, and do so by default.
 * If you want to remove missing values from a discrete scale, specify na.translate = FALSE.
 * @param naValue If na.translate = TRUE, what value aesthetic value should missing be displayed as? Does not apply to
 * position scales where NA is always placed at the far right.
 */
fun GGPlot.scaleFillManual(
    values: Map<String, RColor>? = null,
    limits: List<String>? = null,
    drop: Boolean = true,
    naTranslate: Boolean = true,
    naValue: String? = null,
    name: String? = null,
    vararg dotdotdot: Pair<String, String>
) = appendSpec {

    val args = arg2string(
        "values" to values?.toRNamedVector(),

        // common scale options passed on to discrete_scale
        "limits" to limits?.toRVector(),
        "drop" to drop,
        "na.translate" to naTranslate,
        "na.value" to naValue,
        "name" to name,
        *dotdotdot.asList().toTypedArray()
    )

    addSpec("""scale_color_manual($args)""")
}

/**
 * Allow you to specify your own set of mappings from levels in the data to aesthetic values.
 *
 * For more details see https://ggplot2.tidyverse.org/reference/scale_manual.html
 *
 * @param values A set of aesthetic values to map data values to. If this is a named vector, then the values will be matched based on the names.
 * @param limits A character vector that defines possible values of the scale and their order.
 * @param drop Should unused factor levels be omitted from the scale? The default, TRUE, uses the levels that appear in
 * the data ; FALSE uses all the levels in the factor.
 * @param naTranslate Unlike continuous scales, discrete scales can easily show missing values, and do so by default.
 * If you want to remove missing values from a discrete scale, specify na.translate = FALSE.
 * @param naValue If na.translate = TRUE, what value aesthetic value should missing be displayed as? Does not apply to
 * position scales where NA is always placed at the far right.
 */
fun GGPlot.scaleSizeManual(
    values: Map<String, Double>? = null,
    limits: List<String>? = null,
    drop: Boolean = true,
    naTranslate: Boolean = true,
    naValue: String? = null,
    name: String? = null,
    vararg dotdotdot: Pair<String, String>
) = appendSpec {

    val args = arg2string(
        "values" to values?.toRNamedVector(),

        // common scale options passed on to discrete_scale
        "limits" to limits?.toRVector(),
        "drop" to drop,
        "na.translate" to naTranslate,
        "na.value" to naValue,
        "name" to name,
        *dotdotdot.asList().toTypedArray()
    )

    addSpec("""scale_size_manual($args)""")
}

/**
 * Allow you to specify your own set of mappings from levels in the data to aesthetic values.
 *
 * Legal shape values are the numbers 0 to 25, and the numbers 32 to 127. Only shapes 21 to 25 are filled (and thus are affected by the fill color), the rest are just drawn in the outline color. Shapes 32 to 127 correspond to the corresponding ASCII characters
 *
 * For details see http://sape.inf.usi.ch/quick-reference/ggplot2/shape
 * ![](http://sape.inf.usi.ch/sites/default/files/ggplot2-shape-identity.png)
 * For more details see https://ggplot2.tidyverse.org/reference/scale_manual.html
 *
 * @param values A set of aesthetic values to map data values to. If this is a named vector, then the values will be matched based on the names.
 * @param limits A character vector that defines possible values of the scale and their order.
 * @param drop Should unused factor levels be omitted from the scale? The default, TRUE, uses the levels that appear in
 * the data ; FALSE uses all the levels in the factor.
 * @param naTranslate Unlike continuous scales, discrete scales can easily show missing values, and do so by default.
 * If you want to remove missing values from a discrete scale, specify na.translate = FALSE.
 * @param naValue If na.translate = TRUE, what value aesthetic value should missing be displayed as? Does not apply to
 * position scales where NA is always placed at the far right.
 */
fun GGPlot.scaleShapeManual(
    values: Map<String, Int>? = null,
//    unnamedValues: List<Int>? = null,
    limits: List<String>? = null,
    drop: Boolean = true,
    naTranslate: Boolean = true,
    naValue: String? = null,
    name: String? = null,
    vararg dotdotdot: Pair<String, String>
) = appendSpec {

    values?.values?.forEach { shapeCode ->
        require(shapeCode in 0..25 || shapeCode in 32..127) {
            "$shapeCode is not a valid shape code"
        }
    }

    val args = arg2string(
        "values" to values?.toRNamedVector(),

        // common scale options passed on to discrete_scale
        "limits" to limits?.toRVector(),
        "drop" to drop,
        "na.translate" to naTranslate,
        "na.value" to naValue,
        "name" to name,
        *dotdotdot.asList().toTypedArray()
    )

    addSpec("""scale_shape_manual($args)""")
}

