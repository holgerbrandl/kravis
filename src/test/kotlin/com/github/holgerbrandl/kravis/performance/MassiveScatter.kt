package com.github.holgerbrandl.kravis.performance

import com.github.holgerbrandl.kravis.spec.EncodingChannel.*
import com.github.holgerbrandl.kravis.spec.plotOf
import org.apache.commons.math3.distribution.NormalDistribution

/**
 * @author Holger Brandl
 */

fun main(args: Array<String>) {
    val norm1 = NormalDistribution(1.0, 1.0)


    data class City(val long: Double, val lat: Double, val region: String = "", val wealth: Double? = null)


    val cities = (0..100000).map {
        City(norm1.sample(), norm1.sample(), if (norm1.sample() > 0) "west" else "east")
    }

    println("starting plot")

    val myplot = plotOf(cities) {
        encoding(x) {
            long
        }

        encoding(y) {
            lat
        }

        encoding(color) {
            region
        }
    }


    myplot.render()
}
