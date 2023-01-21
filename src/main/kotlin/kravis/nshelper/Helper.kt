package kravis.nshelper

import kravis.Aes
import kravis.Aesthetic
import kravis.GGPlot
import org.jetbrains.kotlinx.dataframe.DataFrame

/**
 * @author Holger Brandl
 */
fun DataFrame<*>.plot(vararg aes: Pair<Any, Aesthetic>): GGPlot {
    val mappedAes = aes.toMap().mapKeys { it.key.toString() }.toList().toTypedArray()
    return GGPlot(this, Aes(*mappedAes))
}
