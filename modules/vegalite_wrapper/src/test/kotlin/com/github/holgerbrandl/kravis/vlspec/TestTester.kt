package com.github.holgerbrandl.kravis.vlspec

import com.github.holgerbrandl.kravis.StaticHTMLRenderer
import com.github.holgerbrandl.kravis.spec.Aggregate
import com.github.holgerbrandl.kravis.spec.Axis
import com.github.holgerbrandl.kravis.spec.EncodingChannel
import com.github.holgerbrandl.kravis.spec.plotOf
import krangl.sleepPatterns

/**
 * @author Holger Brandl
 */

fun main(args: Array<String>) {


    //    val plotOf = plotOf(sleepPatterns) {
    //        encoding(x) { sleep_total }
    //        encoding(y) { sleep_rem }
    //    }
    val plotOf = plotOf(sleepPatterns) {
//        mark = Mark(bar) // should be optional
        title = "distribution of sleep length"

        encoding(EncodingChannel.x, axis = Axis("Total Sleep (h)"), bin = true) { sleep_total }
        encoding(EncodingChannel.y, aggregate = Aggregate.count)
    }

    //    println(plotOf.buildJson())
    plotOf.render()
    StaticHTMLRenderer(plotOf.buildJson()).openInChrome()

}