package kravis.nshelper

import krangl.DataFrame
import kravis.Aes
import kravis.Aesthetic
import kravis.GGPlot

/**
 * @author Holger Brandl
 */
fun DataFrame.ggplot(vararg aes: Pair<Any, Aesthetic>): GGPlot {
    val mappedAes = aes.toMap().mapKeys { it.key.toString() }.toList().toTypedArray()
    return GGPlot(this, Aes(*mappedAes))
}
