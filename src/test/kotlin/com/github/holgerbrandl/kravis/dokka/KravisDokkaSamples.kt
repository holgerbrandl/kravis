package com.github.holgerbrandl.kravis.dokka

import com.github.holgerbrandl.kravis.spec.xplot.plot
import krangl.sleepData

/**
 * @author Holger Brandl
 */
fun main(args: Array<String>) {
    doBarChart()
}

fun doBarChart() {

    fun main(args: Array<String>) {

        sleepData
            .plot()
            .x { "genus" } // or even do on the fly data transformation using krangl's table expressions
            //            .y { "Sepal.Length" }
            //            .color { "Species" }
            //            .title("Iris Flowers")
            //            .addBars(mapping{
            //
            //            })
            .show()
    }


}

