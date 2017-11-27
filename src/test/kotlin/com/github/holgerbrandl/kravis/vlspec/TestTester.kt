package com.github.holgerbrandl.kravis.vlspec

import com.github.holgerbrandl.kravis.StaticHTMLRenderer
import com.github.holgerbrandl.kravis.spec.*

/**
 * @author Holger Brandl
 */

fun main(args: Array<String>) {


    //    val plotOf = plotOf(sleepPatterns) {
    //        encoding(x) { sleep_total }
    //        encoding(y) { sleep_rem }
    //    }
    val plotOf = plotOf(sleepPatterns) {
        mark = Mark.bar
        title = "distribution of sleep length"

        encoding(EncodingChannel.x, bin = true, axis = Axis("Total Sleep (h)")) { sleep_total }
        encoding(EncodingChannel.y, aggregate = Aggregate.count)
    }

    //    println(plotOf.buildJson())
    plotOf.render()
    StaticHTMLRenderer(plotOf.buildJson()).openInChrome()

}