package kravis

import krangl.irisData

class VarName(val name: String) {
    override fun toString() = name
}

internal fun Any.toStringAndQuote() = when (this) {
    is String -> "'${this}'"
    //    is VarName -> this.toString()
    is Aes -> this.toString().nullIfEmpty()
    is Boolean -> this.toString().toUpperCase()
    is RColor -> "'${this}'"
    is BarStat -> "'${this}'"
    else -> this
}

private fun String.nullIfEmpty(): String? {
    if (isEmpty()) return null else return this
}

/** Concatenates string-value pairs for which the value is not null*/
internal fun arg2string(vararg namedArgs: Pair<String, Any?>) =
    namedArgs.toMap()
        .filterValues { it != null }
        .map { "${it.key}=${it.value!!.toStringAndQuote()}" }
        .joinToString(", ")

internal val String.quoted: String
    get() = "'" + this + "'"

fun main(args: Array<String>) {
    //    ggplot(irisData, Aestethics("R" to x)).geomBar().show()
    GGPlot(irisData, Aes("Sepal.Length" to Aesthetic.x, "Petal.Width" to Aesthetic.y)).geomPoint(alpha = 0.1).title("Cool Plot").show()
    GGPlot(irisData, Aes("Sepal.Length" to Aesthetic.x, "Petal.Width" to Aesthetic.y)).geomPoint(alpha = 0.1).title("Another Cool Plot").show()
    GGPlot(irisData, Aes("Sepal.Length" to Aesthetic.x, "Petal.Width" to Aesthetic.y)).geomPoint(alpha = 0.1).title("Yet Another Cool Plot").show()

}

// note we should rather use some receive context if possible here
val String.asDiscreteVariable: String
    get() = "as.factor($this)".asRExpression
internal val EXPRESSION_PREFIX = ".r_expression."
internal val String.asRExpression: String
    get() = EXPRESSION_PREFIX + this
internal val String.isRExpression: Boolean
    get() = startsWith(EXPRESSION_PREFIX)