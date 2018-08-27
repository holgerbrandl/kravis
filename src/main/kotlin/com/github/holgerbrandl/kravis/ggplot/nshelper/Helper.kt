package com.github.holgerbrandl.kravis.ggplot.nshelper

import com.github.holgerbrandl.kravis.ggplot.Aes
import com.github.holgerbrandl.kravis.ggplot.Aesthetic
import com.github.holgerbrandl.kravis.ggplot.GGPlot
import krangl.DataFrame

/**
 * @author Holger Brandl
 */
fun DataFrame.ggplot(vararg aes: Pair<Any, Aesthetic>): GGPlot {
    val mappedAes = aes.toMap().mapKeys { it.key.toString() }.toList().toTypedArray()
    return GGPlot(this, Aes(*mappedAes))
}
