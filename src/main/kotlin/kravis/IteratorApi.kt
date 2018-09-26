package kravis

import krangl.*
import skipNull
import kotlin.reflect.KProperty0
import kotlin.reflect.KProperty1

infix fun <T> Aesthetic.to(any: PropExtractor<T>): Pair<Aesthetic, PropExtractor<T>> = Pair(this, any)
//infix fun <T> Aesthetic.to2(any: PropExtractor<T>): Pair<Aesthetic, PropExtractor<T>> = Pair(this, any)

typealias PropExtractor<T> = T.(T) -> Any?


//@Deprecated("use with dedicated nullable parameters instead")
inline fun <reified T> Iterable<T>.plot(vararg aes2data: Pair<Aesthetic, PropExtractor<T>>): GGPlot {
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
inline fun <reified T> Iterable<T>.plot(
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
        .skipNull(Aesthetic.x, x)
        .skipNull(Aesthetic.y, y)
        .skipNull(Aesthetic.alpha, alpha)
        .skipNull(Aesthetic.color, color)
        .skipNull(Aesthetic.fill, fill)
        .skipNull(Aesthetic.shape, shape)
        .skipNull(Aesthetic.size, size)
        .skipNull(Aesthetic.stroke, stroke)

    return plot(*mapping.toTypedArray())
}


internal object ExtractorPlots {
    @JvmStatic
    fun main(args: Array<String>) {
        val sleepPatterns = krangl.sleepData.rowsAs<SleepPattern>()

        "to" to { it: SleepPattern -> it.awake }
        sleepPatterns.deparseRecords { mapOf("awake" to it.awake, "genus" to it.genus) }

        sleepPatterns.asDataFrame().plot()


        krangl.sleepPatterns.deparseRecords(
            "foo" with { awake },
            "bar" with { it.brainwt?.plus(3) }
        ).head().print()
    }
}


/** Construct a plot by simply providing references to properties. This will allow to name visual axes correctly. */
inline fun <reified T> Iterable<T>.plot(
    x: KProperty1<T, *>? = null,
    y: KProperty1<T, *>? = null,
    alpha: KProperty1<T, *>? = null,
    color: KProperty1<T, *>? = null,
    fill: KProperty1<T, *>? = null,
    shape: KProperty1<T, *>? = null,
    size: KProperty1<T, *>? = null,
    stroke: KProperty1<T, *>? = null
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

    val deparseFormulae = mapping.map { (_, kprop) ->

        val name = kprop.name
        val deparseFormula: T.(T) -> Any? = { a -> kprop.get(a) }
        name to deparseFormula
    }

    val deparsedReceiver = deparseRecords(*deparseFormulae.toTypedArray())


    // build new mapping
    val col2aes = deparseFormulae.zip(mapping).map { (formuale, aes2prop) ->
        formuale.first to aes2prop.first
    }

    return deparsedReceiver.plot(*col2aes.toTypedArray())
}


/** Construct a plot by simply providing references to properties.*/
// note this internalize for now becaus it's too similar to the extractor api. It would still allow to extract correct column names
internal inline fun <reified T> Iterable<T>.ggplot3(
    x: T.() -> KProperty0<*>,
    y: T.() -> KProperty0<*>
): GGPlot {
    // build df from data


    val map = map { x(it) to y(it) }
    map.first().first.name

    // later convert data to actual plot
    TODO()

//    return ggplot()
}


internal object KProperty0Plot {

    @JvmStatic
    fun main(args: Array<String>) {


        // extractor lambda
        sleepPatterns.plot(
            x = { conservation },
            y = { bodywt }
        )

        // KProperty1
        sleepPatterns.plot(
            x = SleepPattern::sleep_rem,
            y = SleepPattern::sleep_total,
            color = SleepPattern::vore,
            size = SleepPattern::brainwt
        )
            .geomPoint()
            .title("Correlation of total sleep and and rem sleep by food preference")
            .show()

        // KProperty2
        sleepPatterns.ggplot3(
            x = { ::conservation },
            y = { ::bodywt }
        )
    }
}

