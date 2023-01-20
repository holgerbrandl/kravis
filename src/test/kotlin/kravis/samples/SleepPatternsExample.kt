package kravis.samples

import krangl.SleepPattern
import kravis.GGPlot
import kravis.geomPoint
import kravis.plot
import org.jetbrains.kotlinx.dataframe.datasets.sleepPatterns
import kotlin.reflect.KProperty0

internal object SleepPatternsExample {

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



/**
 * Construct a plot by simply providing references to properties.
 *
 * @sample kravis.samples.iteratorAPI
 */
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

//    return plot()
}


