package com.github.holgerbrandl.kravis

import com.github.holgerbrandl.kravis.spec.*
import krangl.sleepPatterns

/**
 * @author Holger Brandl
 */

fun <T : Any> Iterable<T>.histogram(bins: Int = 10, xlabel: String = "", ylabel: String = "n", extractor: PropExtractor<T>? = null): VLBuilder<T> {
    return plotOf(this) {
        encoding(EncodingChannel.x, label = xlabel, bin = true, extractor = extractor)
        encoding(EncodingChannel.y, label = ylabel, aggregate = Aggregate.count)
    }
}


fun main(args: Array<String>) {
    //    val plotOf = plotOf(sleepPatterns) {
    //        encoding(EncodingChannel.x, bin = true) { sleep_total }
    //        encoding(EncodingChannel.y, aggregate = Aggregate.count)
    //    }.render()

    val histogram = sleepPatterns.histogram { sleep_total }
    histogram.render()
}