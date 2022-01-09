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
        "alpha" to alpha,
        "color" to color,
        "fill" to fill,
        "shape" to shape,
        "size" to size,
        "stroke" to stroke
    ).filterValues { it != null }.toList().run { arg2string(*this.toTypedArray()) }


    addSpec("""guides($args)""")
}

enum class LegendType {
    none, colorbar, legend;

    override fun toString() = super.toString().quoted
}


//@Suppress("EnumEntryName", "SpellCheckingInspection")
//enum class Shape(code: Int) {
//
//    black_square(15), black_circle(16), black_triangle(12);
//
//    internal var code = 1
//
//    override fun toString(): String {
//        return code.toString()
//    }
//
//    companion object {
//
//        fun create(shapeCode: Int) = black_circle.apply {
//
//            require(shapeCode in 0..25 || shapeCode in 32..127) {
//                "$shapeCode is not a valid shape code"
//            }
//
//            return Shape(shapeCode)
//        }
//    }
//}

/**
 * Replacement for Kotlin's deprecated `capitalize()` function.
 *
 * From https://stackoverflow.com/questions/67843986/is-there-a-shorter-replacement-for-kotlins-deprecated-string-capitalize-funct
 */
internal fun String.titlecaseFirstChar() = replaceFirstChar(Char::titlecase)
