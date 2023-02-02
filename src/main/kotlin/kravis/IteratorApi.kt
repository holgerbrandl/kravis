package kravis

import com.github.holgerbrandl.kdfutils.toKotlinDF
import krangl.deparseRecords
import org.jetbrains.kotlinx.dataframe.datasets.sleepData
import skipNull
import kotlin.reflect.KProperty0
import kotlin.reflect.KProperty1

infix fun <T> Aesthetic.to(any: PropExtractor<T>): Pair<Aesthetic, PropExtractor<T>> = Pair(this, any)
//infix fun <T> Aesthetic.to2(any: PropExtractor<T>): Pair<Aesthetic, PropExtractor<T>> = Pair(this, any)

typealias PropExtractor<T> = T.(T) -> Any?


//@Deprecated("use with dedicated nullable parameters instead")
/**
 * Create a plot by providing a mapping between visual dimensions called `Aesthetic`s and decomposing lambda function
 * that extract data properties from each `T` in the `Iterable`.
 * @sample kravis.samples.iteratorAPI
 */
inline fun <reified T> Iterable<T>.plot(vararg aes2data: Pair<Aesthetic, PropExtractor<T>>): GGPlot {
    //fun <T : Any> Iterable<T>.plot(vararg data: Pair<PropExtractor<T>, Aesthetic>): GGPlot {

    //todo use reflection to get sensible names for variables
    val rulez = aes2data.toMap()
        .mapKeys { (aes, _) -> aes.toString() }

    val aes = aes2data.toMap().keys.map { it.toString() to it }


    val df = this.deparseRecords(*rulez.toList().toTypedArray())


    return GGPlot(data = df.toKotlinDF(), mapping = Aes(*aes.toTypedArray()))
}

/**
 * Start a plot from any type of `Iterable` using a typed expression based builder to map data attributes to visual aesthetics.
 *
 * @sample kravis.samples.iteratorAPI
 */
inline fun <reified T> Iterable<T>.plot(
    noinline x: PropExtractor<T>? = null,
    noinline y: PropExtractor<T>? = null,
    noinline alpha: PropExtractor<T>? = null,
    noinline color: PropExtractor<T>? = null,
    noinline fill: PropExtractor<T>? = null,
    noinline shape: PropExtractor<T>? = null,
    noinline size: PropExtractor<T>? = null,
    noinline stroke: PropExtractor<T>? = null,
    noinline ymin: PropExtractor<T>? = null,
    noinline ymax: PropExtractor<T>? = null,
    noinline xend: PropExtractor<T>? = null,
    noinline yend: PropExtractor<T>? = null,
    noinline weight: PropExtractor<T>? = null,
    noinline label: PropExtractor<T>? = null,
    noinline group: PropExtractor<T>? = null

): GGPlot {
    // build df from data
    val mapping = listOf<Pair<Aesthetic, PropExtractor<T>>>()
        .skipNull(Aesthetic.x, x)
        .skipNull(Aesthetic.y, y)
        .skipNull(Aesthetic.alpha, alpha)
        .skipNull(Aesthetic.color, color)
        .skipNull(Aesthetic.fill, fill)
        .skipNull(Aesthetic.shape, shape)
        .skipNull(Aesthetic.size, size)
        .skipNull(Aesthetic.stroke, stroke)
        .skipNull(Aesthetic.ymin, ymin)
        .skipNull(Aesthetic.ymax, ymax)
        .skipNull(Aesthetic.xend, xend)
        .skipNull(Aesthetic.yend, yend)
        .skipNull(Aesthetic.weight, weight)
        .skipNull(Aesthetic.label, label)
        .skipNull(Aesthetic.group, group)

    return plot(*mapping.toTypedArray())
}



/**
 * Construct a plot by simply providing references to properties. This will allow to name visual axes correctly.
 *
 * @sample kravis.samples.iteratorAPI
 *
 * */
inline fun <reified T> Iterable<T>.plot(
    x: KProperty1<T, *>? = null,
    y: KProperty1<T, *>? = null,
    alpha: KProperty1<T, *>? = null,
    color: KProperty1<T, *>? = null,
    fill: KProperty1<T, *>? = null,
    shape: KProperty1<T, *>? = null,
    size: KProperty1<T, *>? = null,
    stroke: KProperty1<T, *>? = null,
    ymin: KProperty1<T, *>? = null,
    ymax: KProperty1<T, *>? = null,
    xend: KProperty1<T, *>? = null,
    yend: KProperty1<T, *>? = null,
    weight: KProperty1<T, *>? = null,
    label: KProperty1<T, *>? = null,
    group: KProperty1<T, *>? = null
): GGPlot {
    // build df from data

    // later convert data to actual plot
    val mapping = listOf<Pair<Aesthetic, KProperty1<T, *>>>()
        .skipNull(Aesthetic.x, x)
        .skipNull(Aesthetic.y, y)
        .skipNull(Aesthetic.alpha, alpha)
        .skipNull(Aesthetic.color, color)
        .skipNull(Aesthetic.fill, fill)
        .skipNull(Aesthetic.shape, shape)
        .skipNull(Aesthetic.size, size)
        .skipNull(Aesthetic.stroke, stroke)
        .skipNull(Aesthetic.ymin, ymin)
        .skipNull(Aesthetic.ymax, ymax)
        .skipNull(Aesthetic.xend, xend)
        .skipNull(Aesthetic.yend, yend)
        .skipNull(Aesthetic.weight, weight)
        .skipNull(Aesthetic.label, label)
        .skipNull(Aesthetic.group, group)

    val deparseFormulae = mapping.map { (_, kprop) ->

        val name = kprop.name
        val deparseFormula: T.(T) -> Any? = { a -> kprop.get(a) }
        name to deparseFormula
    }

    val deparsedReceiver = deparseRecords(*deparseFormulae.toTypedArray()).toKotlinDF()


    // build new mapping
    val col2aes = deparseFormulae.zip(mapping).map { (formuale, aes2prop) ->
        formuale.first to aes2prop.first
    }

    return deparsedReceiver.plot(*col2aes.toTypedArray())
}
