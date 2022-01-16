package kravis

import krangl.irisData
import java.util.*

class VarName(val name: String) {
    override fun toString() = name
}


typealias Limits = Pair<Double, Double>

internal fun Limits.toRVector(rExpr: Boolean = true): String = "c($first, $second)".run { if (rExpr) asRExpression else this }


internal fun Iterable<Any>.toRVector(): String = map { it.toStringAndQuote() }.joinToString(", ").run { "c($this)" }.asRExpression


internal fun <K, V> Map<K, V>.toRNamedVector(): String = map { (key, value) ->
    key!!.toStringAndQuote() + " = " + value!!.toStringAndQuote()
}.joinToString(", ").run { "c($this)" }.asRExpression

internal fun Iterable<Any>.toVars(): String = joinToString(", ").run { "vars($this)" }.asRExpression


internal fun Any.toStringAndQuote(): String? {
    val isRExpression = toString().isRExpression
    return when {
        isRExpression -> toString().removePrefix(EXPRESSION_PREFIX)
        //    is VarName -> this.toString()
        this is Aes -> this.toString().nullIfEmpty()
        this is Boolean -> toRString()
        this is RColor -> "'${this}'"
        this is Stat -> "'${this}'"
        this is String -> "'${this}'"

        else -> this.toString()
    }
}

internal fun Boolean.toRString() = this.toString().uppercase(Locale.US)

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

fun main() {
    //    plot(irisData, Aestethics("R" to x)).geomBar().show()
    GGPlot(irisData, Aes("Sepal.Length" to Aesthetic.x, "Petal.Width" to Aesthetic.y)).geomPoint(alpha = 0.1).title("Cool Plot").show()
    GGPlot(irisData, Aes("Sepal.Length" to Aesthetic.x, "Petal.Width" to Aesthetic.y)).geomPoint(alpha = 0.1).title("Another Cool Plot").show()
    GGPlot(irisData, Aes("Sepal.Length" to Aesthetic.x, "Petal.Width" to Aesthetic.y)).geomPoint(alpha = 0.1).title("Yet Another Cool Plot").show()

}

// note we should rather use some receive context if possible here
val String.asDiscreteVariable: String
    get() = "as.factor($this)".asRExpression

internal val EXPRESSION_PREFIX = ".r_expression."

val String.asRExpression: String
    get() = EXPRESSION_PREFIX + this

internal val String.isRExpression: Boolean
    get() = startsWith(EXPRESSION_PREFIX)


fun infoMsg(msg: String) = System.err.println("[kravis] " + msg)


fun warnMsg(msg: String) = System.err.println("[kravis] [WARN] " + msg)


fun errorMsg(msg: String) = System.err.println("[kravis] [ERROR] " + msg)


class MissingAestheticsMapping : RuntimeException() {}
