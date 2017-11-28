package com.github.holgerbrandl.kravis.spec.example

import com.github.holgerbrandl.kravis.StaticHTMLRenderer
import com.github.holgerbrandl.kravis.spec.*
import com.github.holgerbrandl.kravis.spec.EncodingChannel.*
import krangl.DataFrame
import krangl.DataFrameRow
import krangl.fromCSV
import java.awt.Color

/**
 * @author Holger Brandl
 */


// see https://altair-viz.github.io/gallery/bubble_health_income.html
fun gapminderScatter(): VLBuilder<DataFrameRow> {
    val gapminder = DataFrame.fromCSV("https://vega.github.io/vega-lite/data/gapminder-health-income.csv")

    val plot = plotOf(gapminder.rows) {
        encoding(x, scale = Scale(ScaleType.Log)) {
            it["income"]
        }

        encoding(size) {
            it["population"]
        }

        encoding(y, scale = Scale(zero = true)) {
            it["health"]
        }
    }


    return plot

}

fun gapminderScatter2(): VLBuilder<DataFrameRow> {
    val gapminder = DataFrame.fromCSV("https://vega.github.io/vega-lite/data/gapminder-health-income.csv")

    val plot = plotOf(gapminder) {
        mark = Mark(MarkType.circle, filled = true)

        encoding(x, "income", scale = Scale(ScaleType.Log))
        encoding(size, "population")

        // we can also mix with extractor
//        encoding(y, "health")
        encoding(y, label= "health index", scale = Scale(zero = true)){
            it["health"]
        }

        encoding(color, value = Color.RED){}
    }

    return plot

}


fun main(args: Array<String>) {
    gapminderScatter2().apply {

        println(buildJson())
        render()
        StaticHTMLRenderer(buildJson()).openInChrome()
    }
}
