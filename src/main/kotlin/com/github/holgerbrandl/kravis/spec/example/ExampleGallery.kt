package com.github.holgerbrandl.kravis.spec.example

import com.github.holgerbrandl.kravis.spec.*
import com.github.holgerbrandl.kravis.spec.EncodingChannel.*
import com.github.holgerbrandl.kravis.spec.MarkType.circle
import krangl.DataFrame
import krangl.DataFrameRow
import krangl.fromCSV
import krangl.fromJson
import java.awt.Color

/**
 * @author Holger Brandl
 */


// see https://altair-viz.github.io/gallery/bubble_health_income.html
fun gapminderScatterVerbose(): VLBuilder<DataFrameRow> {
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

/** see https://altair-viz.github.io/gallery/bubble_health_income.html */
fun gapminderScatter(): VLBuilder<DataFrameRow> {
    val gapminder = DataFrame.fromCSV("https://vega.github.io/vega-lite/data/gapminder-health-income.csv")

    val plot = plotOf(gapminder) {
        mark = Mark(circle, filled = true)

        encoding(x, "income", scale = Scale(ScaleType.Log))
        encoding(size) { "population" }

        // we can also mix with extractor
        //        encoding(size) { it["population"] }
//        encoding(y, "health")

        // or spec out encoding details
        encoding(y, label = "health index", scale = Scale(zero = true)) {
            it["health"]
        }

        encoding(color, value = Color.RED) {}
    }

    return plot

}

/** from https://vega.github.io/vega-lite/examples/circle_binned.html */
fun binnedScatterplot(): VLBuilder<DataFrameRow> {
    val movies = DataFrame.fromJson("https://raw.githubusercontent.com/vega/vega/master/test/data/movies.json")
    //        .take(100)

    val plot = plotOf(movies) {
        mark = Mark(circle)

        encoding(x, "IMDB_Rating", binParams = BinParams(10))
        encoding(y, "Rotten_Tomatoes_Rating", bin = true)
        encoding(size, aggregate = Aggregate.count)
    }

    return plot
}


fun main(args: Array<String>) {
    binnedScatterplot().apply {

        println(buildJson())
        render()
        //        StaticHTMLRenderer(buildJson()).openInChrome()
    }
}
