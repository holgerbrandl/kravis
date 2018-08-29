package com.github.holgerbrandl.kravis.nshelper

import com.github.holgerbrandl.kravis.Aes
import com.github.holgerbrandl.kravis.Aesthetic
import com.github.holgerbrandl.kravis.GGPlot
import krangl.DataFrame

/**
 * @author Holger Brandl
 */
fun DataFrame.ggplot(vararg aes: Pair<Any, Aesthetic>): GGPlot {
    val mappedAes = aes.toMap().mapKeys { it.key.toString() }.toList().toTypedArray()
    return GGPlot(this, Aes(*mappedAes))
}
