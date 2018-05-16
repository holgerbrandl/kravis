package com.github.holgerbrandl.kravis

import com.github.holgerbrandl.kravis.spec.*
import krangl.DataFrame
import krangl.dataFrameOf

/**
 * @author Holger Brandl
 */


/** Also see https://github.com/haifengl/smile/blob/master/plot/src/main/java/smile/plot/Heatmap.java */
fun DataFrame.heatmap(x: String, y: String, color: String) {
    val heatmap = plotOf(this) {

        mark = Mark(MarkType.rect)

        encoding(EncodingChannel.x, x, type = Type.nominal)
        encoding(EncodingChannel.y, y, type = Type.nominal)
        encoding(EncodingChannel.color, color, aggregate = Aggregate.mean)
    }

    heatmap.render()
}

fun main(args: Array<String>) {
    val heatData = dataFrameOf("x", "y", "pixel_value")(
        1, 1, 3.2,
        1, 2, 7.2,
        2, 1, 4.0,
        2, 2, 5.3
    )
    heatData.heatmap("x", "y", "pixel_value")
}