package kravis

import krangl.SleepPattern
import krangl.deparseRecords
import krangl.rowsAs

infix fun <T> Aesthetic.to(any: PropExtractor<T>): Pair<Aesthetic, PropExtractor<T>> = Pair(this, any)
//infix fun <T> Aesthetic.to2(any: PropExtractor<T>): Pair<Aesthetic, PropExtractor<T>> = Pair(this, any)

typealias PropExtractor<T> = T.(T) -> Any?


inline fun <reified T> Iterable<T>.ggplot(vararg aes2data: Pair<Aesthetic, PropExtractor<T>>): GGPlot {
    //fun <T : Any> Iterable<T>.ggplot(vararg data: Pair<PropExtractor<T>, Aesthetic>): GGPlot {

    //todo use reflection to get sensible names for variables
    val rulez = aes2data.toMap()
        .mapKeys { (aes, extract) -> aes.toString() }

    val aes = aes2data.toMap().keys.map { it.toString() to it }


    val df = this.deparseRecords(*rulez.toList().toTypedArray())


    return GGPlot(data = df, mapping = Aes(*aes.toTypedArray()))
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
