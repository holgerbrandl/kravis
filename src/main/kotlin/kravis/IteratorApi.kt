package kravis

import krangl.SleepPattern
import krangl.deparseRecords
import krangl.rowsAs

infix fun <T> Aesthetic.to(any: PropExtractor<T>): Pair<Aesthetic, PropExtractor<T>> = Pair(this, any)
//infix fun <T> Aesthetic.to2(any: PropExtractor<T>): Pair<Aesthetic, PropExtractor<T>> = Pair(this, any)

typealias PropExtractor<T> = T.(T) -> Any?


@Deprecated("use with dedicated nullable parameters instead")
inline fun <reified T> Iterable<T>.ggplot(vararg aes2data: Pair<Aesthetic, PropExtractor<T>>): GGPlot {
    //fun <T : Any> Iterable<T>.ggplot(vararg data: Pair<PropExtractor<T>, Aesthetic>): GGPlot {

    //todo use reflection to get sensible names for variables
    val rulez = aes2data.toMap()
        .mapKeys { (aes, extract) -> aes.toString() }

    val aes = aes2data.toMap().keys.map { it.toString() to it }


    val df = this.deparseRecords(*rulez.toList().toTypedArray())


    return GGPlot(data = df, mapping = Aes(*aes.toTypedArray()))
}

/**
 * Start a plot from any type of `Iterable` using a typed expression based builder to map data attributes to visual aesthetics.
 *
 * @sample kravis.dokka.iteratorAPI
 */
inline fun <reified T> Iterable<T>.ggplot(
    noinline x: PropExtractor<T>? = null,
    noinline y: PropExtractor<T>? = null,
    noinline alpha: PropExtractor<T>? = null,
    noinline color: PropExtractor<T>? = null,
    noinline fill: PropExtractor<T>? = null,
    noinline shape: PropExtractor<T>? = null,
    noinline size: PropExtractor<T>? = null,
    noinline stroke: PropExtractor<T>? = null

): GGPlot {
    // build df from data
    val mapping = listOf<Pair<Aesthetic, PropExtractor<T>>>()
        .skipNull(x, Aesthetic.x)
        .skipNull(y, Aesthetic.y)
        .skipNull(alpha, Aesthetic.alpha)
        .skipNull(color, Aesthetic.color)
        .skipNull(fill, Aesthetic.fill)
        .skipNull(shape, Aesthetic.shape)
        .skipNull(size, Aesthetic.size)
        .skipNull(stroke, Aesthetic.stroke)

    return ggplot(*mapping.toTypedArray())
}


// todo hide this from public namespace
fun <T> List<Pair<Aesthetic, T>>.skipNull(x: T?, aes: Aesthetic): List<Pair<Aesthetic, T>> {
    return if (x != null) this + Pair(aes, x) else this
}


fun main(args: Array<String>) {
    val sleepPatterns = krangl.sleepData.rowsAs<SleepPattern>()

    //    "to" to { it: SleepPattern -> it.awake }
    //    sleepPatterns.deparseRecords { mapOf("awake" to it.awake, "genus" to it.genus) }

    //        sleepPatterns.asDataFrame().ggplot()
    //


    //    krangl.sleepPatterns.deparseRecords(
    //        "foo" with { awake },
    //        "bar" with { it.brainwt?.plus(3) }
    //    ).head().print()
}
